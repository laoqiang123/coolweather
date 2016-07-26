package com.example.administrator.coolweather.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.administrator.coolweather.model.City;
import com.example.administrator.coolweather.model.County;
import com.example.administrator.coolweather.model.Province;
import com.example.administrator.coolweather.utils.Myapplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/25 0025.
 * @author laoqiang
 * @
 *
 */
public class CoolWeatherDao {
    public static final String DB_NAME = "weather";
    private SQLiteDatabase db;
    private static CoolWeatherDao coolWeatherDao;

    public CoolWeatherDao() {
        MyCoolWeatherOpenHelper mwoh = new MyCoolWeatherOpenHelper(Myapplication.getContext(), DB_NAME, null, 1);
        db = mwoh.getWritableDatabase();
    }
    /**
     *
     * @return 返回数据库操作对象
     *
     */    public synchronized static CoolWeatherDao getInstance() {
        if(coolWeatherDao==null){
            coolWeatherDao = new CoolWeatherDao();
        }
       return  coolWeatherDao;
    }
    /**
     *将province实例存储到数据库
     */
    public void saveProvince(Province province){
        if(province!=null){
            ContentValues value =new ContentValues();
            value.put("province_name",province.getProvinceName());
            value.put("province_code",province.getPrivinceCode());
            db.insert("Province",null,value);
        }
    }
    /**
     *查询所有省份。
     */

    public List<Province> getProvince(){
        List<Province> list = new ArrayList<>();
        Cursor cursor = db.query("Province",null,null,null,null,null,null);
        while(cursor.moveToNext()){
            Province p = new Province();
            p.setId(cursor.getInt(cursor.getColumnIndex("id")));
            p.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
            p.setPrivinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
            list.add(p);
        }
        if(cursor!=null){
            cursor.close();
        }
        return list;
    }
    /**
     * 将city实例存储到数据库。
     */

    public void saveCity(City city){
        ContentValues value = new ContentValues();
        value.put("city_name",city.getCityName());
        value.put("city_code",city.getCityCode());
        value.put("province_id",city.getProvince_id());
        db.insert("City",null,value);
    }
    /**
     *数据库根据省份获取city
     */


    public List<City> getCity(int provinceid){
        List<City> list = new ArrayList<>();
        Cursor cursor = db.query("City",null,"province_id=?",new String[]{String.valueOf(provinceid)},null,null,null);
        while(cursor.moveToNext()){
            City c = new City();
            c.setId(cursor.getInt(cursor.getColumnIndex("id")));
            c.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
            c.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
            list.add(c);
        }
        return  list;
    }

    /**
     * 将county实例存储到数据库
     * @param county
     */
    public void saveCounty(County county){
        if(county!=null){
            ContentValues value = new ContentValues();
            value.put("county_name",county.getCountyName());
            value.put("county_code",county.getCountyCode());
            value.put("city_id",county.getCity_id());
            db.insert("County",null,value);
        }

    }

    /**
     *
     * @param cityid
     * @return    根据城市返回的县信息
     */
    public List<County> getCounty(int cityid){
        List<County> list = new ArrayList<>();
        Cursor cursor = db.query("County",null,"city_id =?",new String[]{String.valueOf(cityid)},null,null,null);
        while(cursor.moveToNext()){
            County c = new County();
            c.setId(cursor.getInt(cursor.getColumnIndex("id")));
            c.setCountyName(cursor.getString(cursor.getColumnIndex("county_name")));
            c.setCountyCode(cursor.getString(cursor.getColumnIndex("county_code")));
            list.add(c);
        }
        return list;

    }






}
