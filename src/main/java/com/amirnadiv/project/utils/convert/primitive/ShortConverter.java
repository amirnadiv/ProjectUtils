package com.amirnadiv.project.utils.common.convert.primitive;

import com.amirnadiv.project.utils.common.StringUtil;
import com.amirnadiv.project.utils.common.convert.ObjectConverter;
import com.amirnadiv.project.utils.common.convert.ConvertException;
import com.amirnadiv.project.utils.common.convert.TypeConverter;

public class ShortConverter extends ObjectConverter<Short> implements TypeConverter<Short> {

    public ShortConverter() {
        register(Short.class);
        register(short.class);
    }

    @Override
    public Short toConvert(String value) {
        return convert(value);
    }

    @Override
    public String fromConvert(Short value) {
        return String.valueOf(value);
    }

    public Short toConvert(Object value) {
        if (value.getClass() == Short.class) {
            return (Short) value;
        }
        if (value instanceof Number) {
            return Short.valueOf(((Number) value).shortValue());
        }

        return convert(value.toString());
    }

    private Short convert(String value) {
        try {
            String stringValue = value.trim();
            if (StringUtil.startsWithChar(stringValue, '+')) {
                stringValue = stringValue.substring(1);
            }
            return Short.valueOf(stringValue);
        } catch (NumberFormatException e) {
            throw new ConvertException(value, e);
        }
    }

}
