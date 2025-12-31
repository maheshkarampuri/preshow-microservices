package com.preshow.showquery.model;

import java.util.List;
import java.util.UUID;

public record ShowInfo(
        UUID showId,
        String time,
        String status
) {}