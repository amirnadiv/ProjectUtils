package com.amirnadiv.project.utils.common.exception;

public class ClassInstantiationException extends RuntimeException {

    private static final long serialVersionUID = -2178789451989790744L;

    private Class<?> clazz;

    public ClassInstantiationException() {
        super();
    }

    public ClassInstantiationException(String message) {
        super(message);
    }

    public ClassInstantiationException(Throwable cause) {
        super(cause);
    }

    public ClassInstantiationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClassInstantiationException(Class<?> clazz, String message) {
        super(message);
        this.clazz = clazz;
    }

    public ClassInstantiationException(Class<?> clazz, String message, Throwable cause) {
        super(message, cause);
        this.clazz = clazz;
    }

    public Class<?> getClazz() {
        return clazz;
    }
}
