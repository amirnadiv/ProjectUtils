package com.amirnadiv.project.utils.common.concurrent;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

public class ReusedCountLatch {

    private final class Sync extends AbstractQueuedSynchronizer {

        private static final long serialVersionUID = -1906056633426816410L;

        private Sync() {
            super();
        }

        int getCount() {
            return getState();
        }

        void setCount(int count) {
            setState(count);
        }

        public int tryAcquireShared(int acquires) {
            return getState() == 0 ? 1 : -1;
        }

        public boolean tryReleaseShared(int delta) {
            // Decrement count; signal when transition to zero
            for (;;) {
                int c = getState();
                int nextc = c + delta;
                if (compareAndSetState(c, nextc)) {
                    return nextc == 0;
                }
            }
        }
    }

    private Sync sync;

    private int count;

    public ReusedCountLatch() {
        this.sync = new Sync();
    }

    public ReusedCountLatch(int count) {
        this.sync = new Sync();
        this.count = count;
        sync.releaseShared(count);
    }

    public void reset() {
        this.sync.setCount(0);
        sync.releaseShared(count);
    }

    public int getCount() {
        return sync.getCount();
    }

    public void increment() {
        sync.releaseShared(+1);
    }

    public void increment(int count) {
        sync.releaseShared(count);
    }

    public void decrement() {
        sync.releaseShared(-1);
    }

    public void decrement(int count) {
        sync.releaseShared(-count);
    }

    public void await() throws InterruptedException {
        sync.acquireSharedInterruptibly(1);
    }

    public boolean await(long timeout, TimeUnit unit) throws InterruptedException {
        return sync.tryAcquireSharedNanos(1, unit.toNanos(timeout));
    }

    public void awaitAndReset() throws InterruptedException {
        await();
        reset();
    }

    public void awaitAndReset(long timeout, TimeUnit unit) throws InterruptedException {
        await(timeout, unit);
        reset();
    }

}
