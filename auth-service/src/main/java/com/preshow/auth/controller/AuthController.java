package com.preshow.auth.controller;

import com.preshow.auth.config.JwtProperties;
import com.preshow.auth.dto.LoginRequest;
import com.preshow.auth.dto.LoginResponse;
import com.preshow.auth.dto.RegisterRequest;
import com.preshow.auth.dto.RegisterResponse;
import com.preshow.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtProperties jwtProperties;
    // Health / ping endpoint
    @GetMapping("/ping")
    public String ping() {
        return "Auth service is running";
    }

    // Register new user
    @PostMapping("/register")
    public RegisterResponse register(@RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    // Login and get JWT
    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }

    // Protected endpoint (JWT test)
    @GetMapping("/secure")
    public String secure() {
        return "JWT is valid and authenticated";
    }
}
