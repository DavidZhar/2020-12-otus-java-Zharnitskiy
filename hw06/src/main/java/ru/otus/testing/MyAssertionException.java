package ru.otus.testing;

public class MyAssertionException extends RuntimeException {
    public MyAssertionException() {
    }

    public MyAssertionException(String message) {
        super(message);
    }

    public MyAssertionException(String message, Throwable cause) {
        super(message, cause);
    }
}
