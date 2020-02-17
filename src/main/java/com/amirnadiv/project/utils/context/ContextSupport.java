package com.amirnadiv.project.utils.common.context;

import java.util.Collection;
import java.util.Map;

import com.amirnadiv.project.utils.common.CollectionUtil;

public class ContextSupport<K, V> implements Context<K, V> {

    private Map<K, V> map = CollectionUtil.createHashMap();

    @Override
    public V get(K key) {
        return map.get(key);
    }

    @Override
    public Context<K, V> set(K key, V value) {
        map.put(key, value);

        return this;
    }

    @Override
    public Collection<V> values() {
        return map.values();
    }

}
