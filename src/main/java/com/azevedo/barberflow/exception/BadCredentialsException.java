package com.azevedo.barberflow.exception;

public class BadCredentialsException extends RuntimeException {

    public BadCredentialsException(String message) {
        super(message);
    }

    public BadCredentialsException(String message, Throwable throwable) {
        super(message, throwable);
    }


}
