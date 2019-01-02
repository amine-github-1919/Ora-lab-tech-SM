package com.oracle.techtrial.notebook.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Component
@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Error handleGenericException(Exception e) {
        LOG.error(e.getMessage(), e);
        return new Error(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Error handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        StringBuilder errorMessage = new StringBuilder();
        LOG.error(e.getMessage(), e);
        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            errorMessage.append(fieldError.getDefaultMessage());
        }
        return new Error(errorMessage.toString());
    }

    @ExceptionHandler(UnprocessableCommandException.class)
    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
    @ResponseBody
    public Error handleUnprocessableCommandException(final UnprocessableCommandException e) {
        LOG.error(e.getMessage(), e);
        return new Error(e.getMessage());
    }

    @ExceptionHandler(UnsupportedInterpreterException.class)
    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
    @ResponseBody
    public Error handleUnsupportedInterpreterException(final UnsupportedInterpreterException e) {
        LOG.error(e.getMessage(), e);
        return new Error(e.getMessage());
    }

    @ExceptionHandler(CodeExecutionException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Error handleCodeExecutionException(final CodeExecutionException e) {
        LOG.error(e.getMessage(), e);
        return new Error(e.getMessage());
    }

    @ExceptionHandler(SessionNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ResponseBody
    public Error handleSessionNotFoundException(final SessionNotFoundException e) {
        LOG.error(e.getMessage(), e);
        return new Error(e.getMessage());
    }


    private static class Error {

        private final String message;

        public Error(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

}
