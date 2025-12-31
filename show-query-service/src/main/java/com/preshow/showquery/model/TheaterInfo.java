package com.preshow.showquery.model;

import java.util.List;
import java.util.UUID;

public record TheaterInfo(
        UUID theaterId,
        String theaterName,
        List<ShowInfo> shows
) {}