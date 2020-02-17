package com.amirnadiv.project.utils.common.bean;

import java.beans.BeanInfo;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.amirnadiv.project.utils.common.ClassUtil;
import com.amirnadiv.project.utils.common.ObjectUtil;
import com.amirnadiv.project.utils.common.ReflectionUtil;
import com.amirnadiv.project.utils.common.cache.BeanInfoCache;
import com.amirnadiv.project.utils.common.cache.FieldCache;

public abstract class BeanCopy {

    private static final FieldCache fieldCache = FieldCache.getInstance();

    private static final BeanInfoCache beanInfoCache = BeanInfoCache.getInstance();

    public static void copy(Object src, Object dest, boolean useAnnotation) {
        if (!useAnnotation) {
            copyProperties(src, dest);
            return;
        }

        copyByAnnotation(src, dest);
    }

    public static void copyProperties(Object src, Object dest) {
        if (ObjectUtil.isAnyNull(src, dest)) {
            return;
        }

        Class<?> srcClazz = src.getClass();
        Field[] destFields = fieldCache.getInstanceFields(dest.getClass());

        for (Field field : destFields) {
            if (ReflectionUtil.isFinal(field)) {
                continue;
            }
            Field srcField = fieldCache.getInstanceField(srcClazz, field.getName());
            if (srcField != null) {
                Object value = ReflectionUtil.readField(srcField, src);
                ReflectionUtil.writeField(field, dest, value);
            }

        }
    }

    public static void copyByAnnotation(Object src, Object dest) {
        if (ObjectUtil.isAnyNull(src, dest)) {
            return;
        }

        Class<?> srcClazz = src.getClass();
        Class<?> destClazz = dest.getClass();
        Field[] destFields = fieldCache.getInstanceFields(dest.getClass());

        for (Field field : destFields) {
            if (ReflectionUtil.isFinal(field) || ReflectionUtil.hasAnnotation(field, IgnoreField.class)) {
                continue;
            }
            Field srcField = fieldCache.getField(srcClazz, CopyField.class, field.getName());
            if (srcField != null && supportFor(destClazz, srcField.getAnnotation(CopyField.class))) {
                Object value = ReflectionUtil.readField(srcField, src);
                ReflectionUtil.writeField(field, dest, value);
            }

        }

    }

    public static void copyByMethod(Object src, Object dest) {
        if (ObjectUtil.isAnyNull(src, dest)) {
            return;
        }

        BeanInfo beanInfo = beanInfoCache.getBeanInfo(dest.getClass());

        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();

        for (PropertyDescriptor destPd : propertyDescriptors) {
            PropertyDescriptor srcPd = beanInfoCache.getPropertyDescriptor(src.getClass(), destPd.getName());
            if (srcPd == null) {
                continue;
            }

            Method readMethod = srcPd.getReadMethod();
            Object value = ReflectionUtil.invokeMethod(readMethod, src);
            Method writeMethod = destPd.getWriteMethod();
            ReflectionUtil.invokeMethod(writeMethod, dest, value);

        }
    }

    private static boolean supportFor(Class<?> fromClass, CopyField copyField) {
        for (Class<?> clazz : copyField.supportFor()) {
            if (ClassUtil.isAssignable(clazz, fromClass)) {
                return true;
            }
        }

        return false;
    }
}
