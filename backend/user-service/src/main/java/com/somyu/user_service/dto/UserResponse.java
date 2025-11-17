package com.somyu.user_service.dto;


import lombok.*;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    private UUID id;
    private String fullName;
    private String email;
    private Set<String> roles;
    private boolean enabled;
    private Instant createdAt;
    private Instant updatedAt;
}