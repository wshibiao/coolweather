package com.coolweather.app.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.coolweather.app.R;
import com.coolweather.app.service.AutoUpdateWeather;
import com.coolweather.app.util.HttpCallbackListener;
import com.coolweather.app.util.HttpUtil;
import com.coolweather.app.util.Utility;

/**
 * Created by wsb on 2015/12/31.
 */
public class ShowWeather extends Activity implements View.OnClickListener,SwipeRefreshLayout.OnRefreshListener{
    private static final String WEATHERTYPE = "http://v.juhe.cn/weather/uni?key=6d660d7c817e5b57ab0afd636d336f3b";

    //控件
    private TextView temperature;
    private TextView wind;
    private TextView temperatureRange;
    private TextView city;
    private  TextView upTime;
    private  TextView humdity;
    private TextView weather;
    private Button update;
    private Button switchCity;
    private RelativeLayout weatherLayout;
    private String cityName;
    //下拉刷新相关
    private SwipeRefreshLayout refreshLayout;
    private ScrollView scrollView;
    private View childView;


    private AutoUpdateWeather.ForegroundServiceBinder foregroundServiceBinder;
    private ServiceConnection connection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            foregroundServiceBinder=(AutoUpdateWeather.ForegroundServiceBinder) service;
            foregroundServiceBinder.updateForegroundService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.swiprefresh_layout);
        temperature = (TextView) findViewById(R.id.temperature);
        wind = (TextView) findViewById(R.id.wind_direction);
        temperatureRange = (TextView) findViewById(R.id.temp_range);
        city = (TextView) findViewById(R.id.city);
        upTime = (TextView) findViewById(R.id.update_time);
        humdity=(TextView)findViewById(R.id.humdity);
        update = (Button) findViewById(R.id.refresh_weather);
        switchCity=(Button)findViewById(R.id.addCity);
        weather=(TextView)findViewById(R.id.weather);

        refreshLayout=(SwipeRefreshLayout)findViewById(R.id.refresh_layout);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        scrollView=(ScrollView)findViewById(R.id.scroll_view);
        weatherLayout=(RelativeLayout)findViewById(R.id.weather_layout);
        childView=getLayoutInflater().inflate(R.layout.show_weather,null);
        scrollView.addView(childView);
        cityName=getIntent().getStringExtra("CityName");

        update.setOnClickListener(this);
        switchCity.setOnClickListener(this);
    }
    @Override
    public void onRefresh(){
        queryWeather(cityName);
        showWeather();
    }
    @Override
    public void onResume(){
        super.onResume();
        queryWeather(cityName);
        showWeather();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.addCity:
                Intent intent=new Intent(ShowWeather.this,ChooseAreaActivity.class);
                intent.putExtra("is_from_weather_activity",true);
                startActivity(intent);

                finish();
                break;
            case  R.id.refresh_weather:
                queryWeather(cityName);
                showWeather();
                break;
            default:
                break;
        }
    }

    /**
     * 用户输入城市名 查询获得城市 listView点击事件->根据天气查询API获取天气信息->根据天气ID查询天气类型->首先查询数据库->没有则通过网络查询完毕后显示在界面上
     */

    public void queryWeather(String cityName) {
        if (!TextUtils.isEmpty(cityName)) {
            String address = "http://v.juhe.cn/weather/index?format=2&cityname=" + cityName
                    + "&key=6d660d7c817e5b57ab0afd636d336f3b";
            HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
                @Override
                public void onFinish(String response) {
                    Utility.handleRecentlyWeatherResponse(ShowWeather.this,response);
                    Utility.handleTodayWeatherResponse(ShowWeather.this,response);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showWeather();
                        }
                    });
                }

                @Override
                public void onError(Exception e) {

                }
            });
        }

    }

    public void queryWeatherId(int id) {

    }

    public void queryWeatherIdFromServer(int id) {

    }
    public void showWeather(){
        SharedPreferences preferences=getSharedPreferences("recentlyWeather",MODE_PRIVATE);

        temperature.setText(preferences.getString("当前温度","")+"℃");
        wind.setText(preferences.getString("当前风向", "")+preferences.getString("当前风力",""));

        humdity.setText("湿度:"+preferences.getString("当前湿度",""+"％"));
        upTime.setText(preferences.getString("更新时间","")+"更新");

        preferences=getSharedPreferences("todayWeather",MODE_PRIVATE);
        city.setText(preferences.getString("city",""));
        temperatureRange.setText(preferences.getString("温度范围",""));
        weather.setText(preferences.getString("天气",""));
        weatherLayout.setVisibility(View.VISIBLE);
        Intent intent=new Intent(ShowWeather.this, AutoUpdateWeather.class);
        startService(intent);
        Intent bindIntent=new Intent(this,AutoUpdateWeather.class);
        bindService(bindIntent, connection, BIND_AUTO_CREATE);
        foregroundServiceBinder.updateForegroundService();
    }


}
