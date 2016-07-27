package com.example.administrator.coolweather.Service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.administrator.coolweather.BroadCast.AutoUpdateReceiver;
import com.example.administrator.coolweather.utils.HttpCallbackListener;
import com.example.administrator.coolweather.utils.HttpUtils;
import com.example.administrator.coolweather.utils.Myapplication;
import com.example.administrator.coolweather.utils.Utility;

/**
 * Created by Administrator on 2016/7/27 0027.
 */
public class AutoUpdateService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

      new Thread(new Runnable() {
          @Override
          public void run() {
             updateWeather(); 
          }
      }).start();
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        int minute  = 1000*60;
        long triggerTime = SystemClock.elapsedRealtime()+minute;
        Intent intent1 = new Intent(Myapplication.getContext(), AutoUpdateReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(Myapplication.getContext(),0,intent1,0);
        am.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerTime,pi);
        return super.onStartCommand(intent, flags, startId);
    }

    private void updateWeather() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(Myapplication.getContext());
        String weathercode = sp.getString("weather_code", "");
        String address = "http://www.weather.com.cn/data/cityinfo/"+weathercode+".html";
        HttpUtils.sendHttpGet(address, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                Utility.handleWeatherResponse(Myapplication.getContext(),response);
                Log.d("tag",response+"更新");
            }

            @Override
            public void onError() {

            }
        });
    }

}
