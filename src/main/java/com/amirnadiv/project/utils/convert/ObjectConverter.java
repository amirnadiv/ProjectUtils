package com.amirnadiv.project.utils.common.convert;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.amirnadiv.project.utils.common.ClassLoaderUtil;
import com.amirnadiv.project.utils.common.CollectionUtil;
import com.amirnadiv.project.utils.common.PackageUtil;
import com.amirnadiv.project.utils.common.convert.TypeConverter.Convert;
import com.amirnadiv.project.utils.common.logger.CachedLogger;
import com.amirnadiv.project.utils.common.logger.Logger;
import com.amirnadiv.project.utils.common.logger.LoggerFactory;

@TypeConverter.Convert
public class ObjectConverter<T> extends CachedLogger {

    private static Logger LOGGER = LoggerFactory.getLogger(ObjectConverter.class);

    protected static Map<Class<?>, TypeConverter<?>> typeConverters = CollectionUtil.createHashMap();

    private final static ObjectConverter<Object> instance = createInstance();

    static {
        try {
            init();
        } catch (Exception e) {
            LOGGER.error("clinit error", e);
        }
    }

    static <T> ObjectConverter<T> createInstance() {
        return new ObjectConverter<T>();
    }

    public static ObjectConverter<Object> getInstance() {
        return instance;
    }

    private static void init() throws IOException, ClassNotFoundException, InstantiationException,
            IllegalAccessException {
        // FIXME
        String packName = PackageUtil.getPackage(ObjectConverter.class).getName() + ".*";
        List<String> classNames = PackageUtil.getClassesInPackage(packName);

        for (String classname : classNames) {
            Class<?> clazz = ClassLoaderUtil.loadClass(classname);
            if (clazz.getAnnotation(Convert.class) != null) {
                clazz.newInstance();
            }
        }
    }

    public T toConvert(String value, Class<?> clazz) {
        if (value == null) {
            return null;
        }

        @SuppressWarnings("unchecked")
        TypeConverter<T> converter = (TypeConverter<T>) typeConverters.get(clazz);

        return (converter == null) ? null : converter.toConvert(value);
    }

    public String fromConvert(T value) {
        if (value == null) {
            return null;
        }
        @SuppressWarnings("unchecked")
        TypeConverter<T> converter = (TypeConverter<T>) typeConverters.get(value.getClass());

        return (converter == null) ? null : converter.fromConvert(value);
    }

    protected void register(Class<T> clazz) {
        // FIXME
        typeConverters.put(clazz, (TypeConverter<?>) this);
    }

    public T toConvert(Object value, Class<?> clazz) {
        if (value == null) {
            return null;
        }
        @SuppressWarnings("unchecked")
        TypeConverter<T> converter = (TypeConverter<T>) typeConverters.get(clazz);

        return (converter == null) ? null : converter.toConvert(value);
    }

}
