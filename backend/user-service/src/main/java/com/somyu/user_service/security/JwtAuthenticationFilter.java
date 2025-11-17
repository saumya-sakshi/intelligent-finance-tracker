package com.somyu.user_service.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Filter that extracts JWT from Authorization header, validates it and sets Authentication
 * in the SecurityContext if valid.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTH_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtProvider jwtProvider;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtProvider jwtProvider,
                                   UserDetailsService userDetailsService) {
        this.jwtProvider = jwtProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        try {
            String token = resolveToken(request);
            if (StringUtils.hasText(token) && jwtProvider.validateToken(token)) {
                String username = jwtProvider.getUsernameFromToken(token);
                if (username != null) {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                    // Optionally, you can derive authorities from token claims instead of loading userDetails roles
                    Set<SimpleGrantedAuthority> authorities = jwtProvider.getRolesFromToken(token).stream()
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toSet());

                    // If token contains roles, use them; otherwise fallback to userDetails.getAuthorities()
                    var auth = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            authorities.isEmpty() ? userDetails.getAuthorities() : authorities
                    );

                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
        } catch (Exception ex) {
            // In case of any exception during JWT processing, clear context and continue.
            SecurityContextHolder.clearContext();
            // Optionally log the exception (not added here to keep code minimal)
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Read the Authorization header and return token without Bearer prefix.
     */
    private String resolveToken(HttpServletRequest request) {
        String bearer = request.getHeader(AUTH_HEADER);
        if (StringUtils.hasText(bearer) && bearer.startsWith(BEARER_PREFIX)) {
            return bearer.substring(BEARER_PREFIX.length());
        }
        return null;
    }
}
