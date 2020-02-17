package com.amirnadiv.project.utils.common;

import java.lang.reflect.Field;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.WeakHashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.LinkedBlockingQueue;

import com.amirnadiv.project.utils.common.able.Keyable;
import com.amirnadiv.project.utils.common.collection.ArrayHashMap;

public abstract class CollectionUtil {

    public static boolean isEmpty(Map<?, ?> map) {
        return (map == null) || (map.size() == 0);
    }

    public static boolean isNotEmpty(Map<?, ?> map) {
        return (map != null) && (map.size() > 0);
    }

    public static boolean isEmpty(Collection<?> collection) {
        return (collection == null) || (collection.size() == 0);
    }

    public static boolean isNotEmpty(Collection<?> collection) {
        return (collection != null) && (collection.size() > 0);
    }

    public static boolean hasItems(Enumeration<?> enums) {
        return (enums != null) && (enums.hasMoreElements());
    }

    public static boolean hasNotItems(Enumeration<?> enums) {
        return (enums == null) || (!enums.hasMoreElements());
    }

    public static boolean hasItems(Iterator<?> iters) {
        return (iters != null) && (iters.hasNext());
    }

    public static boolean hasNotItems(Iterator<?> iters) {
        return (iters != null) && (iters.hasNext());
    }

    public static <E> ArrayList<E> createArrayList() {
        return new ArrayList<E>();
    }

    public static <E> ArrayList<E> createArrayList(int initialCapacity) {
        return new ArrayList<E>(initialCapacity);
    }

    public static <E> ArrayList<E> createArrayList(Collection<? extends E> collection) {
        if (collection == null) {
            return new ArrayList<E>();
        }

        return new ArrayList<E>(collection);
    }

    public static <E> ArrayList<E> createArrayList(Iterable<? extends E> iter) {

        if (iter instanceof Collection<?>) {
            return new ArrayList<E>((Collection<? extends E>) iter);
        }

        ArrayList<E> list = new ArrayList<E>();

        iterableToCollection(iter, list);

        list.trimToSize();

        return list;
    }

    public static <T, V extends T> ArrayList<T> createArrayList(V...args) {
        if (args == null || args.length == 0) {
            return new ArrayList<T>();
        }

        ArrayList<T> list = new ArrayList<T>(args.length);

        for (V v : args) {
            list.add(v);
        }

        return list;

    }

    public static <E> LinkedList<E> createLinkedList() {
        return new LinkedList<E>();
    }

    public static <E> LinkedList<E> createLinkedList(Collection<? extends E> collection) {
        if (collection == null) {
            return new LinkedList<E>();
        }

        return new LinkedList<E>(collection);
    }

    public static <T> LinkedList<T> createLinkedList(Iterable<? extends T> c) {
        LinkedList<T> list = new LinkedList<T>();

        iterableToCollection(c, list);

        return list;
    }

    public static <T, V extends T> LinkedList<T> createLinkedList(V...args) {
        LinkedList<T> list = new LinkedList<T>();

        if (args != null) {
            for (V v : args) {
                list.add(v);
            }
        }

        return list;
    }

    public static <T> List<T> asList(T...args) {
        if (args == null || args.length == 0) {
            return Collections.emptyList();
        }

        return Arrays.asList(args);
    }

    public static <E> HashSet<E> createHashSet() {
        return new HashSet<E>();
    }

    public static <E> HashSet<E> createHashSet(int initialCapacity) {
        return new HashSet<E>(initialCapacity);
    }

    public static <E> HashSet<E> createHashSet(Collection<? extends E> collection) {
        if (collection == null) {
            return new HashSet<E>();
        }
        return new HashSet<E>(collection);
    }

    public static <E, O extends E> HashSet<E> createHashSet(O...args) {
        if (args == null || args.length == 0) {
            return new HashSet<E>();
        }

        HashSet<E> set = new HashSet<E>(args.length);
        for (O o : args) {
            set.add(o);
        }

        return set;
    }

    public static <T> HashSet<T> createHashSet(Iterable<? extends T> iter) {
        HashSet<T> set;

        if (iter instanceof Collection<?>) {
            set = new HashSet<T>((Collection<? extends T>) iter);
        } else {
            set = new HashSet<T>();
            iterableToCollection(iter, set);
        }

        return set;
    }

    public static <E> LinkedHashSet<E> createLinkedHashSet() {
        return new LinkedHashSet<E>();
    }

