package com.amirnadiv.project.utils.common;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Arrays;
import java.util.List;

public abstract class ReflectionUtil {

    // ==========================================================================
    // Method相关的方法。
    // ==========================================================================

    public static Method[] getAllMethodsOfClass(final Class<?> clazz) {
        if (clazz == null) {
            return null;
        }
        Method[] methods = null;
        Class<?> itr = clazz;
        while (itr != null && !itr.equals(Object.class)) {
            methods = (Method[]) ArrayUtil.addAll(itr.getDeclaredMethods(), methods);
            itr = itr.getSuperclass();
        }
        return methods;
    }

    public static Method[] getAllMethodsOfClass(Class<?> clazz, boolean accessible) {
        Method[] methods = getAllMethodsOfClass(clazz);
        if (ArrayUtil.isNotEmpty(methods)) {
            AccessibleObject.setAccessible(methods, accessible);
        }

        return methods;
    }

    public static Method[] getAllInstanceMethods(final Class<?> clazz) {
        if (clazz == null) {
            return null;
        }

        List<Method> methods = CollectionUtil.createArrayList();
        for (Class<?> itr = clazz; hasSuperClass(itr);) {
            for (Method method : itr.getDeclaredMethods()) {
                if (!Modifier.isStatic(method.getModifiers())) {
                    methods.add(method);
                }
            }
            itr = itr.getSuperclass();
        }

        return methods.toArray(new Method[methods.size()]);

    }

    public static Method[] getAllInstanceMethods(Class<?> clazz, boolean accessible) {
        Method[] methods = getAllInstanceMethods(clazz);
        if (ArrayUtil.isNotEmpty(methods)) {
            AccessibleObject.setAccessible(methods, accessible);
        }

        return methods;
    }

    public static <T, A extends Annotation> List<A> findAllMethodAnnotation(Class<T> clazz, Class<A> annotationType) {
        if (clazz == null || annotationType == null) {
            return null;
        }

        List<A> annotations = CollectionUtil.createArrayList();
        Method[] methods = getAllMethodsOfClass(clazz);

        for (Method method : methods) {
            A annotation = method.getAnnotation(annotationType);
            if (annotation != null) {
                annotations.add(annotation);
            }
        }

        return annotations;
    }

    public static <T, A extends Annotation> List<Method> getAnnotationMethods(Class<T> clazz, Class<A> annotationType) {
        if (clazz == null || annotationType == null) {
            return null;
        }
        List<Method> list = CollectionUtil.createArrayList();

        for (Method method : getAllMethodsOfClass(clazz)) {
            A type = method.getAnnotation(annotationType);
            if (type != null) {
                list.add(method);
            }
        }

        return list;
    }

    public static Object invokeMethod(Method method, Object target, Object...args) {
        if (method == null) {
            return null;
        }

        method.setAccessible(true);
        try {
            return method.invoke(target, args);
        } catch (Exception ex) {
            throw ExceptionUtil.toRuntimeException(ex);
        }

    }

    public static Object invokeMethod(Object object, String methodName, Object[] args, Class<?>...parameterTypes) {
        if (object == null || StringUtil.isEmpty(methodName)) {
            return null;
        }

        if (parameterTypes == null) {
            parameterTypes = Emptys.EMPTY_CLASS_ARRAY;
        }
        if (args == null) {
            args = Emptys.EMPTY_OBJECT_ARRAY;
        }
        Method method;
        try {
            method = object.getClass().getDeclaredMethod(methodName, parameterTypes);
        } catch (Exception ex) {
            throw ExceptionUtil.toRuntimeException(ex);
        }
        if (method == null) {
            return null;
        }

        return invokeMethod(method, object, args);

    }

    public static Object invokeStaticMethod(Class<?> clazz, String methodName, Object[] args, Class<?>...parameterTypes) {
        if (parameterTypes == null) {
            parameterTypes = Emptys.EMPTY_CLASS_ARRAY;
        }
        if (args == null) {
            args = Emptys.EMPTY_OBJECT_ARRAY;
        }
        Method method;
        try {
            method = clazz.getDeclaredMethod(methodName, parameterTypes);
        } catch (Exception ex) {
            throw ExceptionUtil.toRuntimeException(ex);
        }
        if (method == null) {
            return null;
        }

        return invokeMethod(method, null, args);
    }

