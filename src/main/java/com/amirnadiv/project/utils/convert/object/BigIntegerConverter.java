package com.amirnadiv.project.utils.common.convert.object;

import java.math.BigInteger;

import com.amirnadiv.project.utils.common.convert.ObjectConverter;
import com.amirnadiv.project.utils.common.convert.ConvertException;
import com.amirnadiv.project.utils.common.convert.TypeConverter;

public class BigIntegerConverter extends ObjectConverter<BigInteger> implements TypeConverter<BigInteger> {

    public BigIntegerConverter() {
        register(BigInteger.class);
    }

    @Override
    public BigInteger toConvert(String value) {
        return new BigInteger(value.trim());
    }

    @Override
    public String fromConvert(BigInteger value) {
        return String.valueOf(value);
    }

    public BigInteger toConvert(Object value) {
        if (value instanceof BigInteger) {
            return (BigInteger) value;
        }
        if (value instanceof Number) {
            return new BigInteger(String.valueOf(((Number) value).longValue()));
        }
        try {
            return new BigInteger(value.toString().trim());
        } catch (NumberFormatException e) {
            throw new ConvertException(value, e);
        }
    }

}
