package com.clt.exception;

public class FileStorageSpaceOutOfRangeException extends RuntimeException {
    public FileStorageSpaceOutOfRangeException(String message) {
        super(message);
    }
}
