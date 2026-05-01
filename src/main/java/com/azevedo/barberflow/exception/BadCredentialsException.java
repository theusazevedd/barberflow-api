package com.azevedo.barberflow.exception;

public class BadCredentialsException extends RuntimeException {

    public BadCredentialsException(String mensagem) {
        super(mensagem);
    }

    public BadCredentialsException(String mensagem, Throwable throwable) {
        super(mensagem, throwable);
    }


}
