package com.coded2.nabuwatercoach;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Rogerio on 17/05/2015.
 */
public class DBHelper extends SQLiteOpenHelper{

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "NabuWaterCoach.db";


    private static final String SQL_CREATE = "CREATE TABLE [daily_record] (\n" +
            "  [ml] INT(4) NOT NULL ON CONFLICT FAIL, \n" +
            "  [date] DATE NOT NULL ON CONFLICT FAIL DEFAULT CURRENT_DATE, \n" +
            "  [time] TIME NOT NULL ON CONFLICT FAIL DEFAULT CURRENT_TIME);";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            //NOT IMPLEMENTED
    }
}
