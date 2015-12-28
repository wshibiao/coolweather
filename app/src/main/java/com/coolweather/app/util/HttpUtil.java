package com.coolweather.app.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by wsb on 2015/12/25.
 */
public class HttpUtil {
    /**
     * @param httpUrl
     *            :请求接口
     * @param httpArg
     *            :参数
     */

    public static void sendHttpRequest(final String httpUrl,final String httpArg,
                                       final HttpCallbackListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
//                HttpURLConnection connection = null;
//                try {
//                    URL url = new URL(address);
//                    connection = (HttpURLConnection) url.openConnection();
//                    connection.setRequestMethod("GET");
//                    connection.setConnectTimeout(8000);
//                    connection.setReadTimeout(8000);
//                    InputStream in = connection.getInputStream();
//                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
//                    StringBuilder response = new StringBuilder();
//                    String line;
//                    while ((line = reader.readLine()) != null) {
//                        response.append(line);
//                    }
//                    if (listener != null) {
//                        // 回调onFinish()方法
//                        listener.onFinish(response.toString());
//                    }
//                } catch (Exception e) {
//                    if (listener != null) {
//                        // 回调onError()方法
//                        listener.onError(e);
//                    }
//                } finally {
//                    if (connection != null) {
//                        connection.disconnect();
//                    }
//                }
                    HttpURLConnection connection=null;
                    BufferedReader reader = null;
                    String result = null;
                    StringBuffer response = new StringBuffer();
                    String address = httpUrl + "?" + httpArg;

                    try {
                        URL url = new URL(address);
                        connection = (HttpURLConnection) url
                                .openConnection();
                        connection.setRequestMethod("GET");
                        // 填入apikey到HTTP header
                        connection.setRequestProperty("apikey",  "248672dfe49cb0f16dfeaaa92a292069");
                        connection.connect();
                        InputStream is = connection.getInputStream();
                        reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                        String strRead = null;
                        while ((strRead = reader.readLine()) != null) {
                            response.append(strRead);
                            response.append("\r\n");
                        }
                    if (listener != null) {
                        // 回调onFinish()方法
                        listener.onFinish(response.toString());
                    }
                } catch (Exception e) {
                    if (listener != null) {
                        // 回调onError()方法
                        listener.onError(e);
                    }
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

}