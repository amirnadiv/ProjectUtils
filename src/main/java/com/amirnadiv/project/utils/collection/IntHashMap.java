package com.amirnadiv.project.utils.common.collection;

import com.amirnadiv.project.utils.common.Emptys;

public class IntHashMap<T> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;

    private static final int MAXIMUM_CAPACITY = 1 << 30;

    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    // 私有变量
    private Entry<T>[] table;
    private int count;
    private int threshold;
    private float loadFactor;

    public IntHashMap() {
        this(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    public IntHashMap(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    @SuppressWarnings("unchecked")
    public IntHashMap(int initialCapacity, float loadFactor) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("Illegal Capacity: " + initialCapacity);
        }

        if (initialCapacity > MAXIMUM_CAPACITY) {
            initialCapacity = MAXIMUM_CAPACITY;
        }

        if (loadFactor <= 0) {
            throw new IllegalArgumentException("Illegal Load: " + loadFactor);
        }

        // 确保初始容量为2的整数次幂.
        int capacity = 1;

        while (capacity < initialCapacity) {
            capacity <<= 1;
        }

        this.loadFactor = loadFactor;
        table = new Entry[capacity];
        threshold = (int) (capacity * loadFactor);
    }

    public int size() {
        return count;
    }

    public boolean isEmpty() {
        return count == 0;
    }

    public boolean containsKey(int key) {
        Entry<T>[] tab = table;
        int hash = key;
        int index = (hash & 0x7FFFFFFF) % tab.length;

        for (Entry<T> e = tab[index]; e != null; e = e.next) {
            if (e.hash == hash) {
                return true;
            }
        }

        return false;
    }

    public boolean containsValue(Object value) {
        Entry<T>[] tab = table;

        boolean valueIsNull = value == null;

        for (int i = tab.length; i-- > 0;) {
            for (Entry<T> e = tab[i]; e != null; e = e.next) {
                if (valueIsNull ? e.value == null : value.equals(e.value)) {
                    return true;
                }
            }
        }

        return false;
    }

    public T get(int key) {
        Entry<T>[] tab = table;
        int hash = key;
        int index = (hash & 0x7FFFFFFF) % tab.length;

        for (Entry<T> e = tab[index]; e != null; e = e.next) {
            if (e.hash == hash) {
                return e.value;
            }
        }

        return null;
    }

    public T put(int key, T value) {
        // Makes sure the key is not already in the hashtable.
        Entry<T>[] tab = table;
        int hash = key;
        int index = (hash & 0x7FFFFFFF) % tab.length;

        for (Entry<T> e = tab[index]; e != null; e = e.next) {
            if (e.hash == hash) {
                T old = e.value;

                e.value = value;
                return old;
            }
        }

        if (count >= threshold) {
            rehash();

            tab = table;
            index = (hash & 0x7FFFFFFF) % tab.length;
        }

        // Creates the new entry.
        Entry<T> e = new Entry<T>(hash, key, value, tab[index]);

        tab[index] = e;
        count++;
        return null;
    }

    public T remove(int key) {
        Entry<T>[] tab = table;
        int hash = key;
        int index = (hash & 0x7FFFFFFF) % tab.length;

        for (Entry<T> e = tab[index], prev = null; e != null; prev = e, e = e.next) {
            if (e.hash == hash) {
                if (prev != null) {
                    prev.next = e.next;
                } else {
                    tab[index] = e.next;
                }

                count--;

                T oldValue = e.value;

                e.value = null;
                return oldValue;
            }
        }

        return null;
    }

    public void clear() {
        Entry<T>[] tab = table;

        for (int index = tab.length; --index >= 0;) {
            tab[index] = null;
        }

        count = 0;
    }

    public int[] keys() {
        if (count == 0) {
            return Emptys.EMPTY_INT_ARRAY;
        }

        int[] keys = new int[count];
        int index = 0;

        for (Entry<T> element : table) {
            Entry<T> entry = element;

            while (entry != null) {
                keys[index++] = entry.key;
                entry = entry.next;
            }
        }

        return keys;
    }

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();

        buffer.append('{');

        int[] keys = keys();

        for (int i = 0; i < keys.length; i++) {
            int key = keys[i];
            T value = get(key);

            if (i > 0) {
                buffer.append(", ");
            }

            buffer.append(key).append('=').append(value == this ? "(this Map)" : value);
        }

        buffer.append('}');

        return buffer.toString();
    }

    protected void rehash() {
        int oldCapacity = table.length;
        Entry<T>[] oldMap = table;

        int newCapacity = oldCapacity * 2;

        @SuppressWarnings("unchecked")
        Entry<T>[] newMap = new Entry[newCapacity];

        threshold = (int) (newCapacity * loadFactor);
        table = newMap;

        for (int i = oldCapacity; i-- > 0;) {
            for (Entry<T> old = oldMap[i]; old != null;) {
                Entry<T> e = old;

                old = old.next;

                int index = (e.hash & 0x7FFFFFFF) % newCapacity;

                e.next = newMap[index];
                newMap[index] = e;
            }
        }
    }

    protected int getCapacity() {
        return table.length;
    }

    protected int getThreshold() {
        return threshold;
    }

    protected static class Entry<T> {
        protected int hash;
        protected int key;
        protected T value;
        protected Entry<T> next;

        protected Entry(int hash, int key, T value, Entry<T> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
