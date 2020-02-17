package com.amirnadiv.project.utils.common.exception;

public class IllegalPathException extends IllegalArgumentException {

    private static final long serialVersionUID = 1434004725746713564L;

    public IllegalPathException() {
        super();
    }

    public IllegalPathException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalPathException(String s) {
        super(s);
    }

    public IllegalPathException(Throwable cause) {
        super(cause);
    }

}
