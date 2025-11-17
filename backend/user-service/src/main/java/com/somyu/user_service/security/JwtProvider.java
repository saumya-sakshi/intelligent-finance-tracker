package com.somyu.user_service.security;

import com.somyu.user_service.config.JwtProperties;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Utility for creating and validating JWT tokens.
 * Uses io.jsonwebtoken (JJWT) 0.11.x APIs.
 */
@Component
public class JwtProvider {

    private final JwtProperties jwtProperties;
    private final Key signingKey;

    public JwtProvider(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        // Use secret bytes as signing key; ensure the secret is sufficiently long for HS256
        byte[] keyBytes = jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8);
        this.signingKey = Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Generate a JWT token for the given user details.
     *
     * @param userDetails Spring Security UserDetails (username is email)
     * @param roles set of role strings (e.g. ROLE_USER)
     * @return signed JWT token string
     */
    public String generateToken(UserDetails userDetails, Set<String> roles) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        Date expiry = new Date(nowMillis + jwtProperties.getExpirationMs());

        Map<String, Object> claims = new HashMap<>();
        // add roles claim as list of strings
        if (roles != null && !roles.isEmpty()) {
            claims.put("roles", roles.stream().collect(Collectors.toList()));
        }

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Extract username (email) from token.
     */
    public String getUsernameFromToken(String token) {
        Claims claims = parseClaims(token);
        return claims.getSubject();
    }

    /**
     * Extract roles from token as a Set<String>
     */
    @SuppressWarnings("unchecked")
    public Set<String> getRolesFromToken(String token) {
        Claims claims = parseClaims(token);
        Object rolesObj = claims.get("roles");
        if (rolesObj instanceof Collection) {
            Collection<?> coll = (Collection<?>) rolesObj;
            return coll.stream().map(String::valueOf).collect(Collectors.toSet());
        }
        return Collections.emptySet();
    }

    /**
     * Validate token (signature + expiration).
     */
    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (ExpiredJwtException ex) {
            // token expired
            return false;
        } catch (JwtException | IllegalArgumentException ex) {
            // invalid token
            return false;
        }
    }

    /**
     * Internal helper to parse claims and validate signature.
     */
    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
