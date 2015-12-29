package com.coolweather.app.util;

import android.text.TextUtils;

import com.coolweather.app.db.CoolWeatherDB;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by wsb on 2015/12/25.
 */
public class Utility {
    /**
     * 解析和处理服务器返回的省级数据
     */
    public synchronized static boolean handleProvincesResponseWithGSON(CoolWeatherDB coolWeatherDB, String response) {
        if (!TextUtils.isEmpty(response)) {
            try {
                //使用JSONObject解析JSON数据
                JSONObject object = new JSONObject(response);//根据返回数据创建JSON对象
                JSONArray jsonArray=object.getJSONArray("result");//根据键名获取JSON数组并遍历
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject obj=(JSONObject)jsonArray.opt(i);//遍历JSON数组并调用saveCity存入数据库中
                    coolWeatherDB.saveCity(obj);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }
}
