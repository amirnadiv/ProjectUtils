package com.amirnadiv.project.utils.common.convert.object;

import java.util.Calendar;
import java.util.Date;

import com.amirnadiv.project.utils.common.convert.ObjectConverter;
import com.amirnadiv.project.utils.common.convert.ConvertException;
import com.amirnadiv.project.utils.common.convert.TypeConverter;

public class CalendarConverter extends ObjectConverter<Calendar> implements TypeConverter<Calendar> {

    public CalendarConverter() {
        register(Calendar.class);
    }

    @Override
    public Calendar toConvert(String value) {
        return convert(value);
    }

    @Override
    public String fromConvert(Calendar value) {
        return String.valueOf(value);
    }

    public Calendar toConvert(Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof Calendar) {
            return (Calendar) value;
        }
        if (value instanceof Date) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime((Date) value);
            return calendar;
        }

        if (value instanceof Number) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(((Number) value).longValue());
            return calendar;
        }

        String stringValue = value.toString().trim();

        return convert(stringValue);
    }

    private Calendar convert(String value) {
        try {
            long milliseconds = Long.parseLong(value);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(milliseconds);
            return calendar;
        } catch (NumberFormatException e) {
            throw new ConvertException(value, e);
        }
    }

}
