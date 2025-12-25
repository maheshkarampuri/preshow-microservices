package com.preshow.auth.dto;

public record LoginResponse(
        String accessToken,
        String refreshToken,
        String userId,
        String role
) {}
