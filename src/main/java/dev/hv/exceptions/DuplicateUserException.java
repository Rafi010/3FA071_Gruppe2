package dev.hv.exceptions;

public class DuplicateUserException extends RuntimeException {
    public DuplicateUserException(String message, Throwable cause) {
        super(message, cause);
    }
}