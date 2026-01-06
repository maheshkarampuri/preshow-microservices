package com.preshow.showquery.dto;

import java.util.List;
import java.util.UUID;

public record BookingConfirmedEvent(
        String showId,
        List<String> seatIds
) {
}
