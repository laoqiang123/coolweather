package com.example.administrator.coolweather.utils;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Administrator on 2016/7/26 0026.
 */
public class HttpUtils {
    static  HttpURLConnection connection =null;
    public static  void sendHttpGet(final String address, final HttpCallbackListener listner){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d("tag",address+"gggggg");
                    URL url = new URL(address);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setReadTimeout(5000);
                    connection.setConnectTimeout(5000);
                    StringBuilder responsse = new StringBuilder();
                    InputStream in = connection.getInputStream();
                    BufferedReader bf = new BufferedReader(new InputStreamReader(in));
                    String line = null;
                    while((line=bf.readLine())!=null){
                        responsse.append(line);
                        Log.d("tag",responsse.toString()+"请求结果");
                    }
                    if(listner!=null){
                        listner.onFinish(responsse.toString());
                    }

                } catch (MalformedURLException e) {

                } catch (IOException e) {
                    e.printStackTrace();
                }
                catch(Exception e){
                    if(listner!=null){
                        listner.onError();
                    }
                }
                finally {
                    if(connection!=null){
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }
}
