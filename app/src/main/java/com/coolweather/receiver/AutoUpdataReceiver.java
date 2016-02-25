package com.coolweather.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.coolweather.service.AutoUpdateWeather;


/**
 * Created by wsb on 2016/1/2.
 */
public class AutoUpdataReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context,Intent intent){
        Intent i=new Intent(context, AutoUpdateWeather.class);
        context.startService(i);
    }
}
