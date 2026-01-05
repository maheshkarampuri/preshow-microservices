package com.preshow.showquery.model;

import java.util.List;
import java.util.UUID;

public record TheaterInfo(
        String theaterId,
        String theaterName,
        List<ShowInfo> shows
) {}