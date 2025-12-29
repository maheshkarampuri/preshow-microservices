package com.preshow.show.dto;

import lombok.*;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShowSlot {
    private UUID showId;
    private String time;
    private String tag;
    private boolean available;
}