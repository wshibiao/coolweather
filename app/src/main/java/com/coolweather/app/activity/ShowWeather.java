package com.coolweather.app.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.coolweather.app.R;
import com.coolweather.app.db.CoolWeatherOpenHelper;
import com.coolweather.app.util.HttpCallbackListener;
import com.coolweather.app.util.HttpUtil;
import com.coolweather.app.util.Utility;

/**
 * Created by wsb on 2015/12/31.
 */
public class ShowWeather extends Activity implements View.OnClickListener {
    private static final String WEATHERTYPE = "http://v.juhe.cn/weather/uni?key=6d660d7c817e5b57ab0afd636d336f3b";

    //控件
    TextView temperature;
    TextView wind;
    TextView temperatureRange;
    TextView city;
    TextView upTime;
    TextView humdity;
    TextView weather;
    Button update;
    Button switchCity;
    RelativeLayout weatherLayout;
    ProgressDialog progressDialog;

    SQLiteDatabase db;
    CoolWeatherOpenHelper dbHelper;
    String cityName;

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.show_weather);
        temperature = (TextView) findViewById(R.id.temperature);
        wind = (TextView) findViewById(R.id.wind_direction);
        temperatureRange = (TextView) findViewById(R.id.temp_range);
        city = (TextView) findViewById(R.id.city);
        upTime = (TextView) findViewById(R.id.update_time);
        humdity=(TextView)findViewById(R.id.humdity);
        update = (Button) findViewById(R.id.refresh_weather);
        switchCity=(Button)findViewById(R.id.addCity);
        weather=(TextView)findViewById(R.id.weather);
        cityName=getIntent().getStringExtra("CityName");
        weatherLayout=(RelativeLayout)findViewById(R.id.weather_info);
        update.setOnClickListener(this);
        switchCity.setOnClickListener(this);
        showWeather();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.addCity:
                Intent intent=new Intent(ShowWeather.this,ChooseAreaActivity.class);
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
        weather.setText(preferences.getString("天气","")); weatherLayout.setVisibility(View.VISIBLE);
    }

    public void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("正在加载");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    public void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}
