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

    @Transactional
    public void process(String payload) {

        JSONObject event = new JSONObject(payload);
        String eventType = event.getString("event");

        if ("payment.captured".equals(eventType)) {
            handlePaymentCaptured(event);
        }

        if ("payment.failed".equals(eventType)) {
            handlePaymentFailed(event);
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

}
