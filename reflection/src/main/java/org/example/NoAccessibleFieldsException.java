package org.example;

public class NoAccessibleFieldsException extends RuntimeException {
    public NoAccessibleFieldsException(String message) {
        throw new RuntimeException(message);
    }
}
