package com.preshow.auth.dto;

import java.util.Map;

public record ErrorResponse(
        String message,
        Map<String, String> errors,
        int status,
        long timestamp
) {}
