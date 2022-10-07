package com.ayham.testapp.service.Exception;

public class UserDoesNotExistException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public UserDoesNotExistException() {
        super("user does not exist");
    }
}
