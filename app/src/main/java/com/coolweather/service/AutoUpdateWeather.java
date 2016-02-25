package com.coolweather.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.coolweather.activity.MainActivity;
import com.coolweather.app.R;
import com.coolweather.receiver.AutoUpdataReceiver;
import com.coolweather.util.Utility;

import org.json.JSONObject;


/**
 * Created by wsb on 2016/1/2.
 */
public class AutoUpdateWeather extends Service {
    private NotificationCompat.Builder notif;
    private ForegroundServiceBinder mBinder = new ForegroundServiceBinder();
    public class ForegroundServiceBinder extends Binder {
        public void updateForegroundService() {
            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
            PendingIntent pendingIntent=PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
            SharedPreferences preferences1=getSharedPreferences("recentlyWeather",MODE_PRIVATE);
            SharedPreferences preferences=getSharedPreferences("today",MODE_PRIVATE);
            notif=new NotificationCompat.Builder(getApplicationContext());
            notif.setContentTitle(preferences1.getString("当前温度","__")+" " +preferences.getString("温度范围","__")
                    +"        "+preferences1.getString("当前风向", "__")+preferences1.getString("当前风力","__"))
                    .setSmallIcon(R.drawable.d01)
                    .setContentText(preferences.getString("天气","__")+"   "+preferences.getString("city","__")+"     "+preferences1.getString("更新时间","__")+"更新")
                    .setContentIntent(pendingIntent);
            startForeground(1,notif.build());
        }

        public int getProgress() {
            Log.d("MyService", "getProgress executed");
            return 0;
        }
    }
    public IBinder onBind(Intent intent){

        return mBinder;
    }


    @Override
    public void onCreate(){


    }


    @Override
    public int onStartCommand(Intent intent,int flags,int startId){
        new Thread(new Runnable() {
            @Override
            public void run() {
                updateWeather();
            }
        }).start();
        AlarmManager manager=(AlarmManager)getSystemService(ALARM_SERVICE);
        int anHour=8*60*60*1000;
        Long triggerAtTime= SystemClock.elapsedRealtime()+anHour;
        Intent i=new Intent(this,AutoUpdataReceiver.class);
        PendingIntent pendingIntent=PendingIntent.getBroadcast(this,0,i,0);
        manager.set(AlarmManager.ELAPSED_REALTIME, triggerAtTime, pendingIntent);
        return super.onStartCommand(intent,flags,startId);
    }

    public void updateWeather(){

            SharedPreferences preferences = getSharedPreferences("today", MODE_PRIVATE);
            String cityName = preferences.getString("city", null);
            String address = "http://v.juhe.cn/weather/index?format=2&cityname=" + cityName
                    + "&key=6d660d7c817e5b57ab0afd636d336f3b";
//            HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
//                @Override
//                public void onFinish(String response) {
//                    Utility.handleRecentlyWeatherResponse(AutoUpdateWeather.this, response);
//                    Utility.handleTodayWeatherResponse(AutoUpdateWeather.this, response);
//                }
//
//                @Override
//                public void onError(Exception e) {
//                    e.printStackTrace();
//                }
//            });
//        }catch (Exception e){
//            e.printStackTrace();
//        }
            RequestQueue mQueue = Volley.newRequestQueue(this);
            JsonObjectRequest objectRequest = new JsonObjectRequest(address, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject object) {
                            Utility.handleTodayWeatherResponse(AutoUpdateWeather.this, object);
                            Utility.handleRecentlyWeatherResponse(AutoUpdateWeather.this, object);
                            Utility.handleFutureWeatherResponse(AutoUpdateWeather.this, object);

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Log.e("TAG", volleyError.getMessage(), volleyError);
                }
            });
            mQueue.add(objectRequest);
        }


    public void deskService(){
        Intent intent=new Intent(this,MainActivity.class);
        PendingIntent pendingIntent=PendingIntent.getActivity(this, 0, intent, 0);
        SharedPreferences preferences1=getSharedPreferences("recentlyWeather",MODE_PRIVATE);
        SharedPreferences preferences=getSharedPreferences("todayWeather",MODE_PRIVATE);
        NotificationCompat.Builder notif=new NotificationCompat.Builder(this);
        notif.setContentTitle(preferences1.getString("当前温度","")+" " +preferences.getString("温度范围","")
                +"        "+preferences1.getString("当前风向", "")+preferences1.getString("当前风力",""))
                .setSmallIcon(R.drawable.d01)
                .setContentText(preferences.getString("天气","")+"   "+preferences.getString("city","")+"     "+preferences1.getString("更新时间","")+"更新")
                .setContentIntent(pendingIntent);
        startForeground(1,notif.build());
    }
}