    public static Method getMethod(Class<?> clazz, String methodName, Class<?>...parameterTypes) {
        if (clazz == null || StringUtil.isBlank(methodName)) {
            return null;
        }

        for (Class<?> itr = clazz; hasSuperClass(itr);) {
            Method[] methods = itr.getDeclaredMethods();

            for (Method method : methods) {
                if (method.getName().equals(methodName) && Arrays.equals(method.getParameterTypes(), parameterTypes)) {
                    return method;
                }
            }

            itr = itr.getSuperclass();
        }

        return null;

    }

    public static Method getMethod(Object object, String methodName, Class<?>...parameterTypes) {
        if (object == null || StringUtil.isBlank(methodName)) {
            return null;
        }

        for (Class<?> itr = object.getClass(); hasSuperClass(itr);) {
            Method[] methods = itr.getDeclaredMethods();

            for (Method method : methods) {
                if (method.getName().equals(methodName) && Arrays.equals(method.getParameterTypes(), parameterTypes)) {
                    return method;
                }
            }

            itr = itr.getSuperclass();
        }

        return null;

    }

    public static Method[] getAccessibleMethods(Class<?> clazz) {
        return getAccessibleMethods(clazz, Object.class);
    }

    public static Method[] getAccessibleMethods(Class<?> clazz, Class<?> limit) {
        Package topPackage = clazz.getPackage();
        List<Method> methodList = CollectionUtil.createArrayList();
        int topPackageHash = (topPackage == null) ? 0 : topPackage.hashCode();
        boolean top = true;
        do {
            if (clazz == null) {
                break;
            }
            Method[] declaredMethods = clazz.getDeclaredMethods();
            for (Method method : declaredMethods) {
                if (Modifier.isVolatile(method.getModifiers())) {
                    continue;
                }
                if (top) {
                    methodList.add(method);
                    continue;
                }
                int modifier = method.getModifiers();
                if (Modifier.isPrivate(modifier) || Modifier.isAbstract(modifier)) {
                    continue;
                }

                if (Modifier.isPublic(modifier) || Modifier.isProtected(modifier)) {
                    addMethodIfNotExist(methodList, method);
                    continue;
                }
                // add super default methods from the same package
                Package pckg = method.getDeclaringClass().getPackage();
                int pckgHash = (pckg == null) ? 0 : pckg.hashCode();
                if (pckgHash == topPackageHash) {
                    addMethodIfNotExist(methodList, method);
                }
            }
            top = false;
        } while ((clazz = clazz.getSuperclass()) != limit);

        Method[] methods = new Method[methodList.size()];
        for (int i = 0; i < methods.length; i++) {
            methods[i] = methodList.get(i);
        }
        return methods;
    }

    private static void addMethodIfNotExist(List<Method> allMethods, Method newMethod) {
        for (Method method : allMethods) {
            if (ObjectUtil.isEquals(method, newMethod)) {
                return;
            }
        }

        allMethods.add(newMethod);
    }

    public static <A extends Annotation> boolean hasAnnotation(Method method, Class<A> annotationType) {
        if (ObjectUtil.isAnyNull(method, annotationType)) {
            return false;
        }

        return method.getAnnotation(annotationType) != null;
    }

    // ==========================================================================
    // Field相关的方法。
    // ==========================================================================

    public static Field[] getAllFieldsOfClass(Class<?> clazz) {
        if (clazz == null) {
            return null;
        }

        return _getAllFieldsOfClass(clazz);
    }

    public static Field[] getAllInstanceFields(Class<?> clazz) {
        if (clazz == null) {
            return null;
        }

        return _getAllInstanceFields(clazz);
    }

    static Field[] _getAllInstanceFields(Class<?> clazz) {
        List<Field> fields = CollectionUtil.createArrayList();
        for (Class<?> itr = clazz; hasSuperClass(itr);) {
            for (Field field : itr.getDeclaredFields()) {
                if (!Modifier.isStatic(field.getModifiers())) {
                    fields.add(field);
                }
            }
            itr = itr.getSuperclass();
        }

        return fields.toArray(new Field[fields.size()]);
    }

