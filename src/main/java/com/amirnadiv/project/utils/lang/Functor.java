package com.amirnadiv.project.utils.common.lang;

import java.lang.reflect.Method;

import com.amirnadiv.project.utils.common.ArrayUtil;
import com.amirnadiv.project.utils.common.Assert;
import com.amirnadiv.project.utils.common.Emptys;
import com.amirnadiv.project.utils.common.ReflectionUtil;
import com.amirnadiv.project.utils.common.able.ClosureResult;

public class Functor implements ClosureResult<Object> {

    private Object target;
    private Method method;

    private Class<?>[] parameterTypes;

    private Object result;

    public Functor(Object target, String methodName) {
        this(target, methodName, Emptys.EMPTY_CLASS_ARRAY);
    }

    public Functor(Object target, String methodName, Class<?>...parameterTypes) {

        Assert.assertNotNull(target, "target obejct is not null.");

        this.target = target;
        this.parameterTypes = parameterTypes;
        method = ReflectionUtil.getMethod(target.getClass(), methodName, this.parameterTypes);

        Assert.assertNotNull(method, "method [" + target.getClass() + "." + methodName + "] !NOT! exist.");

    }

    @Override
    public void execute(Object...args) {
        result = ReflectionUtil.invokeMethod(method, target, args);
    }

    @Override
    public String toString() {
        return new StringBuilder().append(this.target).append(".").append(this.method.getName())
                .append(ArrayUtil.toString(parameterTypes)).toString();
    }

    @Override
    public Object getResult() {
        return result;
    }
}
