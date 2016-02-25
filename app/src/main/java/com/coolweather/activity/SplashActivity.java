package com.coolweather.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Window;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.coolweather.app.R;
import com.coolweather.db.CoolWeatherDB;
import com.coolweather.util.Utility;

import org.json.JSONObject;




/**
 * Created by wsb on 2016/1/13.
 */
public class SplashActivity extends Activity {
    //城市列表API地址
    private static final int SHOW_TIME_MIN=3000;
    public static final int CITY_LIST_SCUESS=1;
    public static final String CITYLIST = "http://v.juhe.cn/weather/citys?key=6d660d7c817e5b57ab0afd636d336f3b";
    private long mStartTime;
    private CoolWeatherDB db;
//    public static final String HTTPURL = "http://v.juhe.cn/weather/citys?key=6d660d7c817e5b57ab0afd636d336f3b";
//    private boolean is_first;
    private SharedPreferences preferences;
//    private ImageView imageView;
//

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // do something
            switch (msg.what) {
                case 0:
                    Intent intent=new Intent(SplashActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                   break;
                case 1:
                    Intent intentFast=new Intent(SplashActivity.this,MainActivity.class);
                    startActivity(intentFast);
                    finish();
                    break;
                default:
                    break;

            }

        }
    };

//    Runnable goToMainActivity = new Runnable() {
//        @Override
//        public void run() {
//           Intent intent=new Intent(SplashActivity.this,MainActivity.class);
//            startActivity(intent);
//            finish();
//        }
//    };
    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.splash_layout);
        db= CoolWeatherDB.getInstance(this);
//        new Handler(){
//            public void handleMessage(Message msg) {
//                        SharedPreferences preferences=getSharedPreferences("is_first",MODE_PRIVATE);
//                        if(preferences.getBoolean("is_first",true)){
//                            loadCity();
//                            postDelayed(goToMainActivity, 8000);
//                            preferences.edit().putBoolean("is_first",false);
//                            preferences.edit().commit();
//                        }else {
//                            post(goToMainActivity);
//                        }
//
//                }
//
//        };
//        new Handler() {
//            public void handleMessage(Message msg) {
//                    Intent intent=new Intent(SplashActivity.this,MainActivity.class);
//                    startActivity(intent);
//
//                finish();
//            };
//        }.sendEmptyMessageDelayed(0,4000);
        firstRun();
    }






    public void firstRun(){
        SharedPreferences preferences=getSharedPreferences("is_first",MODE_PRIVATE);
        if(preferences.getBoolean("is_first",true)){
            loadCity();
            SharedPreferences.Editor editor = getSharedPreferences("is_first",
                    MODE_PRIVATE).edit();
           editor.putBoolean("is_first", false);
           editor.commit();
            Message message=new Message();
            message.what=0;
            handler.sendMessageDelayed(message,8000);

        }else {
            Message message=new Message();
            message.what=1;
            handler.sendMessageDelayed(message,2000);
        }
    }

    public void loadCity(){
        RequestQueue mQueue = Volley.newRequestQueue(this);
        JsonObjectRequest objectRequest=new JsonObjectRequest(CITYLIST, null
                , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject obj) {
                Utility.handleProvincesResponseWithGSON(db, obj);
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
