package com.amirnadiv.project.utils.common.convert.object;

import java.util.Calendar;
import java.util.Date;

import com.amirnadiv.project.utils.common.StringUtil;
import com.amirnadiv.project.utils.common.convert.ConvertException;
import com.amirnadiv.project.utils.common.convert.ObjectConverter;
import com.amirnadiv.project.utils.common.convert.TypeConverter;
import com.amirnadiv.project.utils.common.date.DatetimeObject;

public class DateConverter extends ObjectConverter<Date> implements TypeConverter<Date> {

    @Override
    public Date toConvert(String value) {
        return convert(value);
    }

    @Override
    public String fromConvert(Date value) {
        return String.valueOf(value);
    }

    public Date toConvert(Object value) {
        if (value instanceof Date) {
            return (Date) value;
        }
        if (value instanceof Calendar) {
            return new Date(((Calendar) value).getTimeInMillis());
        }
        if (value instanceof DatetimeObject) {
            return ((DatetimeObject) value).convertToDate();
        }
        if (value instanceof Number) {
            return new Date(((Number) value).longValue());
        }

        return convert(value.toString());

    }

    private Date convert(String value) {
        String stringValue = value.trim();

        if (!StringUtil.isDigits(stringValue)) {
            DatetimeObject datetime = new DatetimeObject(stringValue, DatetimeObject.DEFAULT_FORMAT);
            return datetime.convertToDate();
        }
        try {
            long milliseconds = Long.parseLong(stringValue);
            return new Date(milliseconds);
        } catch (NumberFormatException e) {
            throw new ConvertException(value, e);
        }
    }

}
