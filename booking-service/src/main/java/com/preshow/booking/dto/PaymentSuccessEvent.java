package com.preshow.booking.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record PaymentSuccessEvent(
        UUID bookingId,
        UUID paymentId,
        BigDecimal amount
) {}
