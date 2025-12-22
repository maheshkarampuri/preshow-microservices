package com.preshow.auth.dto;

public record ErrorResponse(
        String message,
        int status,
        long timestamp
) {}
