package com.amirnadiv.project.utils.common.lang;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class FutureResponse<V> extends FutureTask<V> {

    public FutureResponse() {
        super(new Callable<V>() {
            public V call() throws Exception {
                return null;
            }
        });
    }

    // public void bind(Object key) {
    //
    // }

    public void set(V v) {
        super.set(v);
    }
}
