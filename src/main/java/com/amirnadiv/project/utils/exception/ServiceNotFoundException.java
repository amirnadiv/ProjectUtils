package com.amirnadiv.project.utils.common.exception;

public class ServiceNotFoundException extends ClassNotFoundException {
    private static final long serialVersionUID = -6525751787646334079L;

    public ServiceNotFoundException() {
        super();
    }

    public ServiceNotFoundException(String message) {
        super(message);
    }

    public ServiceNotFoundException(Throwable cause) {
        super(null, cause);

    }

    public ServiceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
