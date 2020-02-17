package com.amirnadiv.project.utils.common.able;

public interface Receiver<T> {

    void messageReceived(T msg);
}
