package com.preshow.auth.service;

import com.preshow.auth.config.JwtUtil;
import com.preshow.auth.dto.LoginRequest;
import com.preshow.auth.dto.LoginResponse;
import com.preshow.auth.dto.RegisterRequest;
import com.preshow.auth.dto.RegisterResponse;
import com.preshow.auth.exception.AuthException;
import com.preshow.auth.model.AuthUser;
import com.preshow.auth.repository.AuthUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {


    private final AuthUserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public RegisterResponse register(RegisterRequest request) {

        if (repository.existsByEmail(request.email())) {
            throw new AuthException(
                    "Email already registered",
                    HttpStatus.CONFLICT
            );
        }

        AuthUser user = AuthUser.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role("USER")
                .enabled(true)
                .build();

        AuthUser saved = repository.save(user);

        return new RegisterResponse(
                saved.getId(),
                saved.getEmail(),
                saved.getRole()
        );
    }

    public LoginResponse login(LoginRequest request) {

        AuthUser user = repository.findByEmail(request.email())
                .orElseThrow(() ->
                        new AuthException(
                                "Invalid email or password",
                                HttpStatus.UNAUTHORIZED
                        )
                );

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new AuthException(
                    "Invalid email or password",
                    HttpStatus.UNAUTHORIZED
            );
        }

        String token = jwtUtil.generateToken(user);

        return new LoginResponse(
                token,
                user.getId(),
                user.getRole()
        );
    }

}