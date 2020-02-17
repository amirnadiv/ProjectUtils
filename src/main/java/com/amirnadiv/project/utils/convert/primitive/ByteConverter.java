package com.amirnadiv.project.utils.common.convert.primitive;

import com.amirnadiv.project.utils.common.StringUtil;
import com.amirnadiv.project.utils.common.convert.ObjectConverter;
import com.amirnadiv.project.utils.common.convert.ConvertException;
import com.amirnadiv.project.utils.common.convert.TypeConverter;

public class ByteConverter extends ObjectConverter<Byte> implements TypeConverter<Byte> {

    public ByteConverter() {
        register(Byte.class);
        register(byte.class);
    }

    @Override
    public Byte toConvert(String value) {
        return convert(value);
    }

    @Override
    public String fromConvert(Byte value) {
        return String.valueOf(value);
    }

    public Byte toConvert(Object value) {
        if (value.getClass() == Byte.class) {
            return (Byte) value;
        }
        if (value instanceof Number) {
            return Byte.valueOf(((Number) value).byteValue());
        }

        return convert(value.toString());
    }

    private Byte convert(String value) {
        try {
            String stringValue = value.trim();
            if (StringUtil.startsWithChar(stringValue, '+')) {
                stringValue = stringValue.substring(1);
            }
            return Byte.valueOf(stringValue);
        } catch (NumberFormatException e) {
            throw new ConvertException(value, e);
        }
    }

}
