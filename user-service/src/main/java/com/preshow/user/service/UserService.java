package com.preshow.user.service;

import com.preshow.user.dto.CreateUserRequest;
import com.preshow.user.dto.UserResponse;
import com.preshow.user.model.UserProfile;
import com.preshow.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;

    // Create user (called from auth-service)
    public void createUser(CreateUserRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("User already exists");
        }

        UserProfile user = UserProfile.builder()
                .id(request.getId())
                .email(request.getEmail())
                .role(request.getRole())
                .fullName(request.getFullName())
                .mobile(request.getMobile())
                .active(true)
                .build();

        userRepository.save(user);
    }

    // Get current logged-in user
    @Transactional(readOnly = true)
    public UserResponse getCurrentUser(String userId) {

        UserProfile user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getFullName(),
                user.getMobile(),
                user.getRole(),
                user.isActive()
        );
    }
}
