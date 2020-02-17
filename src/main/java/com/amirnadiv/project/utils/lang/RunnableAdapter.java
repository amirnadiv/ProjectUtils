package com.amirnadiv.project.utils.common.lang;

import java.util.concurrent.Callable;

import com.amirnadiv.project.utils.common.able.Adaptable;

public class RunnableAdapter<T> implements Adaptable<Runnable, Callable<T>>, Callable<T> {

    final Runnable task;
    final T result;

    public RunnableAdapter(Runnable task, T result) {
        this.task = task;
        this.result = result;
    }

    public T call() {
        task.run();
        return result;
    }

    @Override
    public Callable<T> forNew(final Runnable old) {
        return new RunnableAdapter<T>(old, result);
    }

}
