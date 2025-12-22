package com.preshow.auth.dto;

public record LoginResponse(
        String token,
        String userId,
        String role
) {}
