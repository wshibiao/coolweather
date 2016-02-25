package com.coolweather.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.coolweather.adapter.FutureWeatherAdapter;
import com.coolweather.app.R;
import com.coolweather.model.FutureWeather;
import com.coolweather.service.AutoUpdateWeather;
import com.coolweather.util.Utility;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by wsb on 2016/1/16.
 */
public class MainActivity extends Activity {
    public static final String CITYLIST = "http://v.juhe.cn/weather/citys?key=6d660d7c817e5b57ab0afd636d336f3b";
    private static final String WEATHERTYPE="http://v.juhe.cn/weather/uni?key=6d660d7c817e5b57ab0afd636d336f3b";

    //控件
    private TextView city;
    private TextView weather;
    private TextView temperature;
    private TextView week;
    private TextView humidity;
    private TextView comfortable_index;
    private TextView dressingAadvice;
    private TextView dressing;
    private TextView wash;
    private TextView travel;
    private TextView exercise;
    private TextView temperatureRange;
    private TextView PM2;
    private TextView feltAir;
    private TextView drying ;
    private TextView wind;
    private TextView uvIndex;
    private Button leftMenu;
    private DrawerLayout drawerLayout;
    private LinearLayout mGallery;
    private LinearLayout weatherLayout;
//    private LinearLayout futureWeather;
    private Integer[] mImgIds;
    private int[] mImgIDs2;
    private String cityName;
    ;
    private LayoutInflater mInflater;
//    private LayoutInflater futureInflater;
    private ListView mListView;

    private FutureWeatherAdapter mAdapter;
     private  List<FutureWeather> futureWeather=new ArrayList<>();
    private List<FutureWeather> futureWeatherList;

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
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.show_weather);
        mInflater = LayoutInflater.from(this);
//        futureInflater=LayoutInflater.from(this);
        initData();
        initView();


        leftMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                drawerLayout.openDrawer(Gravity.LEFT);
                Intent intent = new Intent(MainActivity.this, ChooseAreaActivity.class);
                startActivity(intent);
            }
        });
        processExtraData();
        Log.d("cityname", cityName + "1");
        mListView.setAdapter(mAdapter);
        showWeather();
    }
    private void initData()
    {
        futureWeatherList=new ArrayList<>();


        mImgIds = new Integer[] {R.drawable.d00 ,R.drawable.d01, R.drawable.d02, R.drawable.d03,
               R.drawable.d04, R.drawable.d05, R.drawable.d06,
                R.drawable.d07, R.drawable.d08 ,R.drawable.d09,R.drawable.d10,
                R.drawable.d11,R.drawable.d12,R.drawable.d13,R.drawable.d14,
                R.drawable.d15,R.drawable.d16,R.drawable.d17,R.drawable.d18,R.drawable.d19,
                R.drawable.d20,R.drawable.d21,R.drawable.d22,R.drawable.d23,R.drawable.d24,
                R.drawable.d25,R.drawable.d26,R.drawable.d27,R.drawable.d28,R.drawable.d29,
                R.drawable.d30,R.drawable.d31,R.drawable.d53};
        mImgIDs2=new int[]{
            R.drawable.d00, R.drawable.d01, R.drawable.d02,
                        R.drawable.d03, R.drawable.d04, R.drawable.d05};

    }

    private void initView() {
        weatherLayout = (LinearLayout) findViewById(R.id.weather_layout);
        city = (TextView) findViewById(R.id.cityName);
        weather = (TextView) findViewById(R.id.weather);
        temperature = (TextView) findViewById(R.id.temperature);
        week = (TextView) findViewById(R.id.week);
        temperatureRange = (TextView) findViewById(R.id.temp_range);
        humidity = (TextView) findViewById(R.id.tv_humidity);
        comfortable_index = (TextView) findViewById(R.id.comfortable);
        dressingAadvice = (TextView) findViewById(R.id.tv_dressing_advice_index);
        dressing = (TextView) findViewById(R.id.tv_dressing_index);
        wash = (TextView) findViewById(R.id.tv_wash_index);
        travel = (TextView) findViewById(R.id.tv_travel_index);
        exercise = (TextView) findViewById(R.id.tv_exercise_index);
        feltAir = (TextView) findViewById(R.id.tv_felt_air_temp);
        PM2 = (TextView) findViewById(R.id.tv_pm2_5_index);
        drying = (TextView) findViewById(R.id.tv_drying_index);
        wind = (TextView) findViewById(R.id.tv_wind);
        uvIndex = (TextView) findViewById(R.id.tv_uv_index);
        mListView = (ListView) findViewById(R.id.future_listview);
        mAdapter = new FutureWeatherAdapter(this, R.layout.future_weather_item, futureWeatherList);
        leftMenu = (Button) findViewById(R.id.left_menu);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mGallery = (LinearLayout) findViewById(R.id.id_gallery);
//        for (int i = 0; i < mImgIds.length; i++)
//        {
//            View view = mInflater.inflate(R.layout.forecast3h_weather_index_listitem,
//                    mGallery, false);
//            TextView t=(TextView) view.findViewById(R.id.index_time);
//            t.setText("4:45");
//
//            ImageView img = (ImageView) view
//                    .findViewById(R.id.image);
//            img.setImageResource(mImgIds[i]);
//            TextView txt = (TextView) view
//                    .findViewById(R.id.index_temperature);
//            txt.setText("15℃ ");
//            mGallery.addView(view);
//        }
        //解决根据返回的天气ID加载相应的图片，以及未来天气实体类的id类型
    }

