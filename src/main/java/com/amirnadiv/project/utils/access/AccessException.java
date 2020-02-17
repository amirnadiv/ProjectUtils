package com.amirnadiv.project.utils.common.access;

import java.io.IOException;

public class AccessException extends IOException {

    private static final long serialVersionUID = -4040415771373665799L;

    public AccessException() {
        super();
    }

    public AccessException(String message) {
        super(message);
    }

    public AccessException(Throwable cause) {
        super(cause);
    }

    public AccessException(String message, Throwable cause) {
        super(message, cause);
    }

}
