package com.coolweather.app.util;

import android.content.Context;
import android.content.SharedPreferences;
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


    /**
     *
     * @param context 上下文
     * @param response 返回的json数据
     * @return
     */
    public synchronized static boolean handleTodayWeatherResponse(Context context,String response) {
        if (!TextUtils.isEmpty(response)) {
            try {
                //使用JSONObject解析JSON数据
                JSONObject object = new JSONObject(response);//根据返回数据创建JSON对象
                object = object.getJSONObject("result");
                object = object.getJSONObject("today");

                String city=object.getString("city");
                String temp=object.getString("temperature");
                String weather =object.getString("weather");
                String dressing_index=object.getString("dressing_index");
                String dressing_advice=object.getString("dressing_advice");
                SharedPreferences.Editor editor = context.getSharedPreferences("todayWeather"
                        , context.MODE_PRIVATE).edit();
                editor.putString("city",city);
                editor.putString("温度范围",temp);
                editor.putString("天气",weather);
                editor.putString("dressing_index",dressing_index);
                editor.putString("dressing_advice",dressing_advice);
                editor.commit();


//                    Log.d("today is ",object.getString("date_y"));
//                    Log.d("今天是: ",object.getString("week"));
//                    Log.d("气温是: ", object.getString("temperature"));
//                    Log.d("风向是 ",object.getString("wind"));
//                    Log.d("穿衣指数",object.getString("dressing_index"));
//                    Log.d("穿衣建议",object.getString("dressing_advice"));
                //实况天气


            }
            catch(Exception e){
            e.printStackTrace();
        }
        return true;
    }
        return false;
    }


    /**
     * 点击更新时调用显示当前实况天气.
     * @param context
     * @param response
     * @return
     */
    public synchronized static void handleRecentlyWeatherResponse(Context context,String response) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONObject object=new JSONObject(response);
                object=object.getJSONObject("result");
                object=object.getJSONObject("sk");
                SharedPreferences.Editor editor=context.getSharedPreferences("recentlyWeather"
                        ,context.MODE_PRIVATE).edit();
                editor.putString("当前温度",object.getString("temp"));
                editor.putString("当前风向", object.getString("wind_direction"));
                editor.putString("当前风力",object.getString("wind_strength"));
                editor.putString("当前湿度",object.getString("humidity"));
                editor.putString("更新时间", object.getString("time"));
                editor.commit();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public synchronized static void handleFeatureWeatherResponse(Context context,String response) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONObject object=new JSONObject(response);
                object=object.getJSONObject("result");
                object=object.getJSONObject("sk");

                SharedPreferences.Editor editor=context.getSharedPreferences("recentlyWeather"
                        ,context.MODE_PRIVATE).edit();

                JSONArray jsonArray=object.getJSONArray("future");
                for(int i=0;i<jsonArray.length();i++) {
                    object=(JSONObject)jsonArray.opt(i);
                    editor.putString("日期 ", object.getString("date"));
                    editor.putString("温度：", object.getString("temperature"));
                    editor.putString("天气：", object.getString("weather"));
                    editor.putString("星期 : ", object.getString("week"));
                    editor.commit();
                };
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
