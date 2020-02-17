package com.amirnadiv.project.utils.common.collection;

import java.util.Map;

import com.amirnadiv.project.utils.common.ObjectUtil;

public class DefaultMapEntry<K, V> implements Map.Entry<K, V> {
    private final K key;
    private V value;

    public DefaultMapEntry(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    public V setValue(V value) {
        V oldValue = this.value;

        this.value = value;

        return oldValue;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }

        if (o == this) {
            return true;
        }

        if (!(o instanceof Map.Entry)) {
            return false;
        }

        Map.Entry<?, ?> e = (Map.Entry<?, ?>) o;

        return ObjectUtil.isEquals(key, e.getKey()) && ObjectUtil.isEquals(value, e.getValue());
    }

    @Override
    public int hashCode() {
        return (key == null ? 0 : key.hashCode()) ^ (value == null ? 0 : value.hashCode());
    }

    @Override
    public String toString() {
        return getKey() + "=" + getValue();
    }
}