    public static <T, V extends T> LinkedHashSet<T> createLinkedHashSet(V...args) {
        if (args == null || args.length == 0) {
            return new LinkedHashSet<T>();
        }
        LinkedHashSet<T> set = new LinkedHashSet<T>(args.length);

        for (V v : args) {
            set.add(v);
        }

        return set;
    }

    public static <T> LinkedHashSet<T> createLinkedHashSet(Iterable<? extends T> iter) {
        LinkedHashSet<T> set;

        if (iter instanceof Collection<?>) {
            set = new LinkedHashSet<T>((Collection<? extends T>) iter);
        } else {
            set = new LinkedHashSet<T>();
            iterableToCollection(iter, set);
        }

        return set;
    }

    @SuppressWarnings("unchecked")
    public static <T, V extends T> TreeSet<T> createTreeSet(V...args) {
        return (TreeSet<T>) createTreeSet(null, args);
    }

    public static <T> TreeSet<T> createTreeSet(Iterable<? extends T> c) {
        return createTreeSet(null, c);
    }

    public static <T> TreeSet<T> createTreeSet(Comparator<? super T> comparator) {
        return new TreeSet<T>(comparator);
    }

    public static <T, V extends T> TreeSet<T> createTreeSet(Comparator<? super T> comparator, V...args) {
        TreeSet<T> set = new TreeSet<T>(comparator);

        if (args != null) {
            for (V v : args) {
                set.add(v);
            }
        }

        return set;
    }

    public static <T> TreeSet<T> createTreeSet(Comparator<? super T> comparator, Iterable<? extends T> c) {
        TreeSet<T> set = new TreeSet<T>(comparator);

        iterableToCollection(c, set);

        return set;
    }

    public static <E> TreeSet<E> createTreeSet(SortedSet<E> set) {
        if (set == null) {
            return new TreeSet<E>();
        }

        return new TreeSet<E>(set);
    }

    public static <K, V> HashMap<K, V> createHashMap() {
        return new HashMap<K, V>();
    }

    public static <K, V> HashMap<K, V> createHashMap(int initialCapacity) {
        return new HashMap<K, V>(initialCapacity);
    }

    public static <K, V> HashMap<K, V> createHashMap(int initialCapacity, float loadFactor) {
        return new HashMap<K, V>(initialCapacity, loadFactor);
    }

    public static <K, V> HashMap<K, V> synchronizedMap() {
        return (HashMap<K, V>) Collections.synchronizedMap(new HashMap<K, V>());
    }

    public static <K, V> HashMap<K, V> createHashMap(Map<? extends K, ? extends V> map) {
        return new HashMap<K, V>(map);
    }

    public static <K, V> LinkedHashMap<K, V> createLinkedHashMap() {
        return new LinkedHashMap<K, V>();
    }

    public static <K, V> LinkedHashMap<K, V> createLinkedHashMap(int initialCapacity) {
        return new LinkedHashMap<K, V>(initialCapacity);
    }

    public static <K, V> LinkedHashMap<K, V> createLinkedHashMap(int initialCapacity, float loadFactor) {
        return new LinkedHashMap<K, V>(initialCapacity, loadFactor);
    }

    public static <K, V> LinkedHashMap<K, V> createLinkedHashMap(Map<? extends K, ? extends V> map) {
        if (map == null) {
            return new LinkedHashMap<K, V>();
        }

        return new LinkedHashMap<K, V>(map);
    }

    public static <K, V> ArrayHashMap<K, V> createArrayHashMap() {
        return new ArrayHashMap<K, V>();
    }

    public static <K, V> ArrayHashMap<K, V> createArrayHashMap(int initialCapacity) {
        return new ArrayHashMap<K, V>(initialCapacity);
    }

    public static <K, V> ConcurrentMap<K, V> createConcurrentMap() {
        return new ConcurrentHashMap<K, V>();
    }

    public static <K, V> ConcurrentMap<K, V> createConcurrentMap(Map<? extends K, ? extends V> map) {
        if (map == null) {
            return null;
        }

        return new ConcurrentHashMap<K, V>(map);
    }

    public static <K, V> ConcurrentMap<K, V> createConcurrentMap(int initialCapacity) {
        return new ConcurrentHashMap<K, V>(initialCapacity);
    }

    public static <K, V> ConcurrentMap<K, V> createConcurrentMap(int initialCapacity, float loadFactor) {
        return new ConcurrentHashMap<K, V>(initialCapacity, loadFactor);
    }

