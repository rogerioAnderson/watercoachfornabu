package com.coded2;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by rogerioso on 26/05/2015.
 */
public class Util {

    public static String formatHora(Date data){
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

}
