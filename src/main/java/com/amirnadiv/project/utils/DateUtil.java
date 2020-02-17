package com.amirnadiv.project.utils.common;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public abstract class DateUtil {


    public static final long SECOND = 1000;

    public static final long MINUTE = SECOND * 60;

    public static final long HOUR = MINUTE * 60;

    public static final long DAY = 24 * HOUR;

    public static final String TIME_BEGIN = " 00:00:00";

    public static final String TIME_END = " 23:59:59";



    public static final String MONTH_PATTERN = "yyyy-MM";

    public static final String DEFAULT_PATTERN = "yyyyMMdd";

    public static final String DOT_PATTERN = "yyyy.MM.dd";

    public static final String FULL_PATTERN = "yyyyMMddHHmmss";

    public static final String FULL_STANDARD_PATTERN = "yyyyMMdd HH:mm:ss";

    public static final String CHINESE_PATTERN = "yyyy-MM-dd";

    public static final String FULL_CHINESE_PATTERN = "yyyy-MM-dd HH:mm:ss";



    public static Calendar getCurrentCalendar() {
        return Calendar.getInstance();
    }

    public static Timestamp getCurrentTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    public static Date getCurrentDatetime() {
        return new Date(System.currentTimeMillis());
    }

    public static Date getCurrentDate() {
        Calendar calendar = Calendar.getInstance();

        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return new Date(calendar.getTimeInMillis());
    }

    public static String getYear() {
        return formatDate("yyyy");
    }

    public static String getMonth() {
        return formatDate("MM");
    }

    public static String getDay() {
        return formatDate("dd");
    }

    public static final int getYear(Date date) {
        if (date == null) {
            return -1;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }

    public static final int getYear(long millis) {
        Calendar calendar = Calendar.getInstance();

        calendar.setTimeInMillis(millis);
        return calendar.get(Calendar.YEAR);
    }

    public static final int getMonth(Date date) {
        if (date == null) {
            return -1;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH) + 1;
    }

    public static final int getMonth(long millis) {
        Calendar calendar = Calendar.getInstance();

        calendar.setTimeInMillis(millis);
        return calendar.get(Calendar.MONTH) + 1;
    }

    public static final int getDay(Date date) {
        if (date == null) {
            return -1;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DATE);
    }

    public static final int getDay(long millis) {
        Calendar calendar = Calendar.getInstance();

        calendar.setTimeInMillis(millis);
        return calendar.get(Calendar.DATE);
    }

    public static final int getHour(Date date) {
        if (date == null) {
            return -1;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    public static final int getHour(long millis) {
        Calendar calendar = Calendar.getInstance();

        calendar.setTimeInMillis(millis);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    public static String getChinesePatternNow() {
        return formatDate(CHINESE_PATTERN);
    }

    public static String getFullChinesePatternNow() {
        return formatDate(FULL_CHINESE_PATTERN);
    }

    public static String getChinesePatternNow(Date date) {
        return formatDate(date, CHINESE_PATTERN);
    }

    public static String getFullCNPatternNow(Date date) {
        return formatDate(date, FULL_CHINESE_PATTERN);
    }


    public static String formatDate(final Date date) {
        return formatDate(date, DEFAULT_PATTERN);
    }

    public static String formatDate(final Date date, String format) {
        if (null == date || StringUtil.isBlank(format)) {
            return null;
        }

        return new SimpleDateFormat(format).format(date);
    }

    public static String formatDate(String format) {
        return formatDate(new Date(), format);
    }

    public static Date parseDate(String sDate) {
        return parseDate(sDate, DEFAULT_PATTERN, null);
    }

    public static Date parseDate(String sDate, String format) {
        return parseDate(sDate, format, null);
    }

    public static Date parseDate(String sDate, String format, Date defaultValue) {
        if (StringUtil.isBlank(sDate) || StringUtil.isBlank(format)) {
            return defaultValue;
        }

        DateFormat formatter = new SimpleDateFormat(format);
        try {
            return formatter.parse(sDate);
        } catch (ParseException e) {
            return defaultValue;
        }

    }


    public static Date addMonths(Date date, int months) {
        if (months == 0) {
            return date;
        }

        if (date == null) {
            return null;
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, months);
        return cal.getTime();
    }

    public static Date addDays(final Date date, int days) {
        if (days == 0) {
            return date;
        }

        if (date == null) {
            return null;
        }

        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, days);

        return cal.getTime();
    }

    public static Date addMins(final Date date, int mins) {
        if (mins == 0) {
            return date;
        }

        if (date == null) {
            return null;
        }

        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MINUTE, mins);

        return cal.getTime();
    }

    // ==========================================================================
    // 日期比较。
    // ==========================================================================

    public static int compareDate(Date first, Date second) {
        if ((first == null) && (second == null)) {
            return 0;
        }

        if (first == null) {
            return -1;
        }

        if (second == null) {
            return 1;
        }

        if (first.before(second)) {
            return -1;
        }

        if (first.after(second)) {
            return 1;
        }

        return 0;
    }

    public static Date getSmaller(Date first, Date second) {
        if ((first == null) && (second == null)) {
            return null;
        }

        if (first == null) {
            return second;
        }

        if (second == null) {
            return first;
        }

        if (first.before(second)) {
            return first;
        }

        if (first.after(second)) {
            return second;
        }

        return first;
    }

    public static Date getLarger(Date first, Date second) {
        if ((first == null) && (second == null)) {
            return null;
        }

        if (first == null) {
            return second;
        }

        if (second == null) {
            return first;
        }

        if (first.before(second)) {
            return second;
        }

        if (first.after(second)) {
            return first;
        }

        return first;
    }

    public static boolean isDateBetween(Date date, Date begin, Date end) {
        int c1 = compareDate(begin, date);
        int c2 = compareDate(date, end);

        return (((c1 == -1) && (c2 == -1)) || (c1 == 0) || (c2 == 0));
    }

    public static boolean isSameMonth(Date date1, Date date2) {
        if (date1 == null && date2 == null) {
            return true;
        }

        if (date1 == null || date2 == null) {
            return false;
        }

        Calendar cal1 = GregorianCalendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = GregorianCalendar.getInstance();
        cal2.setTime(date2);
        return isSameMonth(cal1, cal2);
    }

    public static boolean isSameDay(Date date1, Date date2) {
        if (date1 == null && date2 == null) {
            return true;
        }

        if (date1 == null || date2 == null) {
            return false;
        }

        Calendar cal1 = GregorianCalendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = GregorianCalendar.getInstance();
        cal2.setTime(date2);
        return (cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR))
                && (cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) && (cal1.get(Calendar.DATE) == cal2
                        .get(Calendar.DATE)));
    }

    public static boolean isSameMonth(Calendar cal1, Calendar cal2) {
        if (cal1 == null && cal2 == null) {
            return true;
        }

        if (cal1 == null || cal2 == null) {
            return false;
        }

        return (cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR))
                && (cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH));
    }

    // ==========================================================================
    // 常见特殊时间点。
    // ==========================================================================

    public static Date getStartOfDate(final Date date) {
        if (date == null) {
            return null;
        }

        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static Date getPreviousMonday() {
        Calendar cd = Calendar.getInstance();
        // 获得今天是一周的第几天，星期日是第一天，星期二是第二天......
        int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK) - 1; // 因为按中国礼拜一作为第一天所以这里减1
        Date date;
        if (dayOfWeek == 1) {
            date = addDays(cd.getTime(), -7);
        } else {
            date = addDays(cd.getTime(), -6 - dayOfWeek);
        }
        return getStartOfDate(date);
    }

    public static Date getMondayBefore4Week() {
        Calendar cd = Calendar.getInstance();
        // 获得今天是一周的第几天，星期日是第一天，星期二是第二天......
        int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK) - 1; // 因为按中国礼拜一作为第一天所以这里减1
        Date date;
        if (dayOfWeek == 1) {
            date = addDays(cd.getTime(), -28);
        } else {
            date = addDays(cd.getTime(), -27 - dayOfWeek);
        }
        return getStartOfDate(date);
    }

    public static Date getCurrentMonday() {
        Calendar cd = Calendar.getInstance();
        // 获得今天是一周的第几天，星期日是第一天，星期二是第二天......
        int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK) - 1; // 因为按中国礼拜一作为第一天所以这里减1
        Date date;
        if (dayOfWeek == 1) {
            date = cd.getTime();
        } else {
            date = addDays(cd.getTime(), 1 - dayOfWeek);
        }
        return getStartOfDate(date);
    }

    public static Date getEndOfMonth(final Date date) {
        if (date == null) {
            return null;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);
        calendar.set(Calendar.DATE, 0);

        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return new Date(calendar.getTimeInMillis());
    }

    public static Date getFirstOfMonth(final Date date) {
        Date lastMonth = addMonths(date, -1);

        lastMonth = getEndOfMonth(lastMonth);
        return addDays(lastMonth, 1);
    }

    // FIXME
    public static Date getWeekBegin(Date date) {
        if (date == null) {
            return null;
        }
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);

        int dw = cal.get(Calendar.DAY_OF_WEEK);
        while (dw != Calendar.MONDAY) {
            cal.add(Calendar.DATE, -1);
            dw = cal.get(Calendar.DAY_OF_WEEK);
        }
        return cal.getTime();
    }

    public static Date getWeekEnd(Date date) {
        if (date == null) {
            return null;
        }
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);

        int dw = cal.get(Calendar.DAY_OF_WEEK);
        while (dw != Calendar.SUNDAY) {
            cal.add(Calendar.DATE, 1);
            dw = cal.get(Calendar.DAY_OF_WEEK);
        }
        return cal.getTime();
    }

    public static boolean inFormat(String sourceDate, String format) {
        if (sourceDate == null || StringUtil.isBlank(format)) {
            return false;
        }

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(format);
            dateFormat.setLenient(false);
            dateFormat.parse(sourceDate);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // ==========================================================================
    // 时间间隔。
    // ==========================================================================

    public static int getNumberOfSecondsBetween(final double d1, final double d2) {
        if ((d1 == 0) || (d2 == 0)) {
            return -1;
        }

        return (int) (Math.abs(d1 - d2) / SECOND);
    }

    public static int getNumberOfMonthsBetween(final Date begin, final Date end) {
        if (begin == null || end == null) {
            return -1;
        }

        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(begin);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(end);
        return (cal2.get(Calendar.YEAR) - cal1.get(Calendar.YEAR)) * 12
                + (cal2.get(Calendar.MONTH) - cal1.get(Calendar.MONTH));
    }

    public static long getNumberOfMinuteBetween(final Date begin, final Date end) {
        if (begin == null || end == null) {
            return -1;
        }

        long millisec = end.getTime() - begin.getTime();
        return millisec / (60 * 1000);
    }

    public static long getNumberOfHoursBetween(final Date begin, final Date end) {
        if (begin == null || end == null) {
            return -1;
        }

        long millisec = end.getTime() - begin.getTime() + 1;
        return millisec / (60 * 60 * 1000);
    }

    // FIXME
    public static long getNumberOfDaysBetween(final Date begin, final Date end) {
        if (begin == null || end == null) {
            return -1;
        }

        long millisec = end.getTime() - begin.getTime();
        return millisec / (60 * 60 * 1000 * 24);
    }

    public static long getNumberOfDaysBetween(Calendar before, Calendar after) {
        if (before == null || after == null) {
            return -1;
        }

        before.clear(Calendar.MILLISECOND);
        before.clear(Calendar.SECOND);
        before.clear(Calendar.MINUTE);
        before.clear(Calendar.HOUR_OF_DAY);

        after.clear(Calendar.MILLISECOND);
        after.clear(Calendar.SECOND);
        after.clear(Calendar.MINUTE);
        after.clear(Calendar.HOUR_OF_DAY);

        long elapsed = after.getTime().getTime() - before.getTime().getTime();
        return elapsed / DAY;
    }

    // ==========================================================================
    // 远程。
    // ==========================================================================

    public static Date getRemoteDate(String url) {
        if (url == null) {
            return null;
        }

        URLConnection uc;
        try {
            uc = new URL(url).openConnection();
            uc.connect(); // 发出连接
            return new Date(uc.getDate());// 生成连接对象
        } catch (IOException e) {
            return new Date();
        }

    }

    // ==========================================================================
    // from beidou。
    // ==========================================================================
    public static Calendar getCurDateCeil() {
        Calendar cal = new GregorianCalendar();
        return new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
    }

    public static Calendar getCurDateFloor() {
        Calendar cal = new GregorianCalendar();
        return new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH),
                23, 0);
    }

    public static Date getWeekBegin(Calendar tmp) {
        if (tmp == null) {
            return null;
        }

        Calendar ctmp =
                new GregorianCalendar(tmp.get(Calendar.YEAR), tmp.get(Calendar.MONTH), tmp.get(Calendar.DAY_OF_MONTH));

        int dw = ctmp.get(Calendar.DAY_OF_WEEK);
        while (dw != Calendar.MONDAY) {
            ctmp.add(Calendar.DATE, -1);
            dw = ctmp.get(Calendar.DAY_OF_WEEK);
        }
        return ctmp.getTime();
    }

    public static Date getWeekEnd(Calendar tmp) {
        if (tmp == null) {
            return null;
        }

        Calendar ctmp =
                new GregorianCalendar(tmp.get(Calendar.YEAR), tmp.get(Calendar.MONTH), tmp.get(Calendar.DAY_OF_MONTH),
                        23, 0);

        int dw = ctmp.get(Calendar.DAY_OF_WEEK);
        while (dw != Calendar.SUNDAY) {
            ctmp.add(Calendar.DATE, 1);
            dw = ctmp.get(Calendar.DAY_OF_WEEK);
        }
        return ctmp.getTime();
    }

    // FIXME
    // public static Date getMonthBegin(Calendar tmp){
    // Calendar ctmp = new GregorianCalendar(tmp.get(Calendar.YEAR), tmp
    // .get(Calendar.MONTH), tmp.get(Calendar.DAY_OF_MONTH));
    //
    // int dm = ctmp.get(Calendar.DAY_OF_MONTH);
    // while (dm != 1){
    // ctmp.add(Calendar.DATE, -1);
    // dm = ctmp.get(Calendar.DAY_OF_MONTH);
    // }
    // return ctmp.getTime();
    // }

    // FIXME
    public static Date getQuarterBegin(Calendar tmp) {
        if (tmp == null) {
            return null;
        }

        Calendar ctmp =
                new GregorianCalendar(tmp.get(Calendar.YEAR), tmp.get(Calendar.MONTH), tmp.get(Calendar.DAY_OF_MONTH));

        int month = ctmp.get(Calendar.MONTH);
        int offset = -(month % 3); // TODO: 这里有问题，month要+1再mod
        ctmp.add(Calendar.MONTH, offset);

        return getFirstOfMonth(ctmp.getTime());
    }

    public static Date getQuarterEnd(Calendar tmp) {
        if (tmp == null) {
            return null;
        }

        Calendar ctmp =
                new GregorianCalendar(tmp.get(Calendar.YEAR), tmp.get(Calendar.MONTH), tmp.get(Calendar.DAY_OF_MONTH),
                        23, 0);

        int month = ctmp.get(Calendar.MONTH);
        int offset = 2 - (month % 3);
        ctmp.add(Calendar.MONTH, offset);

        return getEndOfMonth(ctmp.getTime());
    }

    public static Date getQuarterBegin(Date date) {
        if (date == null) {
            return null;
        }
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);

        int month = cal.get(Calendar.MONTH);
        int offset = -(month % 3); // TODO: 这里有问题，month要+1再mod
        cal.add(Calendar.MONTH, offset);

        return getFirstOfMonth(cal.getTime());
    }

    public static Date getQuarterEnd(Date date) {
        if (date == null) {
            return null;
        }
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);

        int month = cal.get(Calendar.MONTH);
        int offset = 2 - (month % 3);
        cal.add(Calendar.MONTH, offset);

        return getEndOfMonth(cal.getTime());
    }

    public static Date getYearBegin(final Date date) {
        if (date == null) {
            return null;
        }

        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.MONTH, 0);
        cal.set(Calendar.DATE, 1);

        return cal.getTime();
    }

    public static Date getYearEnd(final Date date) {
        if (date == null) {
            return null;
        }

        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.MONTH, 11);

        return getEndOfMonth(cal.getTime());
    }

    public static void main(String[] args) {
        // 20140630
    }

    public static Date getDate(int year, int month, int day) {
        GregorianCalendar d = new GregorianCalendar(year, month - 1, day);
        return d.getTime();
    }

}
