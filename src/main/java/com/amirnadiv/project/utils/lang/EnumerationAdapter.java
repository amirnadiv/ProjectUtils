package com.amirnadiv.project.utils.common.lang;

import java.util.Enumeration;
import java.util.Iterator;

import com.amirnadiv.project.utils.common.able.Adaptable;

public class EnumerationAdapter<E> implements Iterator<E>, Adaptable<Enumeration<?>, Iterator<E>> {

    private Enumeration<?> enumeration;

    public EnumerationAdapter() {

    }

    public EnumerationAdapter(Enumeration<?> enumeration) {
        this.enumeration = enumeration;
    }

    @Override
    public boolean hasNext() {
        return enumeration.hasMoreElements();
    }

    @Override
    public E next() {
        @SuppressWarnings("unchecked")
        E next = (E) enumeration.nextElement();
        return next;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<E> forNew(Enumeration<?> old) {
        return new EnumerationAdapter<E>(old);
    }

}