    static Field[] _getAllFieldsOfClass(Class<?> clazz) {
        Field[] fields = null;

        for (Class<?> itr = clazz; hasSuperClass(itr);) {
            fields = (Field[]) ArrayUtil.addAll(itr.getDeclaredFields(), fields);
            itr = itr.getSuperclass();
        }

        return fields;
    }

    public static Field[] getAllFieldsOfClass(Class<?> clazz, boolean accessible) {
        Field[] fields = getAllFieldsOfClass(clazz);
        if (ArrayUtil.isNotEmpty(fields)) {
            AccessibleObject.setAccessible(fields, accessible);
        }

        return fields;
    }

    public static Field[] getAllFieldsOfClass(Object object) {
        if (object == null) {
            return null;
        }

        Field[] fields = _getAllFieldsOfClass(object.getClass());
        return fields;
    }

    public static Field[] getAllFieldsOfClass(Object object, boolean accessible) {
        if (object == null) {
            return null;
        }

        Field[] fields = getAllFieldsOfClass(object);
        AccessibleObject.setAccessible(fields, accessible);
        return fields;
    }

    public static Field[] getAllInstanceFields(Class<?> clazz, boolean accessible) {
        Field[] fields = getAllInstanceFields(clazz);
        if (ArrayUtil.isNotEmpty(fields)) {
            AccessibleObject.setAccessible(fields, accessible);
        }

        return fields;
    }

    public static Field[] getAllInstanceFields(Object object) {
        if (object == null) {
            return null;
        }

        Field[] fields = _getAllInstanceFields(object.getClass());
        return fields;
    }

    public static Field[] getAllInstanceFields(Object object, boolean accessible) {
        if (object == null) {
            return null;
        }

        Field[] fields = getAllInstanceFields(object);
        AccessibleObject.setAccessible(fields, accessible);
        return fields;
    }

    // FIXME
    public static Field[] getInstanceFields(Class<?> clazz, String[] includes) {
        if (clazz == null) {
            return null;
        }

        return _getInstanceFields(clazz, includes);
    }

    static Field[] _getInstanceFields(Class<?> clazz, String[] includes) {
        List<Field> fields = CollectionUtil.createArrayList();
        for (Class<?> itr = clazz; hasSuperClass(itr);) {
            for (Field field : itr.getDeclaredFields()) {
                if (!Modifier.isStatic(field.getModifiers()) && ArrayUtil.contains(includes, field.getName())) {
                    fields.add(field);
                }
            }
            itr = itr.getSuperclass();
        }

        return fields.toArray(new Field[fields.size()]);
    }

    public static Field[] getAnnotationFields(Class<?> clazz, Class<? extends Annotation> annotationClass) {
        if (clazz == null || annotationClass == null) {
            return null;
        }

        Field[] fields = _getAllFieldsOfClass(clazz);
        if (ArrayUtil.isEmpty(fields)) {
            return null;
        }

        List<Field> list = CollectionUtil.createArrayList();
        for (Field field : fields) {
            if (null != field.getAnnotation(annotationClass)) {
                list.add(field);
                field.setAccessible(true);
            }
        }

        return list.toArray(new Field[0]);
    }

    public static Field getField(Object object, String fieldName) {
        if (ObjectUtil.isAnyNull(object, fieldName)) {
            return null;
        }

        return _getField(object.getClass(), fieldName);
    }

    public static Field getField(Class<?> clazz, String fieldName) {
        if (ObjectUtil.isAnyNull(clazz, fieldName)) {
            return null;
        }

        return _getField(clazz, fieldName);
    }

    static Field _getField(Class<?> clazz, String fieldName) {
        for (Class<?> itr = clazz; hasSuperClass(itr);) {
            Field[] fields = itr.getDeclaredFields();
            for (Field field : fields) {
                if (field.getName().equals(fieldName)) {
                    return field;
                }
            }

            itr = itr.getSuperclass();
        }

        return null;
    }

