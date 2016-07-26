package com.example.administrator.coolweather.utils;

import android.app.Application;
import android.content.Context;

/**
 * Created by Administrator on 2016/7/25 0025.
 */
public class Myapplication extends Application {
    private static  Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext(){

        return  context;
    }

}
