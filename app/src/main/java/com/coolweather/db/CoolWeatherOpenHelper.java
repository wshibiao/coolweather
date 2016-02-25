package com.coolweather.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by wsb on 2015/12/25.
 */
public class CoolWeatherOpenHelper extends SQLiteOpenHelper {

    private static final String CERATE_WEATHERID="create table WeatherId("
            +"wid integer primary key,"
            +"weather text)";
    private static final String CREATE_CITY="create table CityList("
                +"id integer primary key,"
                +"province text,"
                +"city text,"
                +"district text)";

    public  CoolWeatherOpenHelper(Context context,String name
            ,SQLiteDatabase.CursorFactory factoryint,int version){
        super(context,name,factoryint,version);
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CERATE_WEATHERID);
        db.execSQL(CREATE_CITY);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){

    }
}
