package com.example.administrator.coolweather.utils;

/**
 * Created by Administrator on 2016/7/26 0026.
 */
public interface HttpCallbackListener {
    void onFinish(String response);
    void onError();
}
