
package com.amirnadiv.project.utils.common.able;

import java.util.concurrent.Callable;


public interface Computable<K, V> {


    V get(K key, Callable<V> callable);

}
