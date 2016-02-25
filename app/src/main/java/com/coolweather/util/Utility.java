package com.coolweather.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import com.coolweather.db.CoolWeatherDB;
import org.json.JSONArray;
import org.json.JSONObject;



/**
 * Created by wsb on 2015/12/25.
 */
public class Utility {
    /**
     * 解析和处理服务器返回的省级数据
     */
    public synchronized static boolean handleProvincesResponseWithGSON(CoolWeatherDB coolWeatherDB,JSONObject obj) {

            try {
                //使用JSONObject解析JSON数据

                JSONArray jsonArray=obj.getJSONArray("result");//根据键名获取JSON数组并遍历
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject object=(JSONObject)jsonArray.opt(i);//遍历JSON数组并调用saveCity存入数据库中
                    coolWeatherDB.saveCity(object);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return true;


    }


    /**
     *
     * @param context 上下文

     * @return
     */
    public synchronized static void handleTodayWeatherResponse(Context context,JSONObject obj) {

            try {
                //使用JSONObject解析JSON数据

                JSONObject object = obj.getJSONObject("result");
                object = object.getJSONObject("today");
//                "today": {
//                    "city": "天津",
//                            "date_y": "2014年03月21日",
//                            "week": "星期五",
//                            "temperature": "8℃~20℃",	/*今日温度*/
//                            "weather": "晴转霾",	/*今日天气*/
//                            "weather_id": {	/*天气唯一标识*/
//                        "fa": "00",	/*天气标识00：晴*/
//                                "fb": "53"	/*天气标识53：霾 如果fa不等于fb，说明是组合天气*/
//                    },
//
// wind": "西南风微风",
//                "dressing_index": "较冷", /*穿衣指数*/
//                        "dressing_advice": "建议着大衣、呢外套加毛衣、卫衣等服装。",	/*穿衣建议*/
//                        "uv_index": "中等",	/*紫外线强度*/
//                        "comfort_index": "",/*舒适度指数*/
//                        "wash_index": "较适宜",	/*洗车指数*/
//                        "travel_index": "适宜",	/*旅游指数*/
//                        "exercise_index": "较适宜",	/*晨练指数*/
//                        "drying_index": ""/*干燥指数*/
                String city=object.getString("city");
                String date_y=object.getString("date_y");
                String week=object.getString("week");
                String temp=object.getString("temperature");
                String weather =object.getString("weather");
                String dressing_index=object.getString("dressing_index");
                String dressing_advice=object.getString("dressing_advice");
                String uv_index=object.getString("uv_index");/*紫外线强度*/
                String comfort_index=object.getString("comfort_index");/*舒适度指数*/
                String wash_index=object.getString("wash_index");/*洗车指数*/
                String travel_index=object.getString("travel_index");/*旅游指数*/
                String exercise_index=object.getString("exercise_index");/*晨练指数*/
                String drying_index=object.getString("drying_index");/*干燥指数*/
                SharedPreferences.Editor editor = context.getSharedPreferences("today"
                        , context.MODE_PRIVATE).edit();
                editor.putString("city",city);
                editor.putString("温度范围",temp);
                editor.putString("天气",weather);
                editor.putString("dressing_index",dressing_index);
                editor.putString("dressing_advice",dressing_advice);
                editor.putString("星期",week);
                editor.putString("日期",date_y);
                editor.putString("紫外线",uv_index);
                editor.putString("舒适度",comfort_index);
                editor.putString("洗车指数",wash_index);
                editor.putString("旅游指数",travel_index);
                editor.putString("锻炼指数",exercise_index);
                editor.putString("干燥指数",drying_index);
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

    }




    /**
     * 点击更新时调用显示当前实况天气.
     * @param context
     *
     * @return
     */
    public synchronized static void handleRecentlyWeatherResponse(Context context,JSONObject obj) {

            try {

                JSONObject object=obj.getJSONObject("result");
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


    public static synchronized  void handleFutureWeatherResponse(Context context,JSONObject obj) {

            try {


                JSONObject object=obj.getJSONObject("result");
                JSONArray jsonArray=object.getJSONArray("future");

                for(int i=1;i<jsonArray.length();i++) {
                    SharedPreferences.Editor editor=context.getSharedPreferences("FutureWeather"+i
                            ,context.MODE_PRIVATE).edit();
                    object=(JSONObject)jsonArray.opt(i);
                    editor.putString("日期", object.getString("date"));
                    editor.putString("温度", object.getString("temperature"));
                    editor.putString("天气", object.getString("weather"));
                    editor.putString("星期", object.getString("week"));
                    object = object.getJSONObject("weather_id");
                    Log.d("fa",object.getString("fa"));
                    Log.d("fb",object.getString("fb"));
                    editor.putString("fa", object.getString("fa"));
                    editor.putString("fb", object.getString("fb"));
                    editor.commit();
                };
            } catch (Exception e) {
                e.printStackTrace();
            }

        }


}
