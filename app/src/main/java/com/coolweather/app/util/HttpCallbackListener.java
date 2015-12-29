package com.coolweather.app.util;

/**
 * Created by wsb on 2015/12/25.
 */
public interface HttpCallbackListener {
        void onFinish(String response);
        void onError(Exception e);

}
