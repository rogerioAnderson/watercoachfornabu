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

    final static String ID_COLUMN_NAME="rowid";
    final static String ML_COLUMN_NAME="ml";
    final static String DATE_COLUMN_NAME ="date";
    final static String TIME_COLUMN_NAME ="time";
    final static String DAILY_RECORD_TABLE_NAME="water_daily_record";
    final static String SQL_LIST_CURRENT_DATE = "SELECT ROWID, ML, DATE, TIME FROM " + DAILY_RECORD_TABLE_NAME + " WHERE DATE = ? ORDER BY TIME ASC";
    final static String SQL_INSERT_RECORD = "INSERT INTO "+DAILY_RECORD_TABLE_NAME+" ("+ML_COLUMN_NAME+","+TIME_COLUMN_NAME+","+DATE_COLUMN_NAME+") VALUES (?,?,?)";
    final static String SQL_DELETE_RECORD = "DELETE FROM "+DAILY_RECORD_TABLE_NAME+" WHERE ROWID = ?";

    final String [] DB_COLUMNS = {ID_COLUMN_NAME,ML_COLUMN_NAME,DATE_COLUMN_NAME,TIME_COLUMN_NAME};

    long rowid;
    public int ml;
    String date;
    String time;


    void save(Context ctx){
        DBHelper helper = new DBHelper(ctx);
        SQLiteDatabase db = helper.getWritableDatabase();
        try{

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            Date now = new Date(System.currentTimeMillis());
            String currentTime = Util.formatHora(now);
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


    public static List<WaterDailyRecord> listOFDay(Context ctx) {
        List<WaterDailyRecord> result = new ArrayList<WaterDailyRecord>();
        DBHelper helper = new DBHelper(ctx);
        SQLiteDatabase db = helper.getReadableDatabase();
        try{
            Date now = new Date(System.currentTimeMillis());
            Cursor cursor = db.rawQuery(SQL_LIST_CURRENT_DATE, new String[]{Util.formatDate(now)});
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