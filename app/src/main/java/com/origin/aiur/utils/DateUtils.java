package com.origin.aiur.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2014/9/23.
 */
public class DateUtils {
    private static final String DATE_FORMAT_YYMMDD = "yy/MM/dd";

    public static String formatDate(long timestamp) {
        return formatDate(timestamp, DATE_FORMAT_YYMMDD);
    }

    public static String formatDate(long timestamp, String format) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);

        if (format == null) {
            format = DATE_FORMAT_YYMMDD;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(calendar.getTime());
    }
}
