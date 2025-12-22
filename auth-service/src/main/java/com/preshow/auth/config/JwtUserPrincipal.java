package com.preshow.auth.config;

import lombok.*;

@Getter
@AllArgsConstructor
public class JwtUserPrincipal {
    private String userId;
    private String role;
}
