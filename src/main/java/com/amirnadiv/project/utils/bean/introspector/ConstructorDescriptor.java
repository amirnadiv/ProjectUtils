package com.amirnadiv.project.utils.common.bean.introspector;

import java.lang.reflect.Constructor;

import com.amirnadiv.project.utils.common.ReflectionUtil;

public class ConstructorDescriptor extends Descriptor {

    protected final Constructor<?> constructor;
    protected final Class<?>[] parameters;

    public ConstructorDescriptor(ClassDescriptor classDescriptor, Constructor<?> constructor) {
        super(classDescriptor, ReflectionUtil.isPublic(constructor));
        this.constructor = constructor;
        this.parameters = constructor.getParameterTypes();

        ReflectionUtil.forceAccess(constructor);
    }

    @Override
    public String getName() {
        return constructor.getName();
    }

    public Constructor<?> getConstructor() {
        return constructor;
    }

    public Class<?>[] getParameters() {
        return parameters;
    }

    public boolean isDefault() {
        return parameters.length == 0;
    }

}
