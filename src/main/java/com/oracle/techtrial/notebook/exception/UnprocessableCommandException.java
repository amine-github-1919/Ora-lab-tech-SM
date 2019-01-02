package com.oracle.techtrial.notebook.exception;

public class UnprocessableCommandException extends RuntimeException {

    public UnprocessableCommandException(final String message) {
        super(message);
    }
}
