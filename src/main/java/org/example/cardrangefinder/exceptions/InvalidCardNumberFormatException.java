package org.example.cardrangefinder.exceptions;

public class InvalidCardNumberFormatException extends RuntimeException {
    public InvalidCardNumberFormatException(String message) {
        super(message);
    }
}
