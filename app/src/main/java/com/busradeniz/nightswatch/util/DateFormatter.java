package com.busradeniz.nightswatch.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by busradeniz on 27/12/15.
 */
public class DateFormatter {

    public static String dateFormatToString(long timestamp){

        DateFormat df = new SimpleDateFormat("dd MMMM yyyy HH:mm:ss");
        return df.format(timestamp);
    }
}
