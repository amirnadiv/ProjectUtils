package com.amirnadiv.project.utils.common.lang;

import java.util.concurrent.Callable;

import com.amirnadiv.project.utils.common.able.Adaptable;
import com.amirnadiv.project.utils.common.logger.CachedLogger;

public class CallableAdapter<T> extends CachedLogger implements Adaptable<Callable<T>, Runnable>, Runnable {

    final Callable<T> task;

    public CallableAdapter(Callable<T> task) {
        this.task = task;
    }

    @Override
    public void run() {
        try {
            task.call();
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    @Override
    public Runnable forNew(final Callable<T> old) {
        return new Runnable() {

            @Override
            public void run() {
                try {
                    old.call();
                } catch (Exception e) {
                    logger.error("", e);
                }

            }
        };
    }

}
