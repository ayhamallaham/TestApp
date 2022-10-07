package com.ayham.testapp.service.Exception;

public class InvalidTokenException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public InvalidTokenException() {
        super("Invalid token");
    }
}
