package com.ardian.pacitanku.util;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormat {
    public static final SimpleDateFormat ISO_8601_FORMAT = new SimpleDateFormat("dd-MM-yyyy");

    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("HH:mm:ss");

    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat simpleFormat = new SimpleDateFormat("dd/MM/yyyy");

    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat fullFormat = new SimpleDateFormat("EE, dd/MM/yyyy HH:mm:ss Z");

    public static String simpleDate(Date d) {
        return simpleFormat.format(d);
    }
    public static String fullDate(Date d) {
        return fullFormat.format(d);
    }
    public static String simpleTime(Date date) {
        return simpleTimeFormat.format(date);
    }
}
