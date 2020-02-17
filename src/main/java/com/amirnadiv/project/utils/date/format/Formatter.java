package com.amirnadiv.project.utils.common.date.format;

import com.amirnadiv.project.utils.common.date.DateTimeStamp;
import com.amirnadiv.project.utils.common.date.DatetimeObject;

public interface Formatter {

    String convert(DatetimeObject datetime, String format);

    DateTimeStamp parse(String value, String format);
}
