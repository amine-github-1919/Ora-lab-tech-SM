package com.oracle.techtrial.notebook.exception;

public class CodeExecutionException extends RuntimeException {

    private final String interpreterName;
    private final String code;
    private final String message;

    public CodeExecutionException(String interperterName, String code, String message, Throwable originalException) {
        super(originalException);
        this.interpreterName = interperterName;
        this.code = code;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return "[" + interpreterName + "] Failed to execute:[" + code + "] with message: [" + message + "]";
    }

}
