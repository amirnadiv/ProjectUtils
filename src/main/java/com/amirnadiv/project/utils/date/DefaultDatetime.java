package com.amirnadiv.project.utils.common.date;

import java.util.Locale;
import java.util.TimeZone;

import com.amirnadiv.project.utils.common.date.format.ISO8601Formatter;
import com.amirnadiv.project.utils.common.date.format.Formatter;

public class DefaultDatetime {

    public static boolean monthFix = true;

    public static TimeZone timeZone; // system default

    public static Locale locale; // system default

    public static String format = DatetimeObject.DEFAULT_FORMAT;

    public static Formatter formatter = new ISO8601Formatter();

    public static int firstDayOfWeek = DatetimeObject.MONDAY;

    public static int mustHaveDayOfFirstWeek = 4;

    public static int minDaysInFirstWeek = 4;

    public static boolean trackDST;
}
