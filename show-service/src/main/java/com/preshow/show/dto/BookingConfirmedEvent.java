package com.preshow.show.dto;

import java.util.List;
import java.util.UUID;

public record BookingConfirmedEvent(
        UUID showId,
        List<UUID> seatIds,
        UUID bookingId
) {
}
