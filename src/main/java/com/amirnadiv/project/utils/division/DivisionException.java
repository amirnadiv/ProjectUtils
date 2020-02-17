package com.amirnadiv.project.utils.common.division;

public class DivisionException extends Exception {

    private static final long serialVersionUID = -693227965893118092L;

    public DivisionException() {
        super();
    }

    public DivisionException(String msg) {
        super(msg);
    }

    public DivisionException(Throwable cause) {
        super(cause);
    }

    public DivisionException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
