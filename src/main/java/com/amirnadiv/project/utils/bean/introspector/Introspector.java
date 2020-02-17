package com.amirnadiv.project.utils.common.bean.introspector;

public interface Introspector {

    ClassDescriptor lookup(Class<?> type);

    ClassDescriptor register(Class<?> type);

    void reset();

}