    @Deprecated
    public static Field getFieldOfClass(Object object, String fieldName) {
        if (ObjectUtil.isAnyNull(object, fieldName)) {
            return null;
        }

        return _getFieldOfClass(object.getClass(), fieldName);
    }

    public static Field getField(Object object, String fieldName, boolean accessible) {
        Field field = getField(object, fieldName);
        if (field != null) {
            field.setAccessible(accessible);
        }

        return field;
    }

    public static Field getField(Class<?> clazz, String fieldName, boolean accessible) {
        Field field = getField(clazz, fieldName);
        if (field != null) {
            field.setAccessible(accessible);
        }

        return field;
    }

    @Deprecated
    public static Field getFieldOfClass(Class<?> clazz, String fieldName) {
        if (ObjectUtil.isAnyNull(clazz, fieldName)) {
            return null;
        }

        return _getFieldOfClass(clazz, fieldName);
    }

    @Deprecated
    public static Field getFieldOfClass(Object object, String fieldName, boolean accessible) {
        Field field = getFieldOfClass(object, fieldName);
        if (field != null) {
            field.setAccessible(accessible);
        }

        return field;
    }

    @Deprecated
    static Field _getFieldOfClass(Class<?> clazz, String fieldName) {
        Field[] fields = _getAllFieldsOfClass(clazz);
        for (Field field : fields) {
            if (field.getName().equals(fieldName))
                return field;
        }

        return null;
    }

    public static Field[] getAccessibleFields(Class<?> clazz) {
        return getAccessibleFields(clazz, Object.class);
    }

    public static Field[] getAccessibleFields(Class<?> clazz, Class<?> limit) {
        if (clazz == null) {
            return null;
        }

        Package topPackage = clazz.getPackage();
        List<Field> fieldList = CollectionUtil.createArrayList();
        int topPackageHash = (topPackage == null) ? 0 : topPackage.hashCode();
        boolean top = true;
        do {
            if (clazz == null) {
                break;
            }
            Field[] declaredFields = clazz.getDeclaredFields();
            for (Field field : declaredFields) {
                if (top == true) { // add all top declared fields
                    fieldList.add(field);
                    continue;
                }
                int modifier = field.getModifiers();
                if (Modifier.isPrivate(modifier)) {
                    continue;
                }
                if (Modifier.isPublic(modifier) || Modifier.isProtected(modifier)) {
                    addFieldIfNotExist(fieldList, field);
                    continue;
                }

                // add super default methods from the same package
                Package pckg = field.getDeclaringClass().getPackage();
                int pckgHash = (pckg == null) ? 0 : pckg.hashCode();
                if (pckgHash == topPackageHash) {
                    addFieldIfNotExist(fieldList, field);
                }
            }
            top = false;
        } while ((clazz = clazz.getSuperclass()) != limit);

        Field[] fields = new Field[fieldList.size()];
        for (int i = 0; i < fields.length; i++) {
            fields[i] = fieldList.get(i);
        }

        return fields;
    }

    private static void addFieldIfNotExist(List<Field> allFields, Field newField) {
        for (Field field : allFields) {
            if (ObjectUtil.isEquals(field, newField)) {
                return;
            }
        }

        allFields.add(newField);
    }

    public static <T> T readField(Field field, Object target) {
        if (field == null || target == null) {
            return null;
        }

        try {
            @SuppressWarnings("unchecked")
            T result = (T) _readField(field, target);

            return result;
        } catch (Exception ex) {
            throw ExceptionUtil.toRuntimeException(ex);
        }
    }

    static Object _readField(Field field, Object target) throws IllegalArgumentException, IllegalAccessException {
        if (!field.isAccessible()) {
            field.setAccessible(true);
        }

        return field.get(target);
    }

    public static Object readField(String fieldName, Object target) {
        Field field = getField(target, fieldName);
        if (field == null) {
            return null;
        }

        try {
            return _readField(field, target);
        } catch (Exception ex) {
            throw ExceptionUtil.toRuntimeException(ex);
        }
    }

    public static void writeField(Field field, Object target, Object value) {
        if (ObjectUtil.isAllNull(field, target, value)) {
            return;
        }

        try {
            _writeField(field, target, value);
        } catch (Exception ex) {
            throw ExceptionUtil.toRuntimeException(ex);
        }
    }

