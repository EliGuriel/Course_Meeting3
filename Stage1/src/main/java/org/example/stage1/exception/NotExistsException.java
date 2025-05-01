package org.example.stage1.exception;

public class NotExistsException extends RuntimeException {
    public NotExistsException(String message) {
        super(message);
    }
}