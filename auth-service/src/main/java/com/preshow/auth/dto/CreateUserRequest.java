package com.preshow.auth.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequest {
    private String id;
    private String email;
    private String role;
    private String fullName;
    private String mobile;
}
