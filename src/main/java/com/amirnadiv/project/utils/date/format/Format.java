package com.amirnadiv.project.utils.common.date.format;

import com.amirnadiv.project.utils.common.date.DateTimeStamp;
import com.amirnadiv.project.utils.common.date.DatetimeObject;

public class Format {

    protected final String format;
    protected final Formatter formatter;

    public Format(Formatter formatter, String format) {
        this.format = format;
        this.formatter = formatter;
    }

    public String getFormat() {
        return format;
    }

    public Formatter getFormatter() {
        return formatter;
    }

    public String convert(DatetimeObject jdt) {
        return formatter.convert(jdt, format);
    }

    public DateTimeStamp parse(String value) {
        return formatter.parse(value, format);
    }
}
