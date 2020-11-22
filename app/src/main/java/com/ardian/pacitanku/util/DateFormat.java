package com.ardian.pacitanku.util;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormat {

    // date format untuk tanggal data event firebase database
    public static final SimpleDateFormat ISO_8601_FORMAT = new SimpleDateFormat("dd-MM-yyyy");

    // date format untuk tanggal data event firebase database
    // untuk tanggal ditampilkan hanya waktunya saja
    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("HH:mm:ss");

    // date format untuk tanggal data event firebase database
    // untuk tanggal ditampilkan dalam bentuk yg simple
    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat simpleFormat = new SimpleDateFormat("dd/MM/yyyy");

    // date format untuk tanggal data event firebase database
    // untuk tanggal ditampilkan dalam bentuk yg full
    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat fullFormat = new SimpleDateFormat("EE, dd/MM/yyyy HH:mm:ss Z");

    // fungsi date format untuk tanggal data event firebase database
    // untuk tanggal ditampilkan dalam bentuk yg simple
    public static String simpleDate(Date d) {
        return simpleFormat.format(d);
    }

    // fungsi date format untuk tanggal data event firebase database
    // untuk tanggal ditampilkan dalam bentuk yg full
    public static String fullDate(Date d) {
        return fullFormat.format(d);
    }

    // fungsi date format untuk tanggal data event firebase database
    // untuk tanggal ditampilkan hanya waktunya saja
    public static String simpleTime(Date date) {
        return simpleTimeFormat.format(date);
    }
}
