package com.preshow.booking.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
public class BookingRequest {
    private UUID showId;
    private List<UUID> seatIds;
    Map<UUID, SeatSnapshot> seatSnapshotMap;
}