    static void _writeField(Field field, Object target, Object value) throws IllegalArgumentException,
            IllegalAccessException {
        if (!field.isAccessible()) {
            field.setAccessible(true);
        }

        field.set(target, value);
    }

    public static void writeField(Object target, String fieldName, Object value) {
        Field field = getField(target, fieldName);
        if (field != null)
            try {
                _writeField(field, target, value);
            } catch (Exception ex) {
                throw ExceptionUtil.toRuntimeException(ex);
            }
    }

    public static Object readStaticField(Field field) {
        if (field == null) {
            return null;
        }

        Assert.assertTrue(Modifier.isStatic(field.getModifiers()), "The field '{}' is not static", field.getName());

        return readField(field, (Object) null);
    }

    public static Object readStaticField(Class<?> clazz, String fieldName) {
        Field field = getField(clazz, fieldName);
        if (field == null) {
            return null;
        }

        return readStaticField(field);
    }

    public static void writeStaticField(Field field, Object value) {
        if (field == null) {
            return;
        }

        Assert.assertTrue(Modifier.isStatic(field.getModifiers()), "The field '{}' is not static", field.getName());

        writeField(field, (Object) null, value);
    }

    public static void writeStaticField(Class<?> clazz, String fieldName, Object value) {
        Field field = getField(clazz, fieldName);
        if (field == null) {
            return;
        }

        writeStaticField(field, value);
    }

    public static boolean notWriter(Field field) {
        return field == null || Modifier.isFinal(field.getModifiers()) || Modifier.isStatic(field.getModifiers());
    }

    public static boolean isFinal(Field field) {
        return field != null && Modifier.isFinal(field.getModifiers());
    }

    public static boolean hasSuperClass(Class<?> clazz) {
        return (clazz != null) && !clazz.equals(Object.class);
    }

    public static <A extends Annotation> boolean hasAnnotation(Field field, Class<A> annotationType) {
        if (ObjectUtil.isAnyNull(field, annotationType)) {
            return false;
        }

        return field.getAnnotation(annotationType) != null;
    }

    // ==========================================================================
    // Constructor相关的方法。
    // ==========================================================================

    // TODO

    // ==========================================================================
    // 泛型相关的方法。
    // ==========================================================================
    public static Class<?> getComponentType(Class<?> clazz) {
        if (clazz == null) {
            return null;
        }

        return getComponentType(clazz.getGenericSuperclass(), null);
    }

    public static Class<?> getComponentType(Class<?> clazz, int index) {
        if (clazz == null) {
            return null;
        }

        Class<?>[] classes = getComponentTypes(clazz.getGenericSuperclass(), null);
        return classes[index];
    }

    public static Class<?>[] getComponentTypes(Class<?> clazz) {
        if (clazz == null) {
            return null;
        }

        return getComponentTypes(clazz.getGenericSuperclass(), null);
    }

    public static Class<?>[] getComponentTypesRecursion(Class<?> clazz) {
        if (clazz == null) {
            return null;
        }

        Class<?>[] classes = getComponentTypes(clazz.getGenericSuperclass(), null);
        if (ArrayUtil.isNotEmpty(classes)) {
            return classes;
        }

        return getComponentTypesRecursion(clazz.getSuperclass());
    }

    public static Class<?> getComponentTypeRecursion(Class<?> clazz) {
        if (clazz == null) {
            return null;
        }

        Class<?>[] classes = getComponentTypes(clazz.getGenericSuperclass(), null);
        if (ArrayUtil.isNotEmpty(classes)) {
            return classes[classes.length - 1];
        }

        return getComponentTypeRecursion(clazz.getSuperclass());
    }

    public static Class<?> getComponentTypeRecursion(Class<?> clazz, int index) {
        if (clazz == null) {
            return null;
        }

        Class<?>[] classes = getComponentTypes(clazz.getGenericSuperclass(), null);
        if (ArrayUtil.isNotEmpty(classes)) {
            return classes[index];
        }

        return getComponentTypeRecursion(clazz.getSuperclass(), index);
    }

    public static Class<?> getComponentType(Type type) {
        return getComponentType(type, null);
    }

