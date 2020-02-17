package com.amirnadiv.project.utils.common.bean.introspector;

public class ClassIntrospector {

    private static final Introspector introspector = new CachingIntrospector();

    public static ClassDescriptor lookup(Class<?> type) {
        return introspector.lookup(type);
    }

    public static ClassDescriptor register(Class<?> type) {
        return introspector.register(type);
    }

    public static void reset() {
        introspector.reset();
    }

}
