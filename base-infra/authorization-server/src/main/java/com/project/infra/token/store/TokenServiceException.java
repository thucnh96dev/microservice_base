package com.project.infra.token.store;

public class TokenServiceException extends RuntimeException {

    public TokenServiceException(String message) {
        super(message);
    }

    public TokenServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
