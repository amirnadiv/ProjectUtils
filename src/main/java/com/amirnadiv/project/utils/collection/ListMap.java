package com.amirnadiv.project.utils.common.collection;

import java.util.List;
import java.util.Map;

public interface ListMap<K, V> extends Map<K, V> {
    V get(int index);

    K getKey(int index);

    Map.Entry<K, V> removeEntry(int index);

    List<K> keyList();

    List<V> valueList();

    List<Map.Entry<K, V>> entryList();
}
