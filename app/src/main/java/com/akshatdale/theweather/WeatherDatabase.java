package com.akshatdale.theweather;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class WeatherDatabase extends SQLiteOpenHelper {
    private static final String DB_NAME = "Weather_DataBase";
    private static final int DB_VERSION = 1;
    public static final String CITY_COLUMN = "City";
    public static final String TABLE_NAME = "Weather_cities";

    public WeatherDatabase(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL("create table " + TABLE_NAME + "(" + CITY_COLUMN + " text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("drop table if exists " + TABLE_NAME);
    }


    public void addCity(String city){

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CITY_COLUMN,city);
        sqLiteDatabase.insert(TABLE_NAME,null,contentValues);
    }


    public void updateCity(String city){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CITY_COLUMN,city);

        sqLiteDatabase.update(TABLE_NAME,contentValues,CITY_COLUMN,null);

    }

    public ArrayList<String> getDBcity(){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(" select * from "+TABLE_NAME,null);
        ArrayList<String> datalist = new ArrayList<>();
        String city;

        while (cursor.moveToNext()){
                  city = cursor.getString(0);
                datalist.add(city);
    }
        return datalist;
    }
}