    private static <E> void iterableToCollection(Iterable<? extends E> iter, Collection<E> list) {
        if (iter == null) {
            return;
        }

        for (E element : iter) {
            list.add(element);
        }
    }

    public static <E extends Enum<E>> EnumSet<E> createEnumSet(Collection<E> c) {
        if (c == null) {
            return null;
        }

        return EnumSet.copyOf(c);
    }

    public static <E extends Enum<E>> EnumSet<E> createEnumSet(Class<E> elementType) {
        if (elementType == null) {
            return null;
        }

        return EnumSet.allOf(elementType);
    }

    public static <K, V> TreeMap<K, V> createTreeMap() {
        return new TreeMap<K, V>();
    }

    public static <K, V> TreeMap<K, V> createTreeMap(Comparator<? super K> comparator) {
        if (comparator == null) {
            return null;
        }

        return new TreeMap<K, V>(comparator);
    }

    public static <K, V> TreeMap<K, V> createTreeMap(Map<? extends K, ? extends V> map) {
        if (map == null) {
            return null;
        }

        return new TreeMap<K, V>(map);
    }

    public static <K, V> TreeMap<K, V> createTreeMap(SortedMap<K, ? extends V> map) {
        if (map == null) {
            return null;
        }

        return new TreeMap<K, V>(map);
    }

    public static <K, V> WeakHashMap<K, V> createWeakHashMap() {
        return new WeakHashMap<K, V>();
    }

    public static <K, V> WeakHashMap<K, V> createWeakHashMap(int initialCapacity) {
        return new WeakHashMap<K, V>(initialCapacity);
    }

    public static <K, V> WeakHashMap<K, V> createWeakHashMap(Map<? extends K, ? extends V> map) {
        if (map == null) {
            return new WeakHashMap<K, V>();
        }

        return new WeakHashMap<K, V>(map);
    }

    public static <K, V> WeakHashMap<K, V> createWeakHashMap(int initialCapacity, float loadFactor) {
        return new WeakHashMap<K, V>(initialCapacity, loadFactor);
    }

    public static <K, V> IdentityHashMap<K, V> createIdentityHashMap() {
        return new IdentityHashMap<K, V>();
    }

    public static <K, V> IdentityHashMap<K, V> createIdentityHashMap(int initialCapacity) {
        return new IdentityHashMap<K, V>(initialCapacity);
    }

    public static <K, V> IdentityHashMap<K, V> createIdentityHashMap(Map<? extends K, ? extends V> map) {
        if (map == null) {
            return null;
        }

        return new IdentityHashMap<K, V>(map);
    }

    public static <K extends Enum<K>, V> EnumMap<K, V> createEnumMap(Class<K> keyType) {
        if (keyType == null) {
            return null;
        }

        return new EnumMap<K, V>(keyType);
    }

    public static <K extends Enum<K>, V> EnumMap<K, V> createEnumMap(Map<K, ? extends V> map) {
        if (map == null) {
            return null;
        }

        return new EnumMap<K, V>(map);
    }

    public static <E> PriorityQueue<E> createPriorityQueue() {
        return new PriorityQueue<E>();
    }

    public static <E> PriorityQueue<E> createPriorityQueue(int initialCapacity) {
        return new PriorityQueue<E>(initialCapacity);
    }

    public static <E> PriorityQueue<E> createPriorityQueue(Collection<? extends E> collection) {
        if (collection == null) {
            return null;
        }

        return new PriorityQueue<E>(collection);
    }

    public static <E> PriorityQueue<E> createPriorityQueue(int initialCapacity, Comparator<? super E> comparator) {
        if (comparator == null) {
            return new PriorityQueue<E>(initialCapacity);
        }

        return new PriorityQueue<E>(initialCapacity, comparator);
    }

    public static <E> PriorityQueue<E> createPriorityQueue(PriorityQueue<? extends E> queue) {
        if (queue == null) {
            return null;
        }

        return new PriorityQueue<E>(queue);
    }

    public static <E> PriorityQueue<E> createPriorityQueue(SortedSet<? extends E> set) {
        if (set == null) {
            return null;
        }

        return new PriorityQueue<E>(set);
    }

    public static <E> ArrayDeque<E> createArrayDeque() {
        return new ArrayDeque<E>();
    }

    public static <E> ArrayDeque<E> createArrayDeque(Collection<? extends E> collection) {
        if (collection == null) {
            return null;
        }

        return new ArrayDeque<E>(collection);
    }

