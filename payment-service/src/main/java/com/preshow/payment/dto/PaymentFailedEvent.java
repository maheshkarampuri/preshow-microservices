package com.preshow.payment.dto;

import java.util.UUID;

public record PaymentFailedEvent(
        UUID bookingId,
        UUID paymentId,
        String reason,
        String description
) {}
