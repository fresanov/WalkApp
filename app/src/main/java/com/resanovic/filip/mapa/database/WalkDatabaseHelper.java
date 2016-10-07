package com.resanovic.filip.mapa.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Filip on 16.9.2016..
 */
public class WalkDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "walk_database";
    private static final String TABLE_NAME = "walk_data";
    private static final String DATE = "date";
    private static final String DISTANCE = "distance";
    private static final String STEPS = "steps";
    private static final String CALORIES = "calories";
    private static final String START_LATITUDE = "start_latitude";
    private static final String START_LONGITUDE = "start_longitude";
    private static final String STOP_LATITUDE = "stop_latitude";
    private static final String STOP_LONGITUDE = "stop_longitude";
    private static final String UUID = "uuid";
    private static final int VERSION = 1;

    public WalkDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + "(" +
                " _id integer primary key autoincrement, " +
                DATE + " numeric, " +
                DISTANCE + " real, " +
                STEPS + " integer, " +
                CALORIES + " integer, " +
                START_LATITUDE + " real, " +
                START_LONGITUDE + " real," +
                STOP_LATITUDE + " real," +
                STOP_LONGITUDE + " real," +
                UUID + " text" +
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean insertData(String date, double distance, int steps, int calories, double start_latitude, double start_longitude, double stop_latitude, double stop_longitude, String uuid){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DATE, date);
        values.put(DISTANCE, distance);
        values.put(STEPS, steps);
        values.put(CALORIES, calories);
        values.put(START_LATITUDE, start_latitude);
        values.put(START_LONGITUDE, start_longitude);
        values.put(STOP_LATITUDE, stop_latitude);
        values.put(STOP_LONGITUDE, stop_longitude);
        values.put(UUID, uuid);
        database.insert(TABLE_NAME, null, values);
        this.close();
        return true;
    }

    public Cursor getSingleRow(int id){
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("select * from " + TABLE_NAME + " where _id=" + String.valueOf(id), null);
        return cursor;
    }

    public Cursor getAllData(){
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("select * from " + TABLE_NAME, null);
        return cursor;
    }

    public void deleteTable() {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(TABLE_NAME, null, null);
        this.close();
    }
}
