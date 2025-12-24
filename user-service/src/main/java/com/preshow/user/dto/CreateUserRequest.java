package com.preshow.user.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequest {
    private String id;       // UUID from auth-service
    private String email;
    private String role;
    private String fullName;
    private String mobile;
}
