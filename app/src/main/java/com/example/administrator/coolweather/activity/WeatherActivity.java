package com.example.administrator.coolweather.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.coolweather.R;
import com.example.administrator.coolweather.utils.HttpCallbackListener;
import com.example.administrator.coolweather.utils.HttpUtils;
import com.example.administrator.coolweather.utils.Myapplication;
import com.example.administrator.coolweather.utils.Utility;

/**
 * Created by Administrator on 2016/7/26 0026.
 */
public class WeatherActivity extends AppCompatActivity {
    private TextView tv_county;
    private LinearLayout linear_weather;
    private TextView tv_time;
    private TextView tv_dete;
    private TextView tv_weather;
    private TextView tv_temper1;
    private TextView tv_temper2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_layout);
        tv_county = (TextView) findViewById(R.id.tv_county);
        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_dete = (TextView) findViewById(R.id.tv_dete);
        tv_weather = (TextView) findViewById(R.id.tv_weather);
        tv_temper1 = (TextView) findViewById(R.id.tv_temper1);
        tv_temper2 = (TextView) findViewById(R.id.tv_temper2);
        linear_weather = (LinearLayout) findViewById(R.id.linear_weather);

        String countycode = getIntent().getStringExtra("county_code");
        Log.d("countycode", countycode + "乡村编号");
        if(!TextUtils.isEmpty(countycode)){
            tv_time.setText("同步中....");
            linear_weather.setVisibility(View.INVISIBLE);
            queryWeatherCode(countycode);       
        }else{
            showWeather();
        }
    }

    private void queryWeatherCode(String countycode) {
        String address = "http://www.weather.com.cn/data/list3/city"+countycode+".xml";
        Log.d("address",address+"有问题的网址");
        queryFromServer(address, "countyCode");

    }

    private void queryFromServer(String address, final String type) {
        HttpUtils.sendHttpGet(address, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                Log.d("response",response+"天气信息");
                if ("countyCode".equals(type)) {
                    if (!TextUtils.isEmpty(response)) {
                        Log.d("tag", response.toString() + "网路请求结果");
                        String[] array = response.split("\\|");
                            if (array != null && array.length == 2) {
                                String weatherCode = array[1];
                                Log.d("tag","执行到了");
                                queryWeatherInfo(weatherCode);

                        }
                    }

                } else if ("weatherCode".equals(type)) {
                    Log.d("tag","执行到了2");
                    Utility.handleWeatherResponse(Myapplication.getContext(), response);
                    Log.d("weatherinfo",response+"天气情况");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showWeather();
                        }
                    });

                }
            }

            @Override
            public void onError() {
                tv_time.setText("同步失败");
            }
        });
    }

    private void queryWeatherInfo(String weatherCode) {
        String address = "http://www.weather.com.cn/data/cityinfo/"+weatherCode+".html";
        Log.d("address",address+"one");
        queryFromServer(address, "weatherCode");
        Log.d("tag","执行到了3");}
    public void showWeather(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Myapplication.getContext());
        tv_county.setText(prefs.getString("city_name",null));
        tv_time.setText("今天"+prefs.getString("public_time",null)+"发布");
        tv_dete.setText(prefs.getString("current_date",null));
        tv_weather.setText(prefs.getString("weather_desp",null));
        tv_temper1.setText(prefs.getString("temp1",null));
        tv_temper2.setText(prefs.getString("temp2",null));
        linear_weather.setVisibility(View.VISIBLE);
    }
}
