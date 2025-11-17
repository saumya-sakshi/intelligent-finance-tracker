package com.somyu.user_service.service;

import com.somyu.user_service.dto.RegisterRequest;
import com.somyu.user_service.dto.UserResponse;

import java.util.Optional;
import java.util.UUID;

public interface UserService {

    /**
     * Register a new user.
     *
     * @param request registration info (fullName, email, password)
     * @return created user details (no password)
     * @throws com.somyu.user_service.exception.EmailAlreadyUsedException if email is already registered
     */
    UserResponse register(RegisterRequest request);

    /**
     * Find user by email (returns Optional of entity or empty).
     *
     * @param email email to search
     * @return optional user id (for internal usage) - keep as Optional to allow callers to decide
     */
    Optional<UUID> findUserIdByEmail(String email);

    UserResponse getUserById(UUID id);
}
