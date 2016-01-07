package com.coolweather.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.coolweather.app.model.CityList;

import java.util.List;

/**
 * Created by wsb on 2016/1/6.
 */
public class MyAdapter extends ArrayAdapter<CityList> {
    private int id;
    public MyAdapter(Context context,int textViewResourceId,List<CityList> object){
        super(context,textViewResourceId,object);
        id=textViewResourceId;
    }

    @Override
    public View getView(int position,View covertView,ViewGroup parent){
        CityList city=getItem(position);
        View view;
        ViewHolder viewHolder;
        if(covertView==null){
            view= LayoutInflater.from(getContext()).inflate(R.layout.item_listview,null);
            viewHolder=new ViewHolder();
            viewHolder.province=(TextView)view.findViewById(R.id.province);
            viewHolder.city=(TextView)view.findViewById(R.id.city);
            viewHolder.district=(TextView)view.findViewById(R.id.district);
            view.setTag(viewHolder);
        }else {

            view=covertView;
            viewHolder=(ViewHolder)view.getTag();
        }
        viewHolder.province.setText(city.getProvince()+"-");
        viewHolder.city.setText(city.getCity()+"-");
        viewHolder.district.setText(city.getDistrict());
        return view;
    }

    class ViewHolder{
        TextView province;
        TextView city;
        TextView district;
    }
}
