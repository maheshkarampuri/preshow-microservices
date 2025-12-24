package com.preshow.user.dto;

import lombok.*;

@Getter
@AllArgsConstructor
public class UserResponse {
    private String id;
    private String email;
    private String fullName;
    private String mobile;
    private String role;
    private boolean active;
}
