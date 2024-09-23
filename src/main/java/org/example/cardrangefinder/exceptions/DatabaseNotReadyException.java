package org.example.cardrangefinder.exceptions;

public class DatabaseNotReadyException extends RuntimeException {
    public DatabaseNotReadyException(String message) {
        super(message);
    }

    public DatabaseNotReadyException(String message, Throwable cause) {
        super(message, cause);
    }
}
