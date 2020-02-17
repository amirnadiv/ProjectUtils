package com.amirnadiv.project.utils.common.cache;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.Callable;

import com.amirnadiv.project.utils.common.ClassUtil;
import com.amirnadiv.project.utils.common.CollectionUtil;
import com.amirnadiv.project.utils.common.ReflectionUtil;
import com.amirnadiv.project.utils.common.able.Computable;

public class FieldCache {

    private static final FieldCache instance = new FieldCache();

    private final Computable<String, Field[]> cache = new ConcurrentCache<String, Field[]>();

    private final Computable<String, Map<String, Field>> cachedMap = new ConcurrentCache<String, Map<String, Field>>();

    private FieldCache() {

    }

    public static FieldCache getInstance() {
        return instance;
    }

    public Field[] getFields(final Class<?> clazz) {
        if (clazz == null) {
            return null;
        }

        return cache.get(ClassUtil.getFriendlyClassName(clazz), new Callable<Field[]>() {
            @Override
            public Field[] call() throws Exception {
                return ReflectionUtil.getAllFieldsOfClass(clazz, true);
            }
        });

    }

    public Field[] getInstanceFields(final Class<?> clazz) {
        if (clazz == null) {
            return null;
        }

        return cache.get(ClassUtil.getFriendlyClassName(clazz) + ".instance", new Callable<Field[]>() {
            @Override
            public Field[] call() throws Exception {
                return ReflectionUtil.getAllInstanceFields(clazz, true);
            }
        });
    }

    public Field[] getFields(final Class<?> clazz, final Class<? extends Annotation> annotationClass) {
        if (clazz == null || annotationClass == null) {
            return null;
        }

        return cache.get(ClassUtil.getFriendlyClassName(clazz) + "." + annotationClass.getName(),
                new Callable<Field[]>() {
                    @Override
                    public Field[] call() throws Exception {
                        return ReflectionUtil.getAnnotationFields(clazz, annotationClass);
                    }
                });
    }

    public Field getField(final Class<?> clazz, String fieldName) {
        if (clazz == null) {
            return null;
        }

        Map<String, Field> map =
                cachedMap.get(ClassUtil.getFriendlyClassName(clazz), new Callable<Map<String, Field>>() {
                    @Override
                    public Map<String, Field> call() throws Exception {
                        Field[] fields = getFields(clazz);
                        Map<String, Field> fieldMap = CollectionUtil.createHashMap(fields.length);

                        for (Field field : fields) {
                            fieldMap.put(field.getName(), field);
                        }

                        return fieldMap;
                    }
                });

        return map.get(fieldName);
    }

    public Field getInstanceField(final Class<?> clazz, String fieldName) {
        if (clazz == null) {
            return null;
        }

        Map<String, Field> map =
                cachedMap.get(ClassUtil.getFriendlyClassName(clazz) + ".instance", new Callable<Map<String, Field>>() {
                    @Override
                    public Map<String, Field> call() throws Exception {
                        Field[] fields = getInstanceFields(clazz);
                        Map<String, Field> fieldMap = CollectionUtil.createHashMap(fields.length);

                        for (Field field : fields) {
                            fieldMap.put(field.getName(), field);
                        }

                        return fieldMap;
                    }
                });

        return map.get(fieldName);
    }

    public Field getField(final Class<?> clazz, final Class<? extends Annotation> annotationClass, String fieldName) {
        if (clazz == null) {
            return null;
        }

        Map<String, Field> map =
                cachedMap.get(ClassUtil.getFriendlyClassName(clazz) + "." + annotationClass.getName(),
                        new Callable<Map<String, Field>>() {
                            @Override
                            public Map<String, Field> call() throws Exception {
                                Field[] fields = getFields(clazz, annotationClass);
                                Map<String, Field> fieldMap = CollectionUtil.createHashMap(fields.length);

                                for (Field field : fields) {
                                    fieldMap.put(field.getName(), field);
                                }

                                return fieldMap;
                            }
                        });

        return map.get(fieldName);
    }
}
