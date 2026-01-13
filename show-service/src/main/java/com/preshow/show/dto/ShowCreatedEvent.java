package com.preshow.show.dto;

import java.time.LocalDate;
import java.util.UUID;

public record ShowCreatedEvent(
        UUID showId,
        UUID theaterId,
        String theaterName,
        UUID movieId,
        String movieName,
        LocalDate showDate,
        String showTimeText
) {}