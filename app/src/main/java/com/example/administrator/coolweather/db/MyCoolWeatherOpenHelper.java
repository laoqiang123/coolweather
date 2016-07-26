package com.example.administrator.coolweather.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2016/7/25 0025.
 */
public class MyCoolWeatherOpenHelper extends SQLiteOpenHelper {
    public static  final  String CREATE_PROVINCES = "create table Province(id integer primary key autoincrement,province_name text,province_code text)";
    public static  final  String CREATE_CITY = "create table City(id integer primary key autoincrement,city_name text,city_code text province_id integer)";
    public static  final  String CREATE_COUNT = "create table County(id integer primary key autoincrement,county_name text,county_code text city_id integer)";
    public MyCoolWeatherOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_PROVINCES);
        db.execSQL(CREATE_CITY);
        db.execSQL(CREATE_COUNT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
