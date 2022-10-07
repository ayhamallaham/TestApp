package com.ayham.testapp.service.Exception;

public class LikeException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public LikeException() {
        super("Can not like own product");
    }
}
