package com.example.administrator.coolweather.BroadCast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.administrator.coolweather.Service.AutoUpdateService;

/**
 * Created by Administrator on 2016/7/27 0027.
 */
public class AutoUpdateReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        intent = new Intent(context, AutoUpdateService.class);
        context.startService(intent);
    }
}
