package com.ayham.testapp.service.Exception;

public class MissingRequiredDataException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public MissingRequiredDataException() {
        super("missing required data");
    }
}
