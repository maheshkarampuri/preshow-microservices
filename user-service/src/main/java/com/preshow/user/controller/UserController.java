package com.preshow.user.controller;

import com.preshow.user.dto.CreateUserRequest;
import com.preshow.user.dto.UserResponse;
import com.preshow.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // INTERNAL API (auth-service → user-service)
    @PostMapping("/internal")
    public ResponseEntity<Void> createUser(@RequestBody CreateUserRequest request) {
        System.out.println("req : "+request);
        userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // PUBLIC API (via API Gateway)
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMe(@AuthenticationPrincipal Jwt jwt) {

        String userId = jwt.getSubject();
        return ResponseEntity.ok(userService.getCurrentUser(userId));
    }
}
