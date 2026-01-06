package com.preshow.booking.dto;

import java.util.UUID;

public record PaymentFailedEvent(
        UUID bookingId,
        UUID paymentId,
        String reason,
        String description
) {}
