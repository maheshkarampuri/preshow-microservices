package com.preshow.showquery.model;

import java.util.List;
import java.util.UUID;

public record ShowInfo(
        String showId,
        String time,
        String status
) {}