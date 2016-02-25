package com.coolweather.util;

/**
 * Created by wsb on 2015/12/25.
 */
public interface HttpCallbackListener {
        void onFinish(StringBuilder response);
        void onError(Exception e);

}
