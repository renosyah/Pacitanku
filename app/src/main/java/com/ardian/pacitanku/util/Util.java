package com.ardian.pacitanku.util;

import java.util.Calendar;
import java.util.Date;

public class Util {
    public static Date getDate(int daysAdded){
        Date dt = new  Date();
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        c.add(Calendar.DATE, daysAdded);
        return c.getTime();
    }
}