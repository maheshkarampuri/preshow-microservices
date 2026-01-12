package com.preshow.payment.service;

import com.preshow.payment.enums.OutboxStatus;
import com.preshow.payment.enums.PaymentStatus;
import com.preshow.payment.model.OutboxEvent;
import com.preshow.payment.model.Payment;
import com.preshow.payment.repository.OutboxEventRepository;
import com.preshow.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentWebhookService {

    private final PaymentRepository paymentRepository;
    private final OutboxEventRepository outboxEventRepository;

//    @Transactional
//    public void process(String payload) {
//
//        JSONObject event = new JSONObject(payload);
//        String eventType = event.getString("event");
//
//        if ("payment.captured".equals(eventType)) {
//            handlePaymentCaptured(event);
//        }
//
//        if ("payment.failed".equals(eventType)) {
//            handlePaymentFailed(event);
//        }
//    }

    @Transactional
    public void process(String payload) {

        JSONObject event = new JSONObject(payload);
        String eventType = event.getString("event");

        switch (eventType) {

            case "payment.captured" -> handlePaymentCaptured(event);
            case "payment.failed"   -> handlePaymentFailed(event);

            case "refund.created",
                 "refund.processed",
                 "refund.failed"     -> handleRefund(event);

            default -> {
                // ignore other events safely
            }
        }
    }

    private void handlePaymentCaptured(JSONObject event) {

        JSONObject paymentEntity =
                event.getJSONObject("payload")
                        .getJSONObject("payment")
                        .getJSONObject("entity");

        String razorpayPaymentId = paymentEntity.getString("id");
        String razorpayOrderId = paymentEntity.getString("order_id");

        Payment payment = paymentRepository
                .findByProviderOrderId(razorpayOrderId)
                .orElseThrow();

        // Idempotency
        if (payment.getStatus() == PaymentStatus.SUCCESS) {
            return;
        }

        payment.setStatus(PaymentStatus.SUCCESS);
        payment.setProviderPaymentId(razorpayPaymentId);
        payment.setCompletedAt(Instant.now());
        paymentRepository.save(payment);

        // OUTBOX EVENT
        OutboxEvent eventOutbox = OutboxEvent.builder()
                .id(UUID.randomUUID())
                .topic("payment-success")
                .aggregateId(payment.getBookingId().toString())
                .payload("""
                {
                  "bookingId": "%s",
                  "paymentId": "%s",
                  "amount": "%s"
                }
                """.formatted(
                        payment.getBookingId(),
                        payment.getId(),
                        payment.getAmount()
                ))
                .status(OutboxStatus.PENDING)
                .createdAt(Instant.now())
                .build();

        outboxEventRepository.save(eventOutbox);
    }

    private void handlePaymentFailed(JSONObject event) {

        JSONObject paymentEntity =
                event.getJSONObject("payload")
                        .getJSONObject("payment")
                        .getJSONObject("entity");

        String razorpayPaymentId = paymentEntity.getString("id");
        String razorpayOrderId = paymentEntity.getString("order_id");

        String errorReason = paymentEntity.optString("error_reason", "UNKNOWN");
        String errorDescription = paymentEntity.optString("error_description", "Payment failed");

        Payment payment = paymentRepository
                .findByProviderOrderId(razorpayOrderId)
                .orElseThrow();

        // Idempotency
        if (payment.getStatus() == PaymentStatus.FAILED) {
            return;
        }

        payment.setStatus(PaymentStatus.FAILED);
        payment.setProviderPaymentId(razorpayPaymentId);
        payment.setCompletedAt(Instant.now());
        paymentRepository.save(payment);

        // OUTBOX EVENT → booking-service will cancel booking
        OutboxEvent outboxEvent = OutboxEvent.builder()
                .id(UUID.randomUUID())
                .topic("payment-failed")
                .aggregateId(payment.getBookingId().toString())
                .payload("""
                {
                  "bookingId": "%s",
                  "paymentId": "%s",
                  "reason": "%s",
                  "description": "%s"
                }
                """.formatted(
                        payment.getBookingId(),
                        payment.getId(),
                        errorReason,
                        errorDescription
                ))
                .status(OutboxStatus.PENDING)
                .createdAt(Instant.now())
                .build();

        outboxEventRepository.save(outboxEvent);
    }

    private void handleRefund(JSONObject event) {

        JSONObject refundEntity =
                event.getJSONObject("payload")
                        .getJSONObject("refund")
                        .getJSONObject("entity");

        String refundId  = refundEntity.getString("id");
        String paymentId = refundEntity.getString("payment_id");
        String status    = refundEntity.getString("status"); // created | processed | failed

        // Find payment (prefer refundId, fallback to paymentId)
        Payment payment = paymentRepository.findByProviderRefundId(refundId).orElseGet(() ->
                        paymentRepository.findByProviderPaymentId(paymentId)
                                .orElseThrow(() ->
                                        new IllegalStateException(
                                                "Payment not found for refundId=" + refundId +
                                                        ", paymentId=" + paymentId
                                        )
                                )
                );

        // Idempotency (Razorpay retries webhooks)
        if (refundId.equals(payment.getProviderRefundId()) &&
                payment.getStatus() == PaymentStatus.REFUNDED &&
                "processed".equals(status)) {
            return;
        }

        // Persist refundId if first time
        if (payment.getProviderRefundId() == null) {
            payment.setProviderRefundId(refundId);
        }

        // 4Status mapping
        switch (status) {
            case "processed" -> payment.setStatus(PaymentStatus.REFUNDED);
            case "failed"    -> payment.setStatus(PaymentStatus.REFUND_FAILED);
            default          -> payment.setStatus(PaymentStatus.REFUND_PENDING);
        }

        paymentRepository.save(payment);

        // Outbox events
        if ("processed".equals(status)) {

            OutboxEvent refundedEvent = OutboxEvent.builder()
                    .id(UUID.randomUUID())
                    .topic("payment-refunded")
                    .aggregateId(payment.getBookingId().toString())
                    .payload("""
                {
                  "bookingId": "%s",
                  "paymentId": "%s",
                  "refundId": "%s"
                }
                """.formatted(
                            payment.getBookingId(),
                            payment.getId(),
                            refundId
                    ))
                    .status(OutboxStatus.PENDING)
                    .createdAt(Instant.now())
                    .build();

            outboxEventRepository.save(refundedEvent);
        }

        if ("failed".equals(status)) {

            OutboxEvent refundFailedEvent = OutboxEvent.builder()
                    .id(UUID.randomUUID())
                    .topic("payment-refund-failed")
                    .aggregateId(payment.getBookingId().toString())
                    .payload("""
                {
                  "bookingId": "%s",
                  "paymentId": "%s",
                  "refundId": "%s"
                }
                """.formatted(
                            payment.getBookingId(),
                            payment.getId(),
                            refundId
                    ))
                    .status(OutboxStatus.PENDING)
                    .createdAt(Instant.now())
                    .build();

            outboxEventRepository.save(refundFailedEvent);
        }
    }


}
