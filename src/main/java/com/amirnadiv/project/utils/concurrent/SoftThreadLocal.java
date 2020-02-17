package com.amirnadiv.project.utils.common.concurrent;

import java.lang.ref.SoftReference;

public class SoftThreadLocal<T> extends ThreadLocal<T> {

    private final ThreadLocal<SoftReference<T>> local = new ThreadLocal<SoftReference<T>>();

    @Override
    public T get() {
        SoftReference<T> ref = local.get();
        T result = null;
        if (null != ref) {
            result = ref.get();
        }
        if (null == result) {
            result = initialValue();
            ref = new SoftReference<T>(result);
            local.set(ref);
        }
        return result;
    }

    @Override
    public void set(T value) {
        if (null == value) {
            remove();
        } else {
            local.set(new SoftReference<T>(value));
        }
    }

    @Override
    public void remove() {
        local.remove();
    }

}
