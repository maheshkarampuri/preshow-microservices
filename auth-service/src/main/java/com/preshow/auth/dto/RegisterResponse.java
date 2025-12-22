package com.preshow.auth.dto;

public record RegisterResponse(
        String userId,
        String email,
        String role
) {}
