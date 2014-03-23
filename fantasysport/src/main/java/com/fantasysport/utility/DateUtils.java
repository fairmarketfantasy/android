package com.fantasysport.utility;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by bylynka on 3/22/14.
 */
public class DateUtils {

    public static final long ONE_MINUTE_IN_MILLIS=60000;

    public static Date addMinutes(Date date, int minutes){
        long t = date.getTime();
        return new Date(t + (minutes*ONE_MINUTE_IN_MILLIS));
    }

    public static Date getCurrentDate(){
        Calendar calendar = Calendar.getInstance();
        return calendar.getTime();
    }
}
