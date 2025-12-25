package com.preshow.auth.controller;

import com.preshow.auth.config.JwtProperties;
import com.preshow.auth.config.JwtUtil;
import com.preshow.auth.dto.LoginRequest;
import com.preshow.auth.dto.LoginResponse;
import com.preshow.auth.dto.RegisterRequest;
import com.preshow.auth.dto.RegisterResponse;
import com.preshow.auth.model.AuthUser;
import com.preshow.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;
    // Health / ping endpoint
    @GetMapping("/ping")
    public String ping() {
        return "Auth service is running";
    }

    // Register new user
    @PostMapping("/register")
    public RegisterResponse register(@Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    // Login and get JWT
    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");
        String newAccessToken = authService.refreshAccessToken(refreshToken);
        return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
    }

    // Protected endpoint (JWT test)
    @GetMapping("/secure")
    public String secure() {
        return "JWT is valid and authenticated";
    }
}
