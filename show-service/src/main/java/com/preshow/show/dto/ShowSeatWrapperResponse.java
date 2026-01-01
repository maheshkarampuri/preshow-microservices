package com.preshow.show.dto;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShowSeatWrapperResponse {
    private UUID showId;
    private List<ShowSeatResponse> seats;
}