    public static Class<?> getComponentType(Type type, Class<?> implClass) {
        Class<?>[] componentTypes = getComponentTypes(type, implClass);
        if (componentTypes == null) {
            return null;
        }
        return componentTypes[componentTypes.length - 1];
    }

    public static Class<?>[] getComponentTypes(Type type) {
        return getComponentTypes(type, null);
    }

    public static Class<?>[] getComponentTypes(Type type, Class<?> implClass) {
        if (type == null) {
            return null;
        }

        GenericType gt = GenericType.find(type);
        if (gt != null) {
            return gt.getComponentTypes(type, implClass);
        }

        return null;

    }

    public static Class<?>[] getGenericSuperTypes(Class<?> type) {
        if (type == null) {
            return null;
        }

        return getComponentTypes(type.getGenericSuperclass());
    }

    public static Class<?> getGenericSuperType(Class<?> type) {
        if (type == null) {
            return null;
        }

        Class<?>[] componentTypes = getComponentTypes(type.getGenericSuperclass());

        if (componentTypes == null) {
            return null;
        }

        return componentTypes[0];
    }

    public static Class<?> getRawType(Type type) {
        return getRawType(type, null);
    }

    public static Class<?> getRawType(Type type, Class<?> implClass) {
        if (type == null) {
            return null;
        }

        GenericType gt = GenericType.find(type);
        if (gt != null) {
            return gt.toRawType(type, implClass);
        }

        return null;

    }

    enum GenericType {

        CLASS_TYPE {

            @Override
            Class<?> type() {
                return Class.class;
            }

            @Override
            Class<?> toRawType(Type type, Class<?> implClass) {
                return (Class<?>) type;
            }

            @Override
            Class<?>[] getComponentTypes(Type type, Class<?> implClass) {
                Class<?> clazz = (Class<?>) type;
                if (clazz.isArray()) {
                    return new Class[] { clazz.getComponentType() };
                }
                return null;
            }
        },
        PARAMETERIZED_TYPE {

            @Override
            Class<?> type() {
                return ParameterizedType.class;
            }

            @Override
            Class<?> toRawType(Type type, Class<?> implClass) {
                ParameterizedType pType = (ParameterizedType) type;
                return getRawType(pType.getRawType(), implClass);
            }

            @Override
            Class<?>[] getComponentTypes(Type type, Class<?> implClass) {
                ParameterizedType pt = (ParameterizedType) type;

                Type[] generics = pt.getActualTypeArguments();

                if (generics.length == 0) {
                    return null;
                }

                Class<?>[] types = new Class[generics.length];

                for (int i = 0; i < generics.length; i++) {
                    types[i] = getRawType(generics[i], implClass);
                }
                return types;
            }
        },
        WILDCARD_TYPE {

            @Override
            Class<?> type() {
                return WildcardType.class;
            }

            @Override
            Class<?> toRawType(Type type, Class<?> implClass) {
                WildcardType wType = (WildcardType) type;

                Type[] lowerTypes = wType.getLowerBounds();
                if (lowerTypes.length > 0) {
                    return getRawType(lowerTypes[0], implClass);
                }

                Type[] upperTypes = wType.getUpperBounds();
                if (upperTypes.length != 0) {
                    return getRawType(upperTypes[0], implClass);
                }

                return Object.class;
            }

            @Override
            Class<?>[] getComponentTypes(Type type, Class<?> implClass) {
                return null;
            }
        },
        GENERIC_ARRAY_TYPE {

            @Override
            Class<?> type() {
                return GenericArrayType.class;
            }

            @Override
            Class<?> toRawType(Type type, Class<?> implClass) {
                Type genericComponentType = ((GenericArrayType) type).getGenericComponentType();
                Class<?> rawType = getRawType(genericComponentType, implClass);
                // FIXME
                return Array.newInstance(rawType, 0).getClass();
            }

            @Override
            Class<?>[] getComponentTypes(Type type, Class<?> implClass) {
                GenericArrayType gat = (GenericArrayType) type;

                Class<?> rawType = getRawType(gat.getGenericComponentType(), implClass);
                if (rawType == null) {
                    return null;
                }

                return new Class[] { rawType };
            }
        },
        TYPE_VARIABLE {

            @Override
            Class<?> type() {
                return TypeVariable.class;
            }

            @Override
            Class<?> toRawType(Type type, Class<?> implClass) {
                TypeVariable<?> varType = (TypeVariable<?>) type;
                if (implClass != null) {
                    Type resolvedType = resolveVariable(varType, implClass);
                    if (resolvedType != null) {
                        return getRawType(resolvedType, null);
                    }
                }
                Type[] boundsTypes = varType.getBounds();
                if (boundsTypes.length == 0) {
                    return Object.class;
                }
                return getRawType(boundsTypes[0], implClass);
            }

            @Override
            Class<?>[] getComponentTypes(Type type, Class<?> implClass) {
                return null;
            }
        };