    public static <E> ArrayDeque<E> createArrayDeque(int initialCapacity) {
        return new ArrayDeque<E>(initialCapacity);
    }

    public static <E> BitSet createBitSet() {
        return new BitSet();
    }

    public static <E> BitSet createBitSet(int initialCapacity) {
        return new BitSet();
    }

    public static <K, V> ConcurrentSkipListMap<K, V> createConcurrentSkipListMap() {
        return new ConcurrentSkipListMap<K, V>();
    }

    public static <K, V> ConcurrentSkipListMap<K, V> createConcurrentSkipListMap(Comparator<? super K> comparator) {
        if (comparator == null) {
            return new ConcurrentSkipListMap<K, V>();
        }

        return new ConcurrentSkipListMap<K, V>(comparator);
    }

    public static <K, V> ConcurrentSkipListMap<K, V> createConcurrentSkipListMap(Map<? extends K, ? extends V> map) {
        if (map == null) {
            return new ConcurrentSkipListMap<K, V>();
        }

        return new ConcurrentSkipListMap<K, V>(map);
    }

    public static <K, V> ConcurrentSkipListMap<K, V> createConcurrentSkipListMap(SortedMap<? extends K, ? extends V> map) {
        if (map == null) {
            return new ConcurrentSkipListMap<K, V>();
        }

        return new ConcurrentSkipListMap<K, V>(map);
    }

    public static <E> ConcurrentSkipListSet<E> createConcurrentSkipListSet() {
        return new ConcurrentSkipListSet<E>();
    }

    public static <E> ConcurrentSkipListSet<E> createConcurrentSkipListSet(Collection<? extends E> collection) {
        if (collection == null) {
            return new ConcurrentSkipListSet<E>();
        }

        return new ConcurrentSkipListSet<E>(collection);
    }

    public static <E> ConcurrentSkipListSet<E> createConcurrentSkipListSet(Comparator<? super E> comparator) {
        if (comparator == null) {
            return new ConcurrentSkipListSet<E>();
        }

        return new ConcurrentSkipListSet<E>(comparator);
    }

    public static <E> ConcurrentSkipListSet<E> createConcurrentSkipListSet(SortedSet<E> set) {
        if (set == null) {
            return new ConcurrentSkipListSet<E>();
        }

        return new ConcurrentSkipListSet<E>(set);
    }

    public static <E> Queue<E> createConcurrentLinkedQueue() {
        return new ConcurrentLinkedQueue<E>();
    }

    public static <E> Queue<E> createConcurrentLinkedQueue(Collection<? extends E> collection) {
        if (collection == null) {
            return new ConcurrentLinkedQueue<E>();
        }

        return new ConcurrentLinkedQueue<E>(collection);
    }

    public static <E> CopyOnWriteArrayList<E> createCopyOnWriteArrayList() {
        return new CopyOnWriteArrayList<E>();
    }

    public static <E> CopyOnWriteArrayList<E> createCopyOnWriteArrayList(Collection<? extends E> collection) {
        if (collection == null) {
            return new CopyOnWriteArrayList<E>();
        }

        return new CopyOnWriteArrayList<E>();
    }

    public static <E> CopyOnWriteArrayList<E> createCopyOnWriteArrayList(E[] toCopyIn) {
        if (toCopyIn == null) {
            return new CopyOnWriteArrayList<E>();
        }

        return new CopyOnWriteArrayList<E>(toCopyIn);
    }

    public static <E> CopyOnWriteArraySet<E> createCopyOnWriteArraySet() {
        return new CopyOnWriteArraySet<E>();
    }

    public static <E> CopyOnWriteArraySet<E> createCopyOnWriteArraySet(Collection<? extends E> collection) {
        return new CopyOnWriteArraySet<E>();
    }

    public static <E> BlockingQueue<E> createLinkedBlockingQueue() {
        return new LinkedBlockingQueue<E>();
    }

    public static <E> BlockingQueue<E> createLinkedBlockingQueue(int capacity) {
        return new LinkedBlockingQueue<E>(capacity);
    }

    public static <E> BlockingQueue<E> createLinkedBlockingQueue(Collection<? extends E> collection) {
        if (collection == null) {
            return new LinkedBlockingQueue<E>();
        }

        return new LinkedBlockingQueue<E>(collection);
    }

    // ==========================================================================
    // 常用转换。
    // ==========================================================================

