package com.preshow.showquery.dto;

import com.preshow.showquery.enums.SeatCategory;
import com.preshow.showquery.enums.SeatStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter @Builder
public class ShowSeatResponse {
    private String id;
    private String seatNumber;
    private SeatCategory category;
    private BigDecimal price;
    private SeatStatus status;
}
