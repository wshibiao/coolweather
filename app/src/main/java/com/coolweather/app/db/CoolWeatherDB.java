package com.coolweather.app.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.coolweather.app.model.City;
import com.coolweather.app.model.County;
import com.coolweather.app.model.Province;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wsb on 2015/12/25.
 */
public class CoolWeatherDB {
    /*

    *数据库名字
     */
    public static final  String DB_NAME="cool_weather";

    /*
    *数据库版本
     */
    public static final int DB_VERSION=1;
    private static CoolWeatherDB coolWeatherDB;
    private SQLiteDatabase db;

    private CoolWeatherDB(Context context){
        CoolWeatherOpenHelper dbHelper=new CoolWeatherOpenHelper(context
                                ,DB_NAME,null,DB_VERSION);
        db=dbHelper.getWritableDatabase();
    }
    /**
     * 获取CoolWeatherDB的实例。
     */

    public synchronized static CoolWeatherDB getInstance(Context context){
        if(coolWeatherDB==null)
            coolWeatherDB=new CoolWeatherDB(context);
        return coolWeatherDB;
    }

    /*
    *将province实例存到Province表中
     */
    public void saveProvince(Province province){
        if(province!=null) {
            db.execSQL("insert into Province name(province_name,province_code) value(?,?)"
                    , new String[]{province.getProvinceName(), province.getProvinceCode()});
        }
    }
    /**
     * 从数据库读取全国所有省份信息
     */
    public List<Province> loadProvince(){
        List<Province>provinceList=new ArrayList<Province>();
        Cursor cursor=db.query("Province",null,null,null,null,null,null);
//        db.execSQL("select * from Province",null);
        if(cursor.moveToNext()){
            do{
                Province province=new Province();
                province.setId(cursor.getInt(cursor.getColumnIndex("id")));
                province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
                province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
                provinceList.add(province);
            }while(cursor.moveToNext());
        }
        return provinceList;
    }

    public void  saveCity(City city){
        if(city!=null){
            db.execSQL("insert into City name(city_name,city_code,province_code) value(?,?,?)"
                    ,new Object[]{city.getCityName(),city.getCityCode(),city.getProvinceId()});
        }

    }
    /**
     * 从数据库读取某省所有城市信息
     */
    public List<City> loadCities(int provinceId){
        List<City>cityList=new ArrayList<City>();
        Cursor cursor=db.query("City",null,"province_id=?"
                ,new String[]{String.valueOf(provinceId)},null,null,null);
//        db.execSQL("select * from Province",null);
        if(cursor.moveToNext()){
            do{
                City city=new City();
                city.setId(cursor.getInt(cursor.getColumnIndex("id")));
                city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
                city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
                city.setProvinceId(provinceId);
                cityList.add(city);
            }while(cursor.moveToNext());
        }
        return cityList;
    }

    /**
     * 将County实例存储到数据库。
     */
    public void saveCounty(County county) {
        if (county != null) {
            ContentValues values = new ContentValues();
            values.put("county_name", county.getCountyName());
            values.put("county_code", county.getCountyCode());
            values.put("city_id", county.getCityId());
            db.insert("County", null, values);
        }
    }
    /**
     * 从数据库读取某城市下所有的县信息。
     */
    public List<County> loadCounties(int cityId) {
        List<County> list = new ArrayList<County>();
        Cursor cursor = db.query("County", null, "city_id = ?",
                new String[] { String.valueOf(cityId) }, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                County county = new County();
                county.setId(cursor.getInt(cursor.getColumnIndex("id")));
                county.setCountyName(cursor.getString(cursor.getColumnIndex("county_name")));
                county.setCountyCode(cursor.getString(cursor.getColumnIndex("county_code")));
                county.setCityId(cityId);
                list.add(county);
            } while (cursor.moveToNext());
        }
        return list;
    }

}

