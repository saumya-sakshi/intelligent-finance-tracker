package com.somyu.user_service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * JWT related configuration properties.
 * Values are read from application.properties (or environment).
 */
@Component
public class JwtProperties {

    @Value("${jwt.secret:change-me-super-secret}")
    private String secret;

    /** Expiration time in milliseconds (default 1 hour) */
    @Value("${jwt.expiration-ms:3600000}")
    private long expirationMs;

    public String getSecret() {
        return secret;
    }

    public long getExpirationMs() {
        return expirationMs;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public void setExpirationMs(long expirationMs) {
        this.expirationMs = expirationMs;
    }
}
