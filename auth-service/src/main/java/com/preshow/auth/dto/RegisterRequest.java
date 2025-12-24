package com.preshow.auth.dto;

public record RegisterRequest(
        String email,
        String password,
        String fullName,
        String mobile
) {}
