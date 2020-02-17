package com.amirnadiv.project.utils.common.bean.introspector;

import java.lang.reflect.Constructor;

public class Constructors {

    protected final ClassDescriptor classDescriptor;
    protected final ConstructorDescriptor[] allConstructors;
    protected ConstructorDescriptor defaultConstructor;

    public Constructors(ClassDescriptor classDescriptor) {
        this.classDescriptor = classDescriptor;
        this.allConstructors = inspectConstructors();
    }

    protected ConstructorDescriptor[] inspectConstructors() {
        Class<?> type = classDescriptor.getType();
        Constructor<?>[] ctors = type.getDeclaredConstructors();

        ConstructorDescriptor[] allConstructors = new ConstructorDescriptor[ctors.length];

        for (int i = 0; i < ctors.length; i++) {
            Constructor<?> ctor = ctors[i];

            ConstructorDescriptor ctorDescriptor = createCtorDescriptor(ctor);
            allConstructors[i] = ctorDescriptor;

            if (ctorDescriptor.isDefault()) {
                defaultConstructor = ctorDescriptor;
            }
        }

        return allConstructors;
    }

    protected ConstructorDescriptor createCtorDescriptor(Constructor<?> constructor) {
        return new ConstructorDescriptor(classDescriptor, constructor);
    }

    public ConstructorDescriptor getDefaultCtor() {
        return defaultConstructor;
    }

    public ConstructorDescriptor getCtorDescriptor(Class<?>...args) {
        ctors: for (ConstructorDescriptor ctorDescriptor : allConstructors) {
            Class<?>[] arg = ctorDescriptor.getParameters();

            if (arg.length != args.length) {
                continue;
            }

            for (int j = 0; j < arg.length; j++) {
                if (arg[j] != args[j]) {
                    continue ctors;
                }
            }

            return ctorDescriptor;
        }
        return null;
    }

    ConstructorDescriptor[] getAllCtorDescriptors() {
        return allConstructors;
    }

}
