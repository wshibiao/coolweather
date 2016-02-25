package com.coolweather.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.coolweather.app.R;
import com.coolweather.model.FutureWeather;

import java.util.List;

/**
 * Created by wsb on 2016/1/21.
 */
public class FutureWeatherAdapter extends ArrayAdapter<FutureWeather>{
    private int id;
    public FutureWeatherAdapter(Context context,int textViewResourceId,List<FutureWeather> object){
        super(context,textViewResourceId,object);
        id=textViewResourceId;
    }

    @Override
    public View getView(int position,View covertView,ViewGroup parent){
        FutureWeather futureWeather1=getItem(position);
        View view;
        ViewHolder viewHolder;
        if(covertView==null){
            view= LayoutInflater.from(getContext()).inflate(R.layout.future_weather_item,null);
            viewHolder=new ViewHolder();
            viewHolder.week_future=(TextView)view.findViewById(R.id.index_week_future);
            viewHolder.weather_future=(ImageView)view.findViewById(R.id.index_future_weather);
            viewHolder.temRange=(TextView)view.findViewById(R.id.index_future_temperature);
            view.setTag(viewHolder);
        }else {

            view=covertView;
            viewHolder=(ViewHolder)view.getTag();
        }
        viewHolder.week_future.setText(futureWeather1.getWeek());
        viewHolder.weather_future.setImageResource((futureWeather1.getImageId()));
        viewHolder.temRange.setText(futureWeather1.getTemperatureRange());
        return view;
    }

    class ViewHolder{
        TextView week_future;
        ImageView weather_future;
        TextView temRange;
    }
}
