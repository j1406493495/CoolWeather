package com.coolweather.app.util;

/**
 * Created by whjin on 16-11-11.
 */
public interface HttpCallbackListener {
    void onFinish(String response);
    void onError(Exception e);
}
