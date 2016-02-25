package com.coolweather.activity;

/**
 * Created by wsb on 2016/1/16.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.coolweather.adapter.MyAdapter;
import com.coolweather.app.R;
import com.coolweather.db.CoolWeatherDB;
import com.coolweather.model.CityList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wsb on 2015/12/25.
 */
public class ChooseAreaActivity extends Activity {

    //控件2
    private ProgressDialog progressDialog;
    private ListView listView;
    private EditText cityText;
    private SwipeRefreshLayout refreshLayout;
    //数据成员
    private MyAdapter adapter;
    private CoolWeatherDB db;
    private List<CityList> dataList;
    private String cityName;


    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.choose_area);
        listView = (ListView) findViewById(R.id.list_view);
        //获取城市名用于查询
        cityText = (EditText) findViewById(R.id.edit_query);
       dataList = new ArrayList<>();

        adapter = new MyAdapter(this, R.layout.item_listview, dataList);
        db = CoolWeatherDB.getInstance(this);


        cityText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                cityName = cityText.getText().toString();
                queryCities(cityName);
                if (s.length() == 0) {
                    dataList.clear();
                    adapter.notifyDataSetChanged();
                }

            }
        });
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CityList district = dataList.get(position);

                Log.d("choose cityname", cityName + "1");
                Intent intent = new Intent(ChooseAreaActivity.this, MainActivity.class);
                intent.putExtra("temp", district.getDistrict());
                startActivity(intent);
//                Intent intent=new Intent(ChooseAreaActivity.this, MainActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putString("CityName", district.getDistrict());
//                intent.putExtras(bundle);
//
//                startActivity(intent);
                finish();
            }
        });

    }




    /**
     * 查询城市
     * @param cityName
     *
     */


    private void queryCities(String cityName) {
        if (!TextUtils.isEmpty(cityName)) {
            dataList.clear();
            List<CityList> cityList = db.loadCity(cityName);
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            if (!cityList.isEmpty()) {
                for (CityList city : cityList) {
                    dataList.add(city);
                }
            } else {
                Toast.makeText(ChooseAreaActivity.this, "没有该城市天气数据", Toast.LENGTH_SHORT).show();
            }
        }

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
    /**
     * 捕获back按键，根据级别来判断下一步操作
     */

//    @Override
//    public void onBackPressed () {
//        if(cityName==null){
//            Intent intent=new Intent(ChooseAreaActivity.this,ShowWeather.class);
//            startActivity(intent);
//        }
//        finish();
//    }
}



