package com.preshow.showquery.dto;

import java.time.LocalDate;
import java.util.UUID;

public record ShowCreatedEvent(
        UUID showId,
        UUID theaterId,
        UUID movieId,
        LocalDate showDate,
        String showTimeText
) {}