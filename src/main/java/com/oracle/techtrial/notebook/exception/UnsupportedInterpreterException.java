package com.oracle.techtrial.notebook.exception;

public class UnsupportedInterpreterException extends RuntimeException {


    private final String interpreter;

    public UnsupportedInterpreterException(final String interpreter) {
        this.interpreter = interpreter;
    }


    @Override
    public String getMessage() {
        return "Unsupported interpreter [" + interpreter + "]";
    }
}