package com.preshow.show.dto;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TheaterShowsResponse {
    private UUID theaterId;
    private String theaterName;
    private List<ShowSlot> shows;
}