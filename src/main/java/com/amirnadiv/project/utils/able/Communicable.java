package com.amirnadiv.project.utils.common.able;

import java.util.concurrent.TimeUnit;

public interface Communicable {


    void send();

    void send(Object bean);


    void send(Object... beans);


    <T> void send(ResponseClosure<T> callback);


    <T> void send(Object object, ResponseClosure<T> callback);


    <T> void send(ResponseClosure<T> callback, Object... objects);


    <T> T sendAndWait();


    <T> T sendAndWait(Object bean);


    <T> T sendAndWait(Object... beans);


    <T> T sendAndWait(long duration, TimeUnit unit);


    <T> T sendAndWait(Object bean, long duration, TimeUnit unit);


    <T> T sendAndWait(Object[] beans, long duration, TimeUnit unit);
}
