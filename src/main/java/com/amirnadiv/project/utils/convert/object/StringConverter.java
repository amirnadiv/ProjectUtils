package com.amirnadiv.project.utils.common.convert.object;

import com.amirnadiv.project.utils.common.ArrayUtil;
import com.amirnadiv.project.utils.common.convert.ObjectConverter;
import com.amirnadiv.project.utils.common.convert.TypeConverter;

public class StringConverter extends ObjectConverter<String> implements TypeConverter<String> {

    public StringConverter() {
        register(String.class);
    }

    @Override
    public String toConvert(String value) {
        return value;
    }

    @Override
    public String fromConvert(String value) {
        return value;
    }

    public String toConvert(Object value) {
        if (value instanceof CharSequence) { // for speed
            return value.toString();
        }
        Class<?> type = value.getClass();
        if (type == Class.class) {
            return ((Class<?>) value).getName();
        }

        return ArrayUtil.toString(value);
    }

}
