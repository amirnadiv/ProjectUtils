package com.amirnadiv.project.utils.common.convert.object;

import java.math.BigDecimal;

import com.amirnadiv.project.utils.common.convert.ObjectConverter;
import com.amirnadiv.project.utils.common.convert.ConvertException;
import com.amirnadiv.project.utils.common.convert.TypeConverter;

public class BigDecimalConverter extends ObjectConverter<BigDecimal> implements TypeConverter<BigDecimal> {

    public BigDecimalConverter() {
        register(BigDecimal.class);
    }

    @Override
    public BigDecimal toConvert(String value) {
        return new BigDecimal(value.trim());
    }

    @Override
    public String fromConvert(BigDecimal value) {
        return String.valueOf(value);
    }

    public BigDecimal toConvert(Object value) {
        if (value instanceof BigDecimal) {
            return (BigDecimal) value;
        }
        try {
            return new BigDecimal(value.toString().trim());
        } catch (NumberFormatException e) {
            throw new ConvertException(value, e);
        }
    }

}
