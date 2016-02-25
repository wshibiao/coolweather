package com.coolweather.adapter;//package com.coolweather.app.adapter;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.TextView;
//
//import com.coolweather.app.R;
//import com.coolweather.app.model.CityItem;
//
//import java.util.List;
//
///**
// * Created by wsb on 2016/1/15.
// */
//public class LeftMenuAdapter extends ArrayAdapter<CityItem>{
//    public LeftMenuAdapter(Context context ,int textViewResourceId,List<CityItem> object){
//        super(context,textViewResourceId,object);
//    };
//
//    @Override
//    public View getView(int position,View convertView,ViewGroup parent){
//        CityItem cityItem=getItem(position);
//        View view;
//        ViewHolder viewHolder;
//        if(convertView==null){
//            view= LayoutInflater.from(getContext()).inflate(R.layout.item_listview,null);
//            viewHolder=new ViewHolder();
//            viewHolder.city=(TextView)view.findViewById(R.id.city_list);
//            viewHolder.temperature=(TextView)view.findViewById(R.id.temp_list);
//            view.setTag(viewHolder);
//        }else {
//            view=convertView;
//            viewHolder=(ViewHolder)view.getTag();
//        }
//        viewHolder.city.setText(cityItem.getCity());
//        viewHolder.temperature.setText(cityItem.getTemperature());
//        return view;
//    }
//    class ViewHolder{
//        TextView city;
//        TextView temperature;
//    }
//}
