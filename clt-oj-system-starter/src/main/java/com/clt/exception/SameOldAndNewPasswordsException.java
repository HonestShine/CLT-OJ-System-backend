package com.clt.exception;

public class SameOldAndNewPasswordsException extends RuntimeException {
    public SameOldAndNewPasswordsException(String message) {
        super(message);
    }
}
