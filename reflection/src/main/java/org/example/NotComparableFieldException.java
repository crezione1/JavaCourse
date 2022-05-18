package org.example;

public class NotComparableFieldException extends RuntimeException {
    public NotComparableFieldException(String message) {
        throw new RuntimeException(message);
    }
}
