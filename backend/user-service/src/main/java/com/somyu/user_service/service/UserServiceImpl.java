package com.somyu.user_service.service;

import com.somyu.user_service.dto.RegisterRequest;
import com.somyu.user_service.dto.UserResponse;
import com.somyu.user_service.entity.Role;
import com.somyu.user_service.entity.User;
import com.somyu.user_service.exception.EmailAlreadyUsedException;
import com.somyu.user_service.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Register a new user with ROLE_USER by default.
     */
    @Override
    public UserResponse register(RegisterRequest request) {
        String email = request.getEmail().toLowerCase().trim();

        // Check uniqueness
        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyUsedException("Email is already registered: " + email);
        }

        // Build user entity
        User user = User.builder()
                // id will be created in @PrePersist if null
                .fullName(request.getFullName().trim())
                .email(email)
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(Set.of(Role.ROLE_USER))
                .enabled(true)
                .build();

        User saved = userRepository.save(user);

        return toUserResponse(saved);
    }

    @Override
    public java.util.Optional<UUID> findUserIdByEmail(String email) {
        return userRepository.findByEmail(email).map(User::getId);
    }

    @Override
    public UserResponse getUserById(UUID id) {
        return userRepository.findById(id).map(this::toUserResponse).orElse(null);
    }

    /* ------------------ Helpers ------------------ */

    private UserResponse toUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .roles(user.getRoles().stream().map(Enum::name).collect(java.util.stream.Collectors.toSet()))
                .enabled(user.isEnabled())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}

