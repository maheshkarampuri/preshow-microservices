package com.preshow.show.dto;

import com.preshow.show.enums.SeatCategory;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@ToString
public class SeatDTO {
    private UUID id;
    private String seatNumber;
    private SeatCategory category;
}
