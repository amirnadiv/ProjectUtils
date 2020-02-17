package com.amirnadiv.project.utils.common.concurrent;

import java.util.Random;

public class Backoff {
    private final int minDelay, maxDelay;
    private int limit; // wait between limit and 2*limit
    private final Random random; // add randomness to wait

    public Backoff(int min, int max) {
        if (max < min) {
            throw new IllegalArgumentException("max must be greater than min");
        }
        minDelay = min;
        maxDelay = min;
        limit = minDelay;
        random = new Random();
    }

    public void backoff() throws InterruptedException {
        int delay = random.nextInt(limit);
        if (limit < maxDelay) { // double limit if less than max
            limit = 2 * limit;
        }
        Thread.sleep(delay);
    }
}
