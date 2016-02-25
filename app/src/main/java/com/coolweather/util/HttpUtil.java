package com.coolweather.util;

import android.content.Context;
import android.util.Log;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by wsb on 2015/12/25.
 */
public class HttpUtil {
    /**
     * 获取城市列表
     * @param httpUrl
     *            :请求接口
     * @param listener
     *              回调函数监听
     *
     */

    public static void sendHttpRequest(final String httpUrl, final HttpCallbackListener listener) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    StringBuilder response=new StringBuilder();//携带json数据
                   //使用HttpClient访问API获取JSON数据

                    URL url=new URL(httpUrl);
                    HttpURLConnection connection=(HttpURLConnection)url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    InputStream in=connection.getInputStream();
                    BufferedReader reader=new BufferedReader(new InputStreamReader(in));
                    String line;
                    while((line=reader.readLine())!=null){
                        response.append(line);
                    }

                        if(listener!=null){
                            listener.onFinish(response);
                        }

                } catch (Exception e) {
                    listener.onError(e);
                }finally {

                }
            }
        }).start();

    }
    public static void volley(Context context,final String httpUrl){
        RequestQueue mQueue = Volley.newRequestQueue(context);
        JsonObjectRequest objectRequest=new JsonObjectRequest(httpUrl, null
                , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject obj) {
//                try {
//                    JSONObject object = obj.getJSONObject("result");
//                    JSONArray jsonArray=object.getJSONArray("future");
//                    for(int i=0;i<jsonArray.length();i++) {
//                        object = (JSONObject) jsonArray.opt(i);
//                        Log.d("weather_id", object.getString("weather_id"));
//                        Log.d("date ", object.getString("date"));
//                        Log.d("温度：", object.getString("temperature"));
//                        Log.d("天气：", object.getString("weather"));
//                        Log.d("week : ", object.getString("week"));
//
//                        object = object.getJSONObject("weather_id");
//                        Log.d("fa", object.getString("fa"));
//                        Log.d("fb", object.getString("fb"));
//                    }
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("TAG", volleyError.getMessage(), volleyError);
            }
        });
        mQueue.add(objectRequest);
    }
}