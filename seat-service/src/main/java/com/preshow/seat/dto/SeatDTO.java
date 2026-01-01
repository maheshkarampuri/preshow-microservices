package com.preshow.seat.dto;

import com.preshow.seat.enums.SeatCategory;
import lombok.Getter;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SeatDTO {
    private UUID id;
    private String seatNumber;
    private SeatCategory category;
}
