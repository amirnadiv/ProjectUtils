package com.amirnadiv.project.utils.common.exception;

public class UncheckedException extends RuntimeException {

    private static final long serialVersionUID = 5325929756409272324L;

    public UncheckedException() {
        super();
    }

    public UncheckedException(String message) {
        super(message);
    }

    public UncheckedException(Throwable cause) {
        super(cause);
    }

    public UncheckedException(String message, Throwable cause) {
        super(message, cause);
    }

}
