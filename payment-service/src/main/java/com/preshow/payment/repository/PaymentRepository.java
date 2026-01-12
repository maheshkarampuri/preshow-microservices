package com.preshow.payment.repository;

import com.preshow.payment.enums.PaymentStatus;
import com.preshow.payment.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;


import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {

    Optional<Payment> findByBookingId(UUID bookingId);

    Optional<Payment> findByProviderOrderId(String providerOrderId);

    List<Payment> findByStatusAndCreatedAtBefore(PaymentStatus status,Instant cutoffTime);

    Optional<Payment> findByProviderPaymentId(String paymentId);

    Optional<Payment> findByProviderRefundId(String refundId);
}
