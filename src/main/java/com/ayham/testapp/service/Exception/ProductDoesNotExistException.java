package com.ayham.testapp.service.Exception;

public class ProductDoesNotExistException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ProductDoesNotExistException() {
        super("product does not exist");
    }
}
