package com.coolweather.app.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.coolweather.app.model.CityList;

import org.json.JSONObject;

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

    /**
     * 将所有天气类型存入数据库中,减少访问网络
     * @param obj
     */
    public void saveWeatherId(JSONObject obj){
        if(obj!=null){
            try{
                db.execSQL("insert into WeatherId (wid,weather) values(?,?)"
                        ,new Object[]{obj.getInt("wid"),obj.getString("weather")});
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     * 根据ID查找天气类型
     * @param id
     */

    public String loadWeatherId(String id){
        String weatherType;
        Cursor cursor;
        if((cursor=db.rawQuery("select * from WeatherId where wid=?", new String[]{id})).moveToFirst()){
            weatherType=cursor.getString(cursor.getColumnIndex("weather"));
            return weatherType;
        }else
            return null;

    }

    /*
    *将所有城市保存在数据库中
     */
    public void saveCity(JSONObject obj){
        if(obj!=null) {
            try {
                db.execSQL("insert into CityList (id,province,city,district) values(?,?,?,?)"
                        , new Object[]{obj.getInt("id"),obj.getString("province")
                        , obj.getString("city"),obj.getString("district")});
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    /**
     *
     * @param name
     *          传入查询的城市名字
     * @return
     *          返回城市数据List<city>，有可能是某省的所有城市,或单一城市
     */
    public List<CityList> loadCity(String name) {
        List<CityList> cityLists=new ArrayList<CityList>();
        CityList city = new CityList();
        Cursor cursor;
        //查询用户输入的是否是省名
        if ((cursor = db.rawQuery("select * from CityList where province=?"
                , new String[]{name})).moveToFirst()) {
           do{
                city = new CityList();
                city.setId(cursor.getInt(cursor.getColumnIndex("id")));
                city.setProvince(cursor.getString(cursor.getColumnIndex("province")));
                city.setCity(cursor.getString(cursor.getColumnIndex("city")));
                city.setDistrict(cursor.getString(cursor.getColumnIndex("district")));
               cityLists.add(city);
            }while (cursor.moveToNext());
        }
        //用户输入的是城市名
        else if ((cursor = db.rawQuery("select * from CityList where district=?"
                , new String[]{name})).moveToFirst()) {
                city = new CityList();
                city.setId(cursor.getInt(cursor.getColumnIndex("id")));
                city.setProvince(cursor.getString(cursor.getColumnIndex("province")));
                city.setCity(cursor.getString(cursor.getColumnIndex("city")));
            city.setDistrict(cursor.getString(cursor.getColumnIndex("district")));
                if (city.getDistrict().equals(name) || city.getDistrict().contains(name)) {
                    cityLists.add(city);
                }
        };
            cursor.close();
            return cityLists;
        }
}

