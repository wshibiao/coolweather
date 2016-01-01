package com.coolweather.app.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

/**
 * Created by wsb on 2015/12/25.
 */
public class HttpUtil {
    /**
     * 获取城市列表
     * @param httpUrl
     *            :请求接口
     * @param listener
     *              回调函数监听
     *
     */

    public static void sendHttpRequest(final String httpUrl, final HttpCallbackListener listener) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String response;//携带json数据
                   //使用HttpClient访问API获取JSON数据
                    HttpClient client = new DefaultHttpClient();
                    HttpGet get = new HttpGet(httpUrl);
                    HttpResponse httpResponse = client.execute(get);
                    if (httpResponse.getStatusLine().getStatusCode() == 200) {
                        HttpEntity entity = httpResponse.getEntity();
                        response = EntityUtils.toString(entity, "utf-8");
                        if(listener!=null){
                            listener.onFinish(response);
                        }
                    }
                } catch (Exception e) {
                    listener.onError(e);
                }finally {

                }
            }
        }).start();

    }

    /**
     * 根据用户输入的城市名字查询该城市的天气
     * @param HttpUrl API地址
     * @param cityName 查询的城市名
     */



}