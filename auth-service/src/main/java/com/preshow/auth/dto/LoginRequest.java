package com.preshow.auth.dto;

public record LoginRequest(
        String email,
        String password
) {}
