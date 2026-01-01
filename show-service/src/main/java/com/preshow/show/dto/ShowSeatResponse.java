package com.preshow.show.dto;

import com.preshow.show.enums.SeatCategory;
import com.preshow.show.enums.SeatStatus;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter @Builder
public class ShowSeatResponse {
    private UUID id;
    private String seatNumber;
    private SeatCategory category;
    private BigDecimal price;
    private SeatStatus status;
}
