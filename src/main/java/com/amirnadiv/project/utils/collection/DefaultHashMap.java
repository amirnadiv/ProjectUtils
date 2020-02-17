package com.amirnadiv.project.utils.common.collection;

import java.io.IOException;
import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

public class DefaultHashMap<K, V> extends AbstractMap<K, V> implements Map<K, V>, Cloneable, Serializable {

    private static final int DEFAULT_INITIAL_CAPACITY = 16;

    private static final int MAXIMUM_CAPACITY = 1 << 30;

    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    protected transient Entry<K, V>[] table;

    protected transient int size;

    protected int threshold;

    protected final float loadFactor;

    protected transient volatile int modCount;

    private transient Set<K> keySet = null;

    private transient Set<Map.Entry<K, V>> entrySet = null;

    private transient Collection<V> values = null;

    public DefaultHashMap() {
        this(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    public DefaultHashMap(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    @SuppressWarnings("unchecked")
    public DefaultHashMap(int initialCapacity, float loadFactor) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("Illegal initial capacity: " + initialCapacity);
        }

        if (initialCapacity > MAXIMUM_CAPACITY) {
            initialCapacity = MAXIMUM_CAPACITY;
        }

        if (loadFactor <= 0 || Float.isNaN(loadFactor)) {
            throw new IllegalArgumentException("Illegal load factor: " + loadFactor);
        }

        // 确保初始容量为2的整数次幂.
        int capacity = 1;

        while (capacity < initialCapacity) {
            capacity <<= 1;
        }

        this.loadFactor = loadFactor;
        this.threshold = (int) (capacity * loadFactor);
        this.table = new Entry[capacity];

        onInit();
    }

    public DefaultHashMap(Map<? extends K, ? extends V> map) {
        this(Math.max((int) (map.size() / DEFAULT_LOAD_FACTOR) + 1, DEFAULT_INITIAL_CAPACITY), DEFAULT_LOAD_FACTOR);
        putAllForCreate(map);
    }

     @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public V get(Object key) {
        Entry<K, V> entry = getEntry(key);

        return entry == null ? null : entry.getValue();
    }

    @Override
    public boolean containsKey(Object key) {
        Entry<K, V> entry = getEntry(key);

        return entry != null;
    }

    @Override
    public V put(K key, V value) {
        Entry<K, V> entry = getEntry(key);

        if (entry != null) {
            V oldValue = entry.getValue();

            entry.setValue(value);
            entry.onAccess();

            return oldValue;
        } else {
            modCount++;


            if (size >= threshold) {
                resize(table.length * 2);
            }

            addEntry(key, value);

            return null;
        }
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> map) {

        int n = map.size();

        if (n == 0) {
            return;
        }

        if (n >= threshold) {
            n = (int) (n / loadFactor + 1);

            if (n > MAXIMUM_CAPACITY) {
                n = MAXIMUM_CAPACITY;
            }

            int capacity = table.length;

            while (capacity < n) {
                capacity <<= 1;
            }

            resize(capacity);
        }

        for (Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public V remove(Object key) {
        Entry<K, V> entry = removeEntryForKey(key);

        return entry == null ? null : entry.getValue();
    }

    @Override
    public void clear() {
        modCount++;
        Arrays.fill(table, null);
        size = 0;
    }

    @Override
    public boolean containsValue(Object value) {
        Entry<K, V>[] tab = table;

        for (Entry<K, V> element : tab) {
            for (Entry<K, V> entry = element; entry != null; entry = entry.next) {
                if (eq(value, entry.getValue())) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public Set<K> keySet() {
        Set<K> ks = keySet;

        return ks != null ? ks : (keySet = new KeySet());
    }

    @Override
    public Collection<V> values() {
        Collection<V> vs = values;

        return vs != null ? vs : (values = new Values());
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        Set<Map.Entry<K, V>> es = entrySet;

        return es != null ? es : (entrySet = new EntrySet());
    }


    protected static class Entry<K, V> extends DefaultMapEntry<K, V> {

        protected final int hash;


        protected Entry<K, V> next;


        protected Entry(int h, K k, V v, Entry<K, V> n) {
            super(k, v);
            next = n;
            hash = h;
        }


        protected void onAccess() {
        }


        protected void onRemove() {
        }
    }

    private abstract class HashIterator<E> implements Iterator<E> {

        private Entry<K, V> current;


        private Entry<K, V> next;

        private int expectedModCount;

        private int index;

        protected HashIterator() {
            expectedModCount = modCount;

            Entry<K, V>[] t = table;
            int i = t.length;
            Entry<K, V> n = null;

            if (size != 0) { // advance to first entry

                while (i > 0 && (n = t[--i]) == null) {
                    ;
                }
            }

            next = n;
            index = i;
        }


        public boolean hasNext() {
            return next != null;
        }


        public void remove() {
            if (current == null) {
                throw new IllegalStateException();
            }

            checkForComodification();

            Object k = current.getKey();

            current = null;
            DefaultHashMap.this.removeEntryForKey(k);
            expectedModCount = modCount;
        }


        protected Entry<K, V> nextEntry() {
            checkForComodification();

            Entry<K, V> entry = next;

            if (entry == null) {
                throw new NoSuchElementException();
            }

            Entry<K, V> n = entry.next;
            Entry<K, V>[] t = table;
            int i = index;

            while (n == null && i > 0) {
                n = t[--i];
            }

            index = i;
            next = n;

            return current = entry;
        }


        private void checkForComodification() {
            if (modCount != expectedModCount) {
                throw new ConcurrentModificationException();
            }
        }
    }

    private class KeyIterator extends HashIterator<K> {

        public K next() {
            return nextEntry().getKey();
        }
    }

    private class ValueIterator extends HashIterator<V> {

        public V next() {
            return nextEntry().getValue();
        }
    }

    private class EntryIterator extends HashIterator<Map.Entry<K, V>> {

        public Map.Entry<K, V> next() {
            return nextEntry();
        }
    }

    private class KeySet extends AbstractSet<K> {

        @Override
        public Iterator<K> iterator() {
            return newKeyIterator();
        }


        @Override
        public int size() {
            return size;
        }


        @Override
        public boolean contains(Object o) {
            return containsKey(o);
        }


        @Override
        public boolean remove(Object o) {
            return DefaultHashMap.this.removeEntryForKey(o) != null;
        }


        @Override
        public void clear() {
            DefaultHashMap.this.clear();
        }
    }

    private class Values extends AbstractCollection<V> {

        @Override
        public Iterator<V> iterator() {
            return newValueIterator();
        }


        @Override
        public int size() {
            return size;
        }


        @Override
        public boolean contains(Object o) {
            return containsValue(o);
        }


        @Override
        public void clear() {
            DefaultHashMap.this.clear();
        }
    }

    private class EntrySet extends AbstractSet<Map.Entry<K, V>> {

        @Override
        public Iterator<Map.Entry<K, V>> iterator() {
            return newEntryIterator();
        }


        @Override
        public boolean contains(Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }

            Map.Entry<?, ?> entry = (Map.Entry<?, ?>) o;
            Entry<K, V> candidate = getEntry(entry.getKey());

            return eq(candidate, entry);
        }


        @Override
        public boolean remove(Object o) {
            return removeEntry(o) != null;
        }


        @Override
        public int size() {
            return size;
        }

        @Override
        public void clear() {
            DefaultHashMap.this.clear();
        }
    }


    private static final long serialVersionUID = 362498820763181265L;

    @SuppressWarnings("unchecked")
    private void readObject(java.io.ObjectInputStream is) throws IOException, ClassNotFoundException {

        is.defaultReadObject();


        int numBuckets = is.readInt();

        table = new Entry[numBuckets];


        onInit();


        int size = is.readInt();

        for (int i = 0; i < size; i++) {
            K key = (K) is.readObject();
            V value = (V) is.readObject();

            putForCreate(key, value);
        }
    }

    private void writeObject(java.io.ObjectOutputStream os) throws IOException {

        os.defaultWriteObject();


        os.writeInt(table.length);


        os.writeInt(size);


        for (Map.Entry<K, V> entry : entrySet()) {
            os.writeObject(entry.getKey());
            os.writeObject(entry.getValue());
        }
    }


    @Override
    @SuppressWarnings("unchecked")
    public Object clone() {
        DefaultHashMap<K, V> result = null;

        try {
            result = (DefaultHashMap<K, V>) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError();
        }

        result.table = new Entry[table.length];
        result.entrySet = null;
        result.modCount = 0;
        result.size = 0;
        result.onInit();
        result.putAllForCreate(this);

        return result;
    }


    protected void onInit() {
    }

    protected Entry<K, V> getEntry(Object key) {
        int hash = hash(key);
        int i = indexFor(hash, table.length);

        for (Entry<K, V> entry = table[i]; entry != null; entry = entry.next) {
            if (entry.hash == hash && eq(key, entry.getKey())) {
                return entry;
            }
        }

        return null;
    }

    protected void addEntry(K key, V value) {
        int hash = hash(key);
        int i = indexFor(hash, table.length);

        table[i] = new Entry<K, V>(hash, key, value, table[i]);
        size++;
    }

    private void putForCreate(K key, V value) {
        Entry<K, V> entry = getEntry(key);

        if (entry != null) {
            entry.setValue(value);
        } else {
            addEntry(key, value);
        }
    }

    private void putAllForCreate(Map<? extends K, ? extends V> map) {
        for (Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
            putForCreate(entry.getKey(), entry.getValue());
        }
    }

    protected Entry<K, V> removeEntryForKey(Object key) {
        int hash = hash(key);
        int i = indexFor(hash, table.length);
        Entry<K, V> prev = table[i];
        Entry<K, V> entry = prev;

        while (entry != null) {
            Entry<K, V> next = entry.next;

            if (entry.hash == hash && eq(key, entry.getKey())) {
                modCount++;
                size--;

                if (prev == entry) {
                    table[i] = next;
                } else {
                    prev.next = next;
                }

                entry.onRemove();

                return entry;
            }

            prev = entry;
            entry = next;
        }

        return entry;
    }

    protected Entry<K, V> removeEntry(Object o) {
        if (!(o instanceof Map.Entry<?, ?>)) {
            return null;
        }

        Map.Entry<?, ?> entry = (Map.Entry<?, ?>) o;
        Object key = entry.getKey();
        int hash = hash(key);
        int i = indexFor(hash, table.length);
        Entry<K, V> prev = table[i];
        Entry<K, V> e = prev;

        while (e != null) {
            Entry<K, V> next = e.next;

            if (e.hash == hash && e.equals(entry)) {
                modCount++;
                size--;

                if (prev == e) {
                    table[i] = next;
                } else {
                    prev.next = next;
                }

                e.onRemove();

                return e;
            }

            prev = e;
            e = next;
        }

        return e;
    }

    protected Iterator<K> newKeyIterator() {
        return new KeyIterator();
    }

    protected Iterator<V> newValueIterator() {
        return new ValueIterator();
    }

    protected Iterator<Map.Entry<K, V>> newEntryIterator() {
        return new EntryIterator();
    }

    protected static int hash(Object obj) {
        int h = obj == null ? 0 : obj.hashCode();

        return h - (h << 7);
    }

    protected static boolean eq(Object x, Object y) {
        return x == null ? y == null : x == y || x.equals(y);
    }

    protected static int indexFor(int hash, int length) {
        return hash & length - 1;
    }

    protected void resize(int newCapacity) {
        Entry<K, V>[] oldTable = table;
        int oldCapacity = oldTable.length;

        if (size < threshold || oldCapacity > newCapacity) {
            return;
        }

        @SuppressWarnings("unchecked")
        Entry<K, V>[] newTable = new Entry[newCapacity];

        transfer(newTable);
        table = newTable;
        threshold = (int) (newCapacity * loadFactor);
    }

    protected void transfer(Entry<K, V>[] newTable) {
        Entry<K, V>[] src = table;
        int newCapacity = newTable.length;

        for (int j = 0; j < src.length; j++) {
            Entry<K, V> entry = src[j];

            if (entry != null) {
                src[j] = null;

                do {
                    Entry<K, V> next = entry.next;
                    int i = indexFor(entry.hash, newCapacity);

                    entry.next = newTable[i];
                    newTable[i] = entry;
                    entry = next;
                } while (entry != null);
            }
        }
    }

    protected int getCapacity() {
        return table.length;
    }

    protected float getLoadFactor() {
        return loadFactor;
    }

    protected int getThreshold() {
        return threshold;
    }
}
