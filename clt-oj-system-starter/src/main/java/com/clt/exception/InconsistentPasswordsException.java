package com.clt.exception;

public class InconsistentPasswordsException extends RuntimeException {
    public InconsistentPasswordsException(String message) {
        super(message);
    }
}
