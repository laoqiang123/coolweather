package com.example.administrator.coolweather.utils;

import android.text.TextUtils;
import android.util.Log;

import com.example.administrator.coolweather.db.CoolWeatherDao;
import com.example.administrator.coolweather.model.City;
import com.example.administrator.coolweather.model.County;
import com.example.administrator.coolweather.model.Province;

/**
 * Created by Administrator on 2016/7/26 0026.
 */
public class Utility {
    public synchronized static boolean handleProvinceResponse(CoolWeatherDao dao,String response){
        if(!TextUtils.isEmpty(response)){
            String[] allprovince = response.split(",");
            Log.d("tag",response+"999999");
            Log.d("tag",allprovince.length+"999999");
            if(allprovince!=null&&allprovince.length>0){
                for(String p :allprovince){
                    String[] array = p.split("\\|");
                    Province p1 = new Province();
                    p1.setProvinceName(array[1]);
                    Log.d("tag------",array[1]);
                    p1.setPrivinceCode(array[0]);
                    dao.saveProvince(p1);

                }
                return  true;
            }

        }
        return false;
    }
    public static  boolean handleCityResponse(CoolWeatherDao dao,String response,int provinceid){
        if(!TextUtils.isEmpty(response)){
            String[] allcityis = response.split(",");
            if(allcityis!=null&&allcityis.length>0){
                for(String c : allcityis){
                    String[] array =c.split("\\|");
                    City city = new City();
                    city.setProvince_id(provinceid);
                    city.setCityName(array[1]);
                    city.setCityCode(array[0]);
                    dao.saveCity(city);

                }
                return  true;
            }

        }
   return  false;
    }
    public static  boolean handleCountiesResopnse(CoolWeatherDao dao,String resopnse,int cityid){
        if(!TextUtils.isEmpty(resopnse)){
            String[] allcountis = resopnse.split(",");
            if(allcountis!=null&&allcountis.length>0){
                for(String s: allcountis){
                    String[] array = s.split("\\|");
                    County c = new County();
                    c.setCity_id(cityid);
                    c.setCountyName(array[1]);
                    c.setCountyCode(array[0]);
                    dao.saveCounty(c);
                }
            }
            return  true;
        }
  return false;
    }

}
