package com.amirnadiv.project.utils.common.convert.object;

import com.amirnadiv.project.utils.common.ClassLoaderUtil;
import com.amirnadiv.project.utils.common.convert.ObjectConverter;
import com.amirnadiv.project.utils.common.convert.ConvertException;
import com.amirnadiv.project.utils.common.convert.TypeConverter;

public class ClassConverter extends ObjectConverter<Class<?>> implements TypeConverter<Class<?>> {

    public ClassConverter() {
        typeConverters.put(Class.class, this);
    }

    @Override
    public Class<?> toConvert(String value) {
        try {
            return ClassLoaderUtil.loadClass(value);
        } catch (ClassNotFoundException e) {
            throw new ConvertException(value, e);
        }
    }

    @Override
    public String fromConvert(Class<?> value) {
        return String.valueOf(value);
    }

    public Class<?> toConvert(Object value) {
        if (value.getClass() == Class.class) {
            return (Class<?>) value;
        }
        try {
            return ClassLoaderUtil.loadClass(value.toString().trim());
        } catch (ClassNotFoundException e) {
            throw new ConvertException(value, e);
        }
    }

}
