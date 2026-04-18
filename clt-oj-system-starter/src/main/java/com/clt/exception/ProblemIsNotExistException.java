package com.clt.exception;

public class ProblemIsNotExistException extends RuntimeException {
    public ProblemIsNotExistException(String message) {
        super(message);
    }
}
