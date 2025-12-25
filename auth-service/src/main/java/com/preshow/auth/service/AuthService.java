package com.preshow.auth.service;

import com.preshow.auth.client.UserServiceClient;
import com.preshow.auth.config.JwtUtil;
import com.preshow.auth.dto.*;
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
    private final UserServiceClient userServiceClient;

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

        try {
            userServiceClient.createUser(
                    new CreateUserRequest(
                            saved.getId(),
                            saved.getEmail(),
                            saved.getRole(),
                            request.fullName(),
                            request.mobile()
                    )
            );
        }
        catch (Exception e){
            repository.deleteById(saved.getId());
            throw new AuthException("User registration failed and rolled back", HttpStatus.BAD_REQUEST);
        }

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

        String accessToken = jwtUtil.generateToken(user);
        String refreshToken = jwtUtil.generateRefreshToken(user);

        return new LoginResponse(
                accessToken,
                refreshToken,
                user.getId(),
                user.getRole()
        );
    }


    public String refreshAccessToken(String refreshToken) {

        if (jwtUtil.isTokenExpired(refreshToken)) {
            throw new AuthException("Refresh token expired", HttpStatus.UNAUTHORIZED);
        }

        String userId = jwtUtil.extractUserId(refreshToken);

        AuthUser user = repository.findById(userId)
                .orElseThrow(() ->
                        new AuthException("User not found", HttpStatus.NOT_FOUND)
                );

        return jwtUtil.generateToken(user);
    }

}