        abstract Class<?> toRawType(Type type, Class<?> implClass);

        abstract Class<?> type();

        abstract Class<?>[] getComponentTypes(Type type, Class<?> implClass);

        static GenericType find(Type type) {
            for (GenericType gt : GenericType.values()) {
                if (ClassUtil.isInstance(gt.type(), type)) {
                    return gt;
                }
            }

            return null;
        }
    }

    public static Type resolveVariable(TypeVariable<?> variable, final Class<?> implClass) {
        final Class<?> rawType = getRawType(implClass, null);

        int index = ArrayUtil.indexOf(rawType.getTypeParameters(), variable);
        if (index >= 0) {
            return variable;
        }

        final Class<?>[] interfaces = rawType.getInterfaces();
        final Type[] genericInterfaces = rawType.getGenericInterfaces();

        for (int i = 0; i <= interfaces.length; i++) {
            Class<?> rawInterface;

            if (i < interfaces.length) {
                rawInterface = interfaces[i];
            } else {
                rawInterface = rawType.getSuperclass();
                if (rawInterface == null) {
                    continue;
                }
            }

            final Type resolved = resolveVariable(variable, rawInterface);
            if (resolved instanceof Class || resolved instanceof ParameterizedType) {
                return resolved;
            }

            if (resolved instanceof TypeVariable) {
                final TypeVariable<?> typeVariable = (TypeVariable<?>) resolved;
                index = ArrayUtil.indexOf(rawInterface.getTypeParameters(), typeVariable);

                if (index < 0) {
                    throw new IllegalArgumentException("Invalid type variable:" + typeVariable);
                }

                final Type type = i < genericInterfaces.length ? genericInterfaces[i] : rawType.getGenericSuperclass();

                if (type instanceof Class) {
                    return Object.class;
                }

                if (type instanceof ParameterizedType) {
                    return ((ParameterizedType) type).getActualTypeArguments()[index];
                }

                throw new IllegalArgumentException("Unsupported type: " + type);
            }
        }
        return null;
    }

    // ==========================================================================
    // 辅助方法。
    // ==========================================================================

    private static final Method IS_SYNTHETIC;
    static {
        Method isSynthetic = null;
        if (SystemUtil.getJavaInfo().isJavaVersionAtLeast(1.5f)) {
            // cannot call synthetic methods:
            try {
                isSynthetic = Member.class.getMethod("isSynthetic", Emptys.EMPTY_CLASS_ARRAY);
            } catch (Exception e) {
                // ignore
            }
        }
        IS_SYNTHETIC = isSynthetic;
    }

    public static boolean isAccessible(Member m) {
        return m != null && Modifier.isPublic(m.getModifiers()) && !isSynthetic(m);
    }

    static boolean isSynthetic(Member m) {
        if (IS_SYNTHETIC != null) {
            try {
                return ((Boolean) IS_SYNTHETIC.invoke(m, (Object[]) null)).booleanValue();
            } catch (Exception e) {
            }
        }
        return false;
    }

    public static boolean isPublic(Member m) {
        return m != null && Modifier.isPublic(m.getModifiers());
    }

    public static void forceAccess(AccessibleObject object) {
        if (object == null || object.isAccessible()) {
            return;
        }
        try {
            object.setAccessible(true);
        } catch (SecurityException e) {
            throw ExceptionUtil.toRuntimeException(e);
        }
    }

}
