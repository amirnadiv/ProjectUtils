package com.amirnadiv.project.utils.common.exception;

public class UnexpectedFailureException extends RuntimeException {
    private static final long serialVersionUID = -8227335536836081391L;

    public UnexpectedFailureException() {
        super();
    }

    public UnexpectedFailureException(String message) {
        super(message);
    }

    public UnexpectedFailureException(Throwable cause) {
        super(cause);
    }

    public UnexpectedFailureException(String message, Throwable cause) {
        super(message, cause);
    }
}
