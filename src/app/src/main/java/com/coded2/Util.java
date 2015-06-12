package com.coded2;

import android.content.res.Resources;

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
    public static float dp2px(Resources resources, float dp) {
        final float scale = resources.getDisplayMetrics().density;
        return  dp * scale + 0.5f;
    }

    public static float sp2px(Resources resources, float sp){
        final float scale = resources.getDisplayMetrics().scaledDensity;
        return sp * scale;
    }

    public static long calculoPercentual(long partial, long total){
        double percent = (partial *100)/total;
        return Math.round(Math.floor(percent));
    }

}
