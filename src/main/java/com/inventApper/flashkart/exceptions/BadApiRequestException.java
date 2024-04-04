package com.inventApper.flashkart.exceptions;

public class BadApiRequestException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public BadApiRequestException(String message) {
        super(message);
    }

    public BadApiRequestException() {
        super("Bad Request !!");
    }
}
