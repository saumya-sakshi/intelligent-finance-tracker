package com.somyu.user_service.controller;

import com.somyu.user_service.config.JwtProperties;
import com.somyu.user_service.dto.AuthResponse;
import com.somyu.user_service.dto.LoginRequest;
import com.somyu.user_service.dto.RegisterRequest;
import com.somyu.user_service.dto.UserResponse;
import com.somyu.user_service.service.UserService;
import com.somyu.user_service.security.JwtProvider;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final JwtProperties jwtProperties;

    public AuthController(UserService userService,
                          AuthenticationManager authenticationManager,
                          JwtProvider jwtProvider,
                          JwtProperties jwtProperties) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
        this.jwtProperties = jwtProperties;
    }

    /**
     * REGISTER USER
     */
    @PostMapping("/user/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request) {
        UserResponse created = userService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * LOGIN
     * - Throws BadCredentialsException if credentials are invalid
     * - All exceptions are handled by GlobalExceptionHandler
     */
    @PostMapping("/auth/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        Set<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        // Generate JWT
        String token = jwtProvider.generateToken(userDetails, roles);

        // Build response
        AuthResponse response = AuthResponse.builder()
                .accessToken(token)
                .tokenType("Bearer")
                .expiresInMillis(jwtProperties.getExpirationMs())
                .email(userDetails.getUsername())
                .roles(roles)
                .build();

        userService.findUserIdByEmail(userDetails.getUsername())
                .ifPresent(response::setUserId);

        return ResponseEntity.ok(response);
    }
}
