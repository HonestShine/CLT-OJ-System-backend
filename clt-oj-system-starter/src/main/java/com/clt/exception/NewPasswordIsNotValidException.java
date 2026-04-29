package com.clt.exception;

public class NewPasswordIsNotValidException extends RuntimeException {
    public NewPasswordIsNotValidException(String message) {
        super(message);
    }
}
