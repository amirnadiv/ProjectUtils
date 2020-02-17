package com.amirnadiv.project.utils.common.collection;

import java.util.AbstractList;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.NoSuchElementException;

public class ArrayHashMap<K, V> extends DefaultHashMap<K, V> implements ListMap<K, V> {
    private static final long serialVersionUID = 3258411729271927857L;

    // ==========================================================================
    // 成员变量
    // ==========================================================================

    protected transient DefaultHashMap.Entry<K, V>[] order;

    private transient List<K> keyList;

    private transient List<V> valueList;

    private transient List<Map.Entry<K, V>> entryList;

    // ==========================================================================
    // 构造函数
    // ==========================================================================

    public ArrayHashMap() {
        super();
    }

    public ArrayHashMap(int initialCapacity) {
        super(initialCapacity);
    }

    public ArrayHashMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public ArrayHashMap(Map<? extends K, ? extends V> map) {
        super(map);
    }

    // ==========================================================================
    // 实现Map和ListMap接口的方法
    // ==========================================================================

    @Override
    public boolean containsValue(Object value) {
        // 覆盖此方法是出于性能的考虑. 利用数组查找更有效.
        for (int i = 0; i < size; i++) {
            Entry entry = (Entry) order[i];

            if (eq(value, entry.getValue())) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void clear() {
        super.clear();
        Arrays.fill(order, null);
    }

    public V get(int index) {
        checkRange(index);
        return order[index].getValue();
    }

    public K getKey(int index) {
        checkRange(index);
        return order[index].getKey();
    }

    public Map.Entry<K, V> removeEntry(int index) {
        checkRange(index);
        return removeEntryForKey(order[index].getKey());
    }

    public List<K> keyList() {
        return keyList != null ? keyList : (keyList = new KeyList());
    }

    public List<V> valueList() {
        return valueList != null ? valueList : (valueList = new ValueList());
    }

    public List<Map.Entry<K, V>> entryList() {
        return entryList != null ? entryList : (entryList = new EntryList());
    }


    protected class Entry extends DefaultHashMap.Entry<K, V> {

        protected int index;


        protected Entry(int h, K k, V v, DefaultHashMap.Entry<K, V> n) {
            super(h, k, v, n);
        }


        @Override
        protected void onRemove() {
            int numMoved = size - index;

            if (numMoved > 0) {
                System.arraycopy(order, index + 1, order, index, numMoved);
            }

            order[size] = null;

            for (int i = index; i < size; i++) {
                ((Entry) order[i]).index--;
            }
        }
    }

    private abstract class ArrayHashIterator<E> implements ListIterator<E> {

        private Entry lastReturned;


        private int cursor;


        private int expectedModCount;


        protected ArrayHashIterator(int index) {
            if (index < 0 || index > size()) {
                throw new IndexOutOfBoundsException("Index: " + index);
            }

            cursor = index;
            expectedModCount = modCount;
        }

        public void add(E o) {
            throw new UnsupportedOperationException();
        }


        public void set(E o) {
            throw new UnsupportedOperationException();
        }


        public boolean hasNext() {
            return cursor < size;
        }


        public boolean hasPrevious() {
            return cursor > 0;
        }


        public int nextIndex() {
            return cursor;
        }


        public int previousIndex() {
            return cursor - 1;
        }


        public void remove() {
            if (lastReturned == null) {
                throw new IllegalStateException();
            }

            checkForComodification();

            removeEntryForKey(lastReturned.getKey());

            if (lastReturned.index < cursor) {
                cursor--;
            }

            lastReturned = null;
            expectedModCount = modCount;
        }


        protected Entry nextEntry() {
            checkForComodification();

            if (cursor >= size) {
                throw new NoSuchElementException();
            }

            lastReturned = (Entry) order[cursor++];

            return lastReturned;
        }


        protected Entry previousEntry() {
            checkForComodification();

            if (cursor <= 0) {
                throw new NoSuchElementException();
            }

            lastReturned = (Entry) order[--cursor];

            return lastReturned;
        }


        protected void setValue(V o) {
            if (lastReturned == null) {
                throw new IllegalStateException();
            }

            checkForComodification();

            lastReturned.setValue(o);
        }


        private void checkForComodification() {
            if (modCount != expectedModCount) {
                throw new ConcurrentModificationException();
            }
        }
    }

    private class KeyIterator extends ArrayHashIterator<K> {

        protected KeyIterator(int index) {
            super(index);
        }


        public K next() {
            return nextEntry().getKey();
        }


        public K previous() {
            return previousEntry().getKey();
        }
    }

    private class ValueIterator extends ArrayHashIterator<V> {

        protected ValueIterator(int index) {
            super(index);
        }


        @Override
        public void set(V o) {
            setValue(o);
        }


        public V next() {
            return nextEntry().getValue();
        }


        public V previous() {
            return previousEntry().getValue();
        }
    }

    private class EntryIterator extends ArrayHashIterator<Map.Entry<K, V>> {

        protected EntryIterator(int index) {
            super(index);
        }


        public Map.Entry<K, V> next() {
            return nextEntry();
        }


        public Map.Entry<K, V> previous() {
            return previousEntry();
        }
    }

    private abstract class ArrayHashList<E> extends AbstractList<E> {

        @Override
        public int size() {
            return size;
        }


        @Override
        public boolean isEmpty() {
            return size == 0;
        }


        @Override
        public void clear() {
            ArrayHashMap.this.clear();
        }


        @Override
        public int lastIndexOf(Object o) {
            return indexOf(o);
        }
    }

    private class EntryList extends ArrayHashList<Map.Entry<K, V>> {

        @Override
        public boolean contains(Object o) {
            if (!(o instanceof Map.Entry<?, ?>)) {
                return false;
            }

            Map.Entry<?, ?> entry = (Map.Entry<?, ?>) o;
            Entry candidate = (Entry) getEntry(entry.getKey());

            return eq(candidate, entry);
        }


        @Override
        public Iterator<Map.Entry<K, V>> iterator() {
            return newEntryIterator();
        }


        @Override
        public boolean remove(Object o) {
            return removeEntry(o) != null;
        }


        @Override
        public Map.Entry<K, V> remove(int index) {
            checkRange(index);
            return removeEntryForKey(order[index].getKey());
        }


        @Override
        public Map.Entry<K, V> get(int index) {
            checkRange(index);
            return order[index];
        }


        @Override
        public int indexOf(Object o) {
            if (o != null && o instanceof Map.Entry<?, ?>) {
                Entry entry = (Entry) getEntry(((Map.Entry<?, ?>) o).getKey());

                if (entry != null && entry.equals(o)) {
                    return entry.index;
                }
            }

            return -1;
        }


        @Override
        public ListIterator<Map.Entry<K, V>> listIterator(int index) {
            return new EntryIterator(index);
        }
    }

    private class KeyList extends ArrayHashList<K> {

        @Override
        public boolean contains(Object o) {
            return ArrayHashMap.this.containsKey(o);
        }


        @Override
        public Iterator<K> iterator() {
            return newKeyIterator();
        }


        @Override
        public boolean remove(Object o) {
            Entry entry = (Entry) getEntry(o);

            if (entry == null) {
                return false;
            } else {
                removeEntry(entry);
                return true;
            }
        }


        @Override
        public K remove(int index) {
            checkRange(index);
            return removeEntryForKey(order[index].getKey()).getKey();
        }


        @Override
        public K get(int index) {
            checkRange(index);
            return order[index].getKey();
        }


        @Override
        public int indexOf(Object o) {
            Entry entry = (Entry) getEntry(o);

            if (entry != null) {
                return entry.index;
            }

            return -1;
        }


        @Override
        public ListIterator<K> listIterator(int index) {
            return new KeyIterator(index);
        }
    }

    private class ValueList extends ArrayHashList<V> {

        @Override
        public boolean contains(Object o) {
            return ArrayHashMap.this.containsValue(o);
        }


        @Override
        public Iterator<V> iterator() {
            return newValueIterator();
        }


        @Override
        public boolean remove(Object o) {
            int index = indexOf(o);

            if (index != -1) {
                ArrayHashMap.this.removeEntry(index);
                return true;
            }

            return false;
        }


        @Override
        public V remove(int index) {
            checkRange(index);
            return removeEntryForKey(order[index].getKey()).getValue();
        }


        @Override
        public V get(int index) {
            checkRange(index);
            return order[index].getValue();
        }


        @Override
        public int indexOf(Object o) {
            for (int i = 0; i < size; i++) {
                if (eq(o, order[i].getValue())) {
                    return i;
                }
            }

            return -1;
        }


        @Override
        public ListIterator<V> listIterator(int index) {
            return new ValueIterator(index);
        }
    }


    @Override
    @SuppressWarnings("unchecked")
    protected void onInit() {
        order = new DefaultHashMap.Entry[threshold];
    }

    @Override
    protected void addEntry(K key, V value) {
        int hash = hash(key);
        int i = indexFor(hash, table.length);
        Entry entry = new Entry(hash, key, value, table[i]);

        table[i] = entry;
        entry.index = size;
        order[size++] = entry;
    }

    @Override
    protected Iterator<K> newKeyIterator() {
        return new KeyIterator(0);
    }

    @Override
    protected Iterator<V> newValueIterator() {
        return new ValueIterator(0);
    }

    @Override
    protected Iterator<Map.Entry<K, V>> newEntryIterator() {
        return new EntryIterator(0);
    }

    @Override
    protected void resize(int newCapacity) {
        super.resize(newCapacity);

        if (threshold > order.length) {
            @SuppressWarnings("unchecked")
            DefaultHashMap.Entry<K, V>[] newOrder = new DefaultHashMap.Entry[threshold];

            System.arraycopy(order, 0, newOrder, 0, order.length);

            order = newOrder;
        }
    }

    @Override
    protected void transfer(DefaultHashMap.Entry<K, V>[] newTable) {
        int newCapacity = newTable.length;

        for (int i = 0; i < size; i++) {
            Entry entry = (Entry) order[i];
            int index = indexFor(entry.hash, newCapacity);

            entry.next = newTable[index];
            newTable[index] = entry;
        }
    }

    private void checkRange(int index) {
        if (index >= size || index < 0) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }
}
