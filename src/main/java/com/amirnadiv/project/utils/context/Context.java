package com.amirnadiv.project.utils.common.context;

import java.util.Collection;

public interface Context<K, V> {

    V get(K key);

    Context<K, V> set(K key, V value);

    Collection<V> values();

}
