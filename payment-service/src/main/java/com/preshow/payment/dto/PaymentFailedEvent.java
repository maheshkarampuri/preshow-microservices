package com.preshow.payment.dto;

import java.util.UUID;

public record PaymentFailedEvent(
        UUID bookingId,
        UUID paymentId,
        String refundId,
        String reason,
        String description
) {}
