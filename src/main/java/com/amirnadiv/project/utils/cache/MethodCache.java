package com.amirnadiv.project.utils.common.cache;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.Callable;

import com.amirnadiv.project.utils.common.ArrayUtil;
import com.amirnadiv.project.utils.common.ClassUtil;
import com.amirnadiv.project.utils.common.CollectionUtil;
import com.amirnadiv.project.utils.common.ReflectionUtil;
import com.amirnadiv.project.utils.common.able.Computable;

public class MethodCache {

    private static final MethodCache instance = new MethodCache();

    private final Computable<String, Method[]> cache = new ConcurrentCache<String, Method[]>();

    private final Computable<String, Map<String, Method>> cachedMap =
            new ConcurrentCache<String, Map<String, Method>>();

    private MethodCache() {

    }

    public static MethodCache getInstance() {
        return instance;
    }

    public Method[] getMethods(final Class<?> clazz) {
        if (clazz == null) {
            return null;
        }

        return cache.get(ClassUtil.getFriendlyClassName(clazz), new Callable<Method[]>() {
            @Override
            public Method[] call() throws Exception {
                return ReflectionUtil.getAllMethodsOfClass(clazz, true);
            }
        });

    }

    public Method[] getInstanceMethods(final Class<?> clazz) {
        if (clazz == null) {
            return null;
        }

        return cache.get(ClassUtil.getFriendlyClassName(clazz) + ".instance", new Callable<Method[]>() {
            @Override
            public Method[] call() throws Exception {
                return ReflectionUtil.getAllInstanceMethods(clazz, true);
            }
        });
    }

    public Method[] getMethods(final Class<?> clazz, final Class<? extends Annotation> annotationClass) {
        if (clazz == null || annotationClass == null) {
            return null;
        }

        return cache.get(ClassUtil.getFriendlyClassName(clazz) + "." + annotationClass.getName(),
                new Callable<Method[]>() {
                    @Override
                    public Method[] call() throws Exception {
                        return ReflectionUtil.getAnnotationMethods(clazz, annotationClass).toArray(new Method[0]);
                    }
                });
    }

    private static String getMethodSignature(String methodName, Class<?>...params) {
        String name = methodName + (ArrayUtil.isEmpty(params) ? "" : ArrayUtil.toString(params));

        return name;
    }

    public Method getMethod(final Class<?> clazz, String methodName, Class<?>...params) {
        if (clazz == null) {
            return null;
        }

        Map<String, Method> map =
                cachedMap.get(ClassUtil.getFriendlyClassName(clazz), new Callable<Map<String, Method>>() {
                    @Override
                    public Map<String, Method> call() throws Exception {
                        Method[] methods = getMethods(clazz);
                        Map<String, Method> methodMap = CollectionUtil.createHashMap(methods.length);

                        for (Method method : methods) {
                            Class<?>[] types = method.getParameterTypes();
                            String name = getMethodSignature(method.getName(), types);
                            methodMap.put(name, method);
                        }

                        return methodMap;
                    }
                });

        return map.get(getMethodSignature(methodName, params));
    }

    public Method getInstanceMethod(final Class<?> clazz, String methodName, Class<?>...params) {
        if (clazz == null) {
            return null;
        }

        Map<String, Method> map =
                cachedMap.get(ClassUtil.getFriendlyClassName(clazz) + ".instance", new Callable<Map<String, Method>>() {
                    @Override
                    public Map<String, Method> call() throws Exception {
                        Method[] methods = getInstanceMethods(clazz);
                        Map<String, Method> methodMap = CollectionUtil.createHashMap(methods.length);

                        for (Method method : methods) {
                            Class<?>[] types = method.getParameterTypes();
                            String name = getMethodSignature(method.getName(), types);
                            methodMap.put(name, method);
                        }

                        return methodMap;
                    }
                });

        return map.get(getMethodSignature(methodName, params));
    }

    public Method getMethod(final Class<?> clazz, final Class<? extends Annotation> annotationClass, String methodName,
            Class<?>...params) {
        if (clazz == null) {
            return null;
        }

        Map<String, Method> map =
                cachedMap.get(ClassUtil.getFriendlyClassName(clazz) + "." + annotationClass.getName(),
                        new Callable<Map<String, Method>>() {
                            @Override
                            public Map<String, Method> call() throws Exception {
                                Method[] methods = getMethods(clazz, annotationClass);
                                Map<String, Method> methodMap = CollectionUtil.createHashMap(methods.length);

                                for (Method method : methods) {
                                    Class<?>[] types = method.getParameterTypes();
                                    String name = getMethodSignature(method.getName(), types);
                                    methodMap.put(name, method);
                                }

                                return methodMap;
                            }
                        });

        return map.get(getMethodSignature(methodName, params));
    }
}