//        public void initFuture(){
//        for (FutureWeather fu:futureWeather){
//            int id=fu.getImageId();
//            fu.setImageId(mImgIds[id]);
//            futureWeatherList.add(fu);
//            mAdapter.notifyDataSetChanged();
//        }
//            mListView.setAdapter(mAdapter);
//        }
//        for (int i = 0; i < mImgIDs2.length; i++)
//        {
//            FutureWeather futureWeather=new FutureWeather();
//            futureWeather.setWeek("星期一");
//            futureWeather.setImageId(mImgIDs2[i]);
//            futureWeather.setTemperatureRange("15-20℃");
//            futureWeatherList.add(futureWeather);
//            mAdapter.notifyDataSetChanged();
//        }




    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);//must store the new intent unless getIntent() will return the old one
        processExtraData();
    }
    @Override
    protected void onResume() {
        super.onResume();
        cityName=getIntent().getStringExtra("temp");
        queryWeather(cityName);
    }

    private void processExtraData(){

        Intent intent = getIntent();

        //use the data received here

    }
    public void queryWeather(String name){
        String address = "http://v.juhe.cn/weather/index?format=2&cityname=" + name
                + "&key=6d660d7c817e5b57ab0afd636d336f3b";

        RequestQueue mQueue= Volley.newRequestQueue(this);
        JsonObjectRequest objectRequest=new JsonObjectRequest(address, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject object) {
                        Utility.handleTodayWeatherResponse(MainActivity.this, object);
                        Utility.handleRecentlyWeatherResponse(MainActivity.this, object);
                        Utility.handleFutureWeatherResponse(MainActivity.this,object);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showWeather();
                            }
                        });
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("TAG", volleyError.getMessage(), volleyError);
            }
        });
        mQueue.add(objectRequest);
    }

//    editor.putString("city",city);
//    editor.putString("温度范围",temp);
//    editor.putString("天气",weather);
//    editor.putString("dressing_index",dressing_index);
//    editor.putString("dressing_advice",dressing_advice);
//    editor.putString("星期",week);
//    editor.putString("日期",date_y);
//    editor.putString("紫外线",uv_index);
//    editor.putString("舒适度",comfort_index);
//    editor.putString("洗车指数",wash_index);
//    editor.putString("旅游指数",travel_index);
//    editor.putString("锻炼指数",exercise_index);
//    editor.putString("干燥指数",drying_index);
//    object=object.getJSONObject("weather_id");
//    editor.putString("fa", object.getString("fa"));
//    editor.putString("fb", object.getString("fb"));
    public void showWeather() {
        SharedPreferences preferences = getSharedPreferences("today", MODE_PRIVATE);
        city.setText(preferences.getString("city", ""));
        weather.setText(preferences.getString("天气", ""));
        week.setText(preferences.getString("星期", ""));
        uvIndex.setText(preferences.getString("紫外线", ""));
        temperatureRange.setText(preferences.getString("温度范围", ""));
        comfortable_index.setText(preferences.getString("舒适度", ""));
        wash.setText(preferences.getString("洗车指数", ""));
        travel.setText(preferences.getString("旅游指数", ""));
        exercise.setText(preferences.getString("锻炼指数", ""));
        drying.setText(preferences.getString("干燥指数", ""));
        dressing.setText(preferences.getString("dressing_index", ""));
        dressingAadvice.setText(preferences.getString("dressing_advice", ""));
        ;
        preferences = getSharedPreferences("recentlyWeather", MODE_PRIVATE);
        wind.setText(preferences.getString("当前风向", "") + preferences.getString("当前风力", ""));
        humidity.setText(preferences.getString("当前湿度", ""));
        temperature.setText(preferences.getString("当前温度", ""));
        initFuture();
        Intent intent=new Intent(MainActivity.this, AutoUpdateWeather.class);
        startService(intent);
//        Intent bindIntent=new Intent(this,AutoUpdateWeather.class);
//        bindService(bindIntent, connection, BIND_AUTO_CREATE);
//        foregroundServiceBinder.updateForegroundService();
    }
    public void initFuture(){
        SharedPreferences preferences;
        futureWeatherList.clear();
        for (int i = 1; i < 7; i++) {
            preferences = getSharedPreferences("FutureWeather" + i, MODE_PRIVATE);
            FutureWeather futureWeather = new FutureWeather();

            futureWeather.setTemperatureRange(preferences.getString("温度",""));
            futureWeather.setWeek(preferences.getString("星期", ""));
            int id=Integer.valueOf(preferences.getString("fa", "0")).intValue();
                    futureWeather.setImageId(mImgIds[id]);
            futureWeather.setImageId2(mImgIds[Integer.parseInt(preferences.getString("fb","0"))]);
            futureWeatherList.add(futureWeather);
            mAdapter.notifyDataSetChanged();
        }
    }
}