    public static <T extends Keyable<K>, K> Map<K, T> list2Map(List<T> list) {
        if (list == null) {
            return null;
        }

        Map<K, T> result = createHashMap(list.size());
        for (T data : list) {
            result.put(data.getId(), data);
        }

        return result;
    }

    public static <T extends Keyable<K>, K> Collection<K> collectId(List<T> list) {
        if (list == null) {
            return null;
        }

        Collection<K> result = createHashSet(list.size());
        for (T data : list) {
            result.add(data.getId());
        }

        return result;
    }

    public static <T extends Keyable<K>, K, O extends Keyable<K>> void merge(List<T> list, Map<K, O> map,
            String...fields) {
        if (isEmpty(list) || isEmpty(map) || ArrayUtil.isEmpty(fields)) {
            return;
        }

        for (T data : list) {
            O from = map.get(data.getId());
            if (from == null) {
                continue;
            }

            for (String field : fields) {
                Object value = ReflectionUtil.readField(field, from);
                ReflectionUtil.writeField(data, field, value);
            }
        }

    }

    // FIXME cached
    public static <T extends Keyable<K>, K, O extends Keyable<K>> void merge(List<T> list, Map<K, O> map,
            Field...fields) {
        if (isEmpty(list) || isEmpty(map) || ArrayUtil.isEmpty(fields)) {
            return;
        }

        for (T data : list) {
            O from = map.get(data.getId());
            if (from == null) {
                continue;
            }

            for (Field field : fields) {
                Object value = ReflectionUtil.readField(field, from);
                ReflectionUtil.writeField(data, field.getName(), value);
            }
        }

    }


    public static <T extends Keyable<K>, K, O extends Keyable<K>> void merge(Class<T> clazz, List<T> list,
            Map<K, O> map, Field...fields) {
        if (isEmpty(list) || isEmpty(map) || ArrayUtil.isEmpty(fields) || clazz == null) {
            return;
        }

        Field[] allFields = ReflectionUtil.getAllInstanceFields(clazz);
        Set<Field> firstFieldSet = createHashSet(fields);
        Set<Field> secFieldSet = createHashSet(allFields);
        Set<Field> intersection = intersection(firstFieldSet, secFieldSet);

        for (T data : list) {
            O from = map.get(data.getId());
            if (from == null) {
                continue;
            }

            for (Field field : intersection) {
                Object value = ReflectionUtil.readField(field, from);
                ReflectionUtil.writeField(data, field.getName(), value);
            }
        }

    }

    // ==========================================================================
    // 集合运算。
    // ==========================================================================

    public static final <T> Set<T> intersection(Set<T> set1, Set<T> set2) {
        if (set1 == null || set2 == null) {
            return null;
        }

        if (set1.isEmpty() || set2.isEmpty()) {
            return Collections.emptySet();
        }

        Set<T> result = CollectionUtil.createHashSet();
        Set<T> smaller = (set1.size() > set2.size()) ? set2 : set1;
        Set<T> bigger = (smaller == set2) ? set1 : set2;

        for (T value : smaller) {
            if (bigger.contains(value)) {
                result.add(value);
            }
        }
        return result;
    }

    public static <T> Set<T> subtract(Set<T> set1, Set<T> set2) {
        if (set1 == null || set2 == null) {
            return null;
        }
        Set<T> result = createHashSet(set1);
        result.removeAll(set2);
        return result;
    }

    public static <T> Set<T> union(Set<T> set1, Set<T> set2) {
        if (isEmpty(set1)) {
            return set2;
        }
        if (isEmpty(set2)) {
            return set1;
        }

        Set<T> result = createHashSet(set1);
        result.addAll(set2);
        return result;
    }

    public static <T> List<? extends T> concatSuper(List<? extends T> collection1, List<? extends T> collection2) {
        if (isEmpty(collection1)) {
            return collection2;
        }
        if (isEmpty(collection2)) {
            return collection1;
        }
        List<T> result = createArrayList(collection1.size() + collection2.size());
        result.addAll(collection1);
        result.addAll(collection2);
        return result;

    }

    public static <T> List<T> concat(List<T> collection1, List<T> collection2) {
        if (isEmpty(collection1)) {
            return collection2;
        }
        if (isEmpty(collection2)) {
            return collection1;
        }

        collection1.addAll(collection2);
        return collection1;

    }

    // FIXME createCollections add DataFilter implement

}
