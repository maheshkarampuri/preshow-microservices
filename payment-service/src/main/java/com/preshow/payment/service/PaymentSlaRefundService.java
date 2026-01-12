package com.preshow.payment.service;

import com.preshow.payment.enums.PaymentStatus;
import com.preshow.payment.model.Payment;
import com.preshow.payment.repository.PaymentRepository;
import com.razorpay.RazorpayClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentSlaRefundService {

    private final PaymentRepository paymentRepository;
    private final RazorpayClient razorpayClient;

    private static final Duration SLA = Duration.ofMinutes(5);

    @Scheduled(fixedDelay = 60000)
    @Transactional
    public void refundIfBookingSilentBeyondSla() {

        Instant cutoffTime = Instant.now().minus(SLA);

        List<Payment> payments =
                paymentRepository.findByStatusAndCreatedAtBefore(
                        PaymentStatus.SUCCESS,
                        cutoffTime
                );

        for (Payment payment : payments) {

            if (payment.getStatus() != PaymentStatus.SUCCESS) continue;
            if (payment.getProviderPaymentId() == null) continue;

            payment.setStatus(PaymentStatus.REFUND_PENDING);
            paymentRepository.save(payment);

            log.warn(
                    "SLA breached → refunding bookingId={}, paymentId={}",
                    payment.getBookingId(),
                    payment.getId()
            );

            try {
                System.out.println("refund started");
                razorpayClient.payments.refund(payment.getProviderPaymentId());
            } catch (Exception ex) {
                log.error("Refund trigger failed for paymentId={}", payment.getId(), ex);
            }
        }
    }

}
