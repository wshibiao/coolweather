package com.coolweather.app.service;

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

import com.coolweather.app.R;
import com.coolweather.app.activity.ShowWeather;
import com.coolweather.app.receiver.AutoUpdataReceiver;
import com.coolweather.app.util.HttpCallbackListener;
import com.coolweather.app.util.HttpUtil;
import com.coolweather.app.util.Utility;

/**
 * Created by wsb on 2016/1/2.
 */
public class AutoUpdateWeather extends Service {
    private NotificationCompat.Builder notif;
    private ForegroundServiceBinder mBinder = new ForegroundServiceBinder();
    public class ForegroundServiceBinder extends Binder {
        public void updateForegroundService() {
            Intent intent=new Intent(getApplicationContext(),ShowWeather.class);
            PendingIntent pendingIntent=PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
            SharedPreferences preferences1=getSharedPreferences("recentlyWeather",MODE_PRIVATE);
            SharedPreferences preferences=getSharedPreferences("todayWeather",MODE_PRIVATE);
            notif=new NotificationCompat.Builder(getApplicationContext());
            notif.setContentTitle(preferences1.getString("当前温度","")+" " +preferences.getString("温度范围","")
                    +"        "+preferences1.getString("当前风向", "")+preferences1.getString("当前风力",""))
                    .setSmallIcon(R.drawable.d01)
                    .setContentText(preferences.getString("天气","")+"   "+preferences.getString("city","")+"     "+preferences1.getString("更新时间","")+"更新")
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
        try {
            SharedPreferences preferences=getSharedPreferences("todayWeather",MODE_PRIVATE);
            String cityName=preferences.getString("city", null);
            String address = "http://v.juhe.cn/weather/index?format=2&cityname=" + cityName
                    + "&key=6d660d7c817e5b57ab0afd636d336f3b";
            HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
                @Override
                public void onFinish(String response) {
                    Utility.handleRecentlyWeatherResponse(AutoUpdateWeather.this,response);
                    Utility.handleTodayWeatherResponse(AutoUpdateWeather.this,response);
                }

                @Override
                public void onError(Exception e) {
                    e.printStackTrace();
                }
             });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void deskService(){
        Intent intent=new Intent(this,ShowWeather.class);
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
