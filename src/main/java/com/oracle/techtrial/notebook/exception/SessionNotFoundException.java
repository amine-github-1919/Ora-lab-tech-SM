package com.oracle.techtrial.notebook.exception;

public class SessionNotFoundException extends RuntimeException {

    private final String sessionId;

    public SessionNotFoundException(final String sessionId) {
        this.sessionId = sessionId;
    }


    @Override
    public String getMessage() {
        return "Session Id [" + sessionId + "] not found";
    }
}