package com.amirnadiv.project.utils.common.convert.primitive;

import com.amirnadiv.project.utils.common.StringUtil;
import com.amirnadiv.project.utils.common.convert.ObjectConverter;
import com.amirnadiv.project.utils.common.convert.ConvertException;
import com.amirnadiv.project.utils.common.convert.TypeConverter;

public class IntegerConverter extends ObjectConverter<Integer> implements TypeConverter<Integer> {

    public IntegerConverter() {
        register(Integer.class);
        register(int.class);
    }

    @Override
    public Integer toConvert(String value) {
        return convert(value);
    }

    @Override
    public String fromConvert(Integer value) {
        return String.valueOf(value);
    }

    public Integer toConvert(Object value) {
        if (value.getClass() == Integer.class) {
            return (Integer) value;
        }
        if (value instanceof Number) {
            return Integer.valueOf(((Number) value).intValue());
        }

        return convert(value.toString());
    }

    private Integer convert(String value) {
        try {
            String stringValue = value.trim();
            if (StringUtil.startsWithChar(stringValue, '+')) {
                stringValue = stringValue.substring(1);
            }
            return Integer.valueOf(stringValue);
        } catch (NumberFormatException e) {
            throw new ConvertException(value, e);
        }
    }

}
