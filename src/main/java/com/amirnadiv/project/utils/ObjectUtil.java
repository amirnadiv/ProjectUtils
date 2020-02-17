package com.amirnadiv.project.utils.common;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public abstract class ObjectUtil {

    public static final String SERIAL_VERSION_UID = "serialVersionUID";


    public static <T, S extends T> T defaultIfNull(T object, S defaultValue) {
        return object == null ? defaultValue : object;
    }



    public static boolean isEquals(Object object1, Object object2) {
        if (object1 == object2) {
            return true;
        }

        if (object1 == null || object2 == null) {
            return false;
        }

        if (!object1.getClass().equals(object2.getClass())) {
            return false;
        }

        if (object1 instanceof Object[]) {
            return Arrays.deepEquals((Object[]) object1, (Object[]) object2);
        }
        if (object1 instanceof int[]) {
            return Arrays.equals((int[]) object1, (int[]) object2);
        }
        if (object1 instanceof long[]) {
            return Arrays.equals((long[]) object1, (long[]) object2);
        }
        if (object1 instanceof short[]) {
            return Arrays.equals((short[]) object1, (short[]) object2);
        }
        if (object1 instanceof byte[]) {
            return Arrays.equals((byte[]) object1, (byte[]) object2);
        }
        if (object1 instanceof double[]) {
            return Arrays.equals((double[]) object1, (double[]) object2);
        }
        if (object1 instanceof float[]) {
            return Arrays.equals((float[]) object1, (float[]) object2);
        }
        if (object1 instanceof char[]) {
            return Arrays.equals((char[]) object1, (char[]) object2);
        }
        if (object1 instanceof boolean[]) {
            return Arrays.equals((boolean[]) object1, (boolean[]) object2);
        }
        return object1.equals(object2);
    }



    public static int hashCode(Object object) {
        if (object == null) {
            return 0;
        } else if (object instanceof Object[]) {
            return Arrays.deepHashCode((Object[]) object);
        } else if (object instanceof int[]) {
            return Arrays.hashCode((int[]) object);
        } else if (object instanceof long[]) {
            return Arrays.hashCode((long[]) object);
        } else if (object instanceof short[]) {
            return Arrays.hashCode((short[]) object);
        } else if (object instanceof byte[]) {
            return Arrays.hashCode((byte[]) object);
        } else if (object instanceof double[]) {
            return Arrays.hashCode((double[]) object);
        } else if (object instanceof float[]) {
            return Arrays.hashCode((float[]) object);
        } else if (object instanceof char[]) {
            return Arrays.hashCode((char[]) object);
        } else if (object instanceof boolean[]) {
            return Arrays.hashCode((boolean[]) object);
        } else {
            return object.hashCode();
        }
    }



    public static int identityHashCode(Object object) {
        return object == null ? 0 : System.identityHashCode(object);
    }

    public static String identityToString(Object object) {
        if (object == null) {
            return null;
        }

        return appendIdentityToString(new StringBuilder(), object).toString();
    }

    public static String identityToString(Object object, String nullStr) {
        if (object == null) {
            return nullStr;
        }

        return appendIdentityToString(new StringBuilder(), object).toString();
    }

    public static <A extends Appendable> A appendIdentityToString(A buffer, Object object) {
        Assert.assertNotNull(buffer, "appendable");

        try {
            if (object == null) {
                buffer.append("null");
            } else {
                buffer.append(ClassUtil.getFriendlyClassNameForObject(object));
                buffer.append('@').append(Integer.toHexString(identityHashCode(object)));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return buffer;
    }



    public static String toString(Object object) {
        return toString(object, Emptys.EMPTY_STRING);
    }

    public static String toString(Object object, String nullStr) {
        if (object == null) {
            return nullStr;
        } else if (object instanceof Object[]) {
            return Arrays.deepToString((Object[]) object);
        } else if (object instanceof int[]) {
            return Arrays.toString((int[]) object);
        } else if (object instanceof long[]) {
            return Arrays.toString((long[]) object);
        } else if (object instanceof short[]) {
            return Arrays.toString((short[]) object);
        } else if (object instanceof byte[]) {
            return Arrays.toString((byte[]) object);
        } else if (object instanceof double[]) {
            return Arrays.toString((double[]) object);
        } else if (object instanceof float[]) {
            return Arrays.toString((float[]) object);
        } else if (object instanceof char[]) {
            return Arrays.toString((char[]) object);
        } else if (object instanceof boolean[]) {
            return Arrays.toString((boolean[]) object);
        } else {
            return object.toString();
        }
    }



    public static boolean isSameType(Object object1, Object object2) {
        if ((object1 == null) || (object2 == null)) {
            return true;
        }

        return object1.getClass().equals(object2.getClass());
    }



    public static boolean isEmpty(Object object) {
        if (object == null) {
            return true;
        }
        if (object instanceof String) {
            return StringUtil.isEmpty(object.toString());
        }
        if (object.getClass().isArray()) {
            return Array.getLength(object) == 0;
        }

        return false;
    }

    public static boolean isNotEmpty(Object object) {
        return !isEmpty(object);
    }

    public static boolean isNull(Object object) {
        return (object == null);
    }

    public static boolean isNotNull(Object object) {
        return (object != null);
    }

    public static boolean isAllNull(Object...objects) {
        if (objects == null) {
            return true;
        }

        for (Object object : objects) {
            if (object != null) {
                return false;
            }
        }
        return true;
    }

    public static boolean isAnyNull(Object...objects) {
        if (objects == null) {
            return true;
        }

        for (Object object : objects) {
            if (object == null) {
                return true;
            }
        }
        return false;
    }

    public static int nullNum(Object...objects) {
        if (objects == null) {
            return -1;
        }

        int result = 0;
        for (Object object : objects) {
            if (object == null) {
                result++;
            }
        }
        return result;
    }

    public static StringBuilder appendIdentityToString(StringBuilder builder, Object object) {
        if (object == null) {
            return null;
        }

        if (builder == null) {
            builder = new StringBuilder();
        }
        // FIXME
        builder.append(ClassUtil.getSimpleClassNameForObject(object));

        return builder.append('@').append(Integer.toHexString(identityHashCode(object)));
    }

    public static StringBuffer appendIdentityToString(StringBuffer buffer, Object object) {
        if (object == null) {
            return null;
        }

        if (buffer == null) {
            buffer = new StringBuffer();
        }
        // FIXME
        buffer.append(ClassUtil.getSimpleClassNameForObject(object));

        return buffer.append('@').append(Integer.toHexString(identityHashCode(object)));
    }



    public static Object clone(Object array) throws CloneNotSupportedException {
        if (array == null) {
            return null;
        }


        if (Object[].class.isInstance(array)) {
            return ArrayUtil.clone((Object[]) array);
        }

        if (long[].class.isInstance(array)) {
            return ArrayUtil.clone((long[]) array);
        }

        if (int[].class.isInstance(array)) {
            return ArrayUtil.clone((int[]) array);
        }

        if (short[].class.isInstance(array)) {
            return ArrayUtil.clone((short[]) array);
        }

        if (byte[].class.isInstance(array)) {
            return ArrayUtil.clone((byte[]) array);
        }

        if (double[].class.isInstance(array)) {
            return ArrayUtil.clone((double[]) array);
        }

        if (float[].class.isInstance(array)) {
            return ArrayUtil.clone((float[]) array);
        }

        if (boolean[].class.isInstance(array)) {
            return ArrayUtil.clone((boolean[]) array);
        }

        if (char[].class.isInstance(array)) {
            return ArrayUtil.clone((char[]) array);
        }


        if (!(Cloneable.class.isInstance(array))) {
            throw new CloneNotSupportedException("Object of class " + array.getClass().getName() + " is not Cloneable");
        }

        Class<? extends Object> clazz = array.getClass();

        try {
            Method cloneMethod = clazz.getMethod("clone", Emptys.EMPTY_CLASS_ARRAY);

            return cloneMethod.invoke(array, Emptys.EMPTY_OBJECT_ARRAY);
        } catch (NoSuchMethodException e) {
            throw new CloneNotSupportedException(e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new CloneNotSupportedException(e.getMessage());
        } catch (IllegalAccessException e) {
            throw new CloneNotSupportedException(e.getMessage());
        } catch (InvocationTargetException e) {
            throw new CloneNotSupportedException(e.getMessage());
        }
    }

}
