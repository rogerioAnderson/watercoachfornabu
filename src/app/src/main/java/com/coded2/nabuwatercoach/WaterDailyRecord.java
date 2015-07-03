package com.coded2.nabuwatercoach;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.coded2.DBHelper;
import com.coded2.Util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by rogerioso on 12/05/2015.
 */
public class WaterDailyRecord {

    private final static String ID_COLUMN_NAME="rowid";
    private final static String ML_COLUMN_NAME="ml";
    private final static String DATE_COLUMN_NAME ="date";
    private final static String TIME_COLUMN_NAME ="time";
    private final static String DAILY_RECORD_TABLE_NAME="water_daily_record";
    private final static String SQL_LIST_CURRENT_DATE = "SELECT ROWID, ML, DATE, TIME FROM " + DAILY_RECORD_TABLE_NAME + " WHERE DATE = ? ORDER BY TIME ASC";
    private final static String SQL_INSERT_RECORD = "INSERT INTO "+DAILY_RECORD_TABLE_NAME+" ("+ML_COLUMN_NAME+","+TIME_COLUMN_NAME+","+DATE_COLUMN_NAME+") VALUES (?,?,?)";
    private final static String SQL_DELETE_RECORD = "DELETE FROM "+DAILY_RECORD_TABLE_NAME+" WHERE ROWID = ?";
    private final static String SQL_STATISTICS = "SELECT SUM(ML) "+ML_COLUMN_NAME+", strftime(\"%m/%Y\",date) "+DATE_COLUMN_NAME+" from water_daily_record group by strftime(\"%m/%Y\",date) order by date desc";
    private final static String SQL_STATISTICS_BY_MONTH = "SELECT SUM(ml) "+ML_COLUMN_NAME+",strftime('%d',date) "+DATE_COLUMN_NAME+" FROM WATER_DAILY_RECORD WHERE strftime(\"%m/%Y\",date) =? group by date";
    private final String [] DB_COLUMNS = {ID_COLUMN_NAME,ML_COLUMN_NAME,DATE_COLUMN_NAME,TIME_COLUMN_NAME};

    long rowid;
    int ml;
    String date;
    String time;


    void save(Context ctx){
        DBHelper helper = new DBHelper(ctx);
        SQLiteDatabase db = helper.getWritableDatabase();
        try{

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            Date now = new Date(System.currentTimeMillis());
            String currentTime = Util.formatTime(now);
            String currentDate = Util.formatDate(now);
            String params[] = {Integer.toString(this.ml),currentTime,currentDate};
            db.execSQL(SQL_INSERT_RECORD,params);

        }finally {
            db.close();
            helper.close();
        }


    }

    void delete(Context ctx){
        DBHelper helper = new DBHelper(ctx);
        SQLiteDatabase db = helper.getWritableDatabase();
        try{
            String params[] = {Long.toString(this.rowid)};
            db.execSQL(SQL_DELETE_RECORD,params);
        }finally {
            db.close();
            helper.close();
        }
    }


    static List<WaterDailyRecord>statisticsOfMonth(Context ctx,String month){
        List<WaterDailyRecord> result = new ArrayList<WaterDailyRecord>();
        DBHelper helper = new DBHelper(ctx);
        SQLiteDatabase db = helper.getReadableDatabase();
        try{

            Cursor cursor = db.rawQuery(SQL_STATISTICS_BY_MONTH, new String[]{month});
            try{
                if (cursor.moveToFirst()) {
                    do {
                        WaterDailyRecord record = new WaterDailyRecord();
                        record.ml = cursor.getInt(cursor.getColumnIndex(ML_COLUMN_NAME));
                        record.date = cursor.getString(cursor.getColumnIndex(DATE_COLUMN_NAME));
                        result.add(record);
                    } while (cursor.moveToNext());
                }
            }finally {
                cursor.close();
            }

        }finally {
            db.close();
        }
        helper.close();
        return result;
    }

    static List<WaterDailyRecord>statistics(Context ctx){
        List<WaterDailyRecord> result = new ArrayList<WaterDailyRecord>();
        DBHelper helper = new DBHelper(ctx);
        SQLiteDatabase db = helper.getReadableDatabase();
        try{

            Cursor cursor = db.rawQuery(SQL_STATISTICS, null);
            try{
                if (cursor.moveToFirst()) {
                    do {
                        WaterDailyRecord record = new WaterDailyRecord();
                        record.ml = cursor.getInt(cursor.getColumnIndex(ML_COLUMN_NAME));
                        record.date = cursor.getString(cursor.getColumnIndex(DATE_COLUMN_NAME));
                        result.add(record);
                    } while (cursor.moveToNext());
                }
            }finally {
                cursor.close();
            }

        }finally {
            db.close();
        }
        helper.close();
        return result;

    }

    static List<WaterDailyRecord> listOFDay(Context ctx, Date date) {
        List<WaterDailyRecord> result = new ArrayList<WaterDailyRecord>();
        DBHelper helper = new DBHelper(ctx);
        SQLiteDatabase db = helper.getReadableDatabase();
        try{


            String param;
            if(date==null){
                Date now = new Date(System.currentTimeMillis());
                param = Util.formatDate(now);
            }else{
                param = Util.formatDate(date);
            }

            Cursor cursor = db.rawQuery(SQL_LIST_CURRENT_DATE, new String[]{param});
            try{
                if (cursor.moveToFirst()) {
                    do {
                        WaterDailyRecord record = new WaterDailyRecord();
                        record.rowid = cursor.getLong(cursor.getColumnIndex(ID_COLUMN_NAME));
                        record.ml = cursor.getInt(cursor.getColumnIndex(ML_COLUMN_NAME));
                        record.date = cursor.getString(cursor.getColumnIndex(DATE_COLUMN_NAME));
                        record.time = cursor.getString(cursor.getColumnIndex(TIME_COLUMN_NAME));
                        result.add(record);
                    } while (cursor.moveToNext());
                }
            }finally {
                cursor.close();
            }

        }finally {
            db.close();
        }
        helper.close();
        return result;
    }
}