package com.somyu.user_service.dto;

import lombok.*;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {

    private String accessToken;
    private String tokenType; // e.g. "Bearer"
    private Long expiresInMillis;

    // user info
    private UUID userId;
    private String email;
    private Set<String> roles;
}