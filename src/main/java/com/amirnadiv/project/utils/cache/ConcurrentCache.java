package com.amirnadiv.project.utils.common.cache;

import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import com.amirnadiv.project.utils.common.CollectionUtil;
import com.amirnadiv.project.utils.common.able.Computable;

public class ConcurrentCache<K, V> implements Computable<K, V> {

    private final ConcurrentMap<K, Future<V>> concurrentMap;

    public ConcurrentCache() {
        concurrentMap = CollectionUtil.createConcurrentMap();
    }

    public static <K, V> Computable<K, V> createComputable() {
        return new ConcurrentCache<K, V>();
    }

    // FIXME
    // public ConcurrentCache(Map<K, V> map) {
    // concurrentMap = CollectionUtil.createConcurrentMap(map);
    // }

    public V get(K key, Callable<V> callable) {
        Future<V> future = concurrentMap.get(key);
        if (future == null) {
            FutureTask<V> futureTask = new FutureTask<V>(callable);
            future = concurrentMap.putIfAbsent(key, futureTask);
            if (future == null) {
                future = futureTask;
                futureTask.run();
            }
        }
        try {
            // 此时阻塞
            return future.get();
        } catch (Exception e) {
            concurrentMap.remove(key);
            return null;
        }
    }
}
