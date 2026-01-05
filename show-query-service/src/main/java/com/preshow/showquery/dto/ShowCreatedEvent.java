package com.preshow.showquery.dto;

import java.time.LocalDate;
import java.util.UUID;

public record ShowCreatedEvent(
        String showId,
        String theaterId,
        String movieId,
        LocalDate showDate,
        String showTimeText
) {}