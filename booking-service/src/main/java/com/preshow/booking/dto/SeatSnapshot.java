package com.preshow.booking.dto;

import java.math.BigDecimal;

public record SeatSnapshot(String seatNumber,
                           BigDecimal price) {
}
