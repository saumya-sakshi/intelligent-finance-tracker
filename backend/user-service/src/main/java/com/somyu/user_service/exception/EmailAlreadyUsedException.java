package com.somyu.user_service.exception;

/**
 * Thrown when attempting to register with an email that already exists.
 */
public class EmailAlreadyUsedException extends RuntimeException {
    public EmailAlreadyUsedException(String message) {
        super(message);
    }
}
