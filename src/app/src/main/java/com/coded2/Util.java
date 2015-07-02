package com.coded2;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by rogerioso on 26/05/2015.
 */
public class Util {

    public static String formatTime(Date data){
        if(data==null)
            return "";

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        return sdf.format(data);
    }

    public static String formatDate(Date data){
        if(data==null)
            return "";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(data);
    }

    public static String formatDateLocale(Date data){
        if(data==null)
            return "";
        DateFormat df = DateFormat.getDateInstance();
        return df.format(data);
    }

    /*
    public static boolean isToday(Date date){
        if(date==null) return false;
        String dateStr = formatDate(date);
        String nowStr = formatDate(new Date(System.currentTimeMillis()));

        boolean dataIgual = dateStr.equalsIgnoreCase(nowStr);

        if(dataIgual){
            return false;
        }

        return true;
    }
    */

    public static boolean isSameDay(Calendar cal1, Calendar cal2) {
        if (cal1 == null || cal2 == null) {
            return false;
        }
        return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) &&
                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR));
    }


    public static boolean isToday(Date date) {
        Calendar calendarDate = Calendar.getInstance();
        calendarDate.setTime(date);
        return isSameDay(calendarDate, Calendar.getInstance());
    }

}
