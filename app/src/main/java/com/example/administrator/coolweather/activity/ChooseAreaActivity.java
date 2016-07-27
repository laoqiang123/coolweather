package com.example.administrator.coolweather.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.coolweather.R;
import com.example.administrator.coolweather.db.CoolWeatherDao;
import com.example.administrator.coolweather.model.City;
import com.example.administrator.coolweather.model.County;
import com.example.administrator.coolweather.model.Province;
import com.example.administrator.coolweather.utils.HttpCallbackListener;
import com.example.administrator.coolweather.utils.HttpUtils;
import com.example.administrator.coolweather.utils.Myapplication;
import com.example.administrator.coolweather.utils.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/26 0026.
 */
public class ChooseAreaActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private ListView listview;
    private TextView tv_title;
    private List<String> list = new ArrayList<>();
    private CoolWeatherDao dao;
    private List<Province> provinceList = new ArrayList<>();
    private List<City> cityList = new ArrayList<>();
    private List<County> countyList = new ArrayList<>();
    private  ArrayAdapter adapter;
    private ProgressDialog pd;
    public  static  final int LEVEL_PROVINCE = 0;
    public  static  final  int LEVEL_CITY = 1;
    public  static  final  int LEVEL_COUNTY = 2;
    private int currentlevel;
    private Province selectedprovince;
    private City selectedcity;
    private County selectedcounty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.choose_area);
        listview = (ListView) findViewById(R.id.listview);
        tv_title = (TextView) findViewById(R.id.tv_title);
        dao = CoolWeatherDao.getInstance();
        adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,list);
        queryProvince();
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(this);
    }



    private void queryProvince() {
        provinceList = dao.getProvince();
        if(provinceList.size()>0){
            list.clear();
            for(Province province:provinceList){
                list.add(province.getProvinceName());
                Log.d("tag",province.getProvinceName());
            }
            adapter.notifyDataSetChanged();
            listview.setSelection(0);
            tv_title.setText("中国");
            currentlevel = LEVEL_PROVINCE;
        }else{
            queryFromServer(null, "province");
        }

    }
    public void queryCity(){
        cityList = dao.getCity(selectedprovince.getId());
        if(cityList.size()>0){
            list.clear();
            for(City c :cityList) {
                list.add(c.getCityName());
            }
            adapter.notifyDataSetChanged();
                listview.setSelection(0);
                tv_title.setText(selectedprovince.getProvinceName());
                currentlevel = LEVEL_CITY;

        }else{
            Log.d("tag",selectedprovince.getPrivinceCode());
            queryFromServer(selectedprovince.getPrivinceCode(), "city");
        }
    }
    public void queryCounty(){
        countyList  = dao.getCounty(selectedcity.getId());
        if(countyList.size()>0){
            list.clear();
            for(County c:countyList) {
                list.add(c.getCountyName());
            }
            adapter.notifyDataSetChanged();
                listview.setSelection(0);
                tv_title.setText(selectedcity.getCityName());
                currentlevel = LEVEL_COUNTY;



        }else{
            Log.d("tag",selectedcity.getCityCode()+"code");
            queryFromServer(selectedcity.getCityCode(), "county");
        }
    }

    private void queryFromServer(String code, final String type) {
        String address;
        if(!TextUtils.isEmpty(code)){
            address = "http://www.weather.com.cn/data/list3/city"+code+".xml";
        }else{
            Log.d("tag","address");
            address = "http://www.weather.com.cn/data/list3/city.xml";
        }
        showProgress();
        HttpUtils.sendHttpGet(address, new HttpCallbackListener() {
            boolean result;
            @Override
            public void onFinish(String response) {
                if(type.equals("province")){
                    result = Utility.handleProvinceResponse(dao,response);
                }else if(type.equals("city")){
                    result = Utility.handleCityResponse(dao,response,selectedprovince.getId());
                }else if(type.equals("county")){
                    Log.d("tag",response.toString()+"county");
                    result = Utility.handleCountiesResopnse(dao,response,selectedcity.getId());
                    Log.d("tag",result+"结果");
                }
                if(result){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgress();
                            if(type.equals("province")){
                                queryProvince();
                            }else if(type.equals("city")){
                                queryCity();
                            } else if(type.equals("county")){
                                queryCounty();
                            }

                        }
                    });
                }

            }

            @Override
            public void onError() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgress();;
                            Toast.makeText(ChooseAreaActivity.this,"加载失败",Toast.LENGTH_SHORT).show();
                        }
                    });
            }
        });
    }

    private void showProgress() {
        if(pd==null){
            pd = new ProgressDialog(this);
            pd.setMessage("正在加载中");
            pd.setCancelable(false);
        }
        pd.show();
    }
    private void closeProgress(){
        if(pd!=null){
            pd.dismiss();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(currentlevel==LEVEL_PROVINCE){
            selectedprovince = provinceList.get(position);
            queryCity();

        }else if(currentlevel==LEVEL_CITY){
            selectedcity = cityList.get(position);
            Log.d("tag","county");
            queryCounty();
        }else if(currentlevel==LEVEL_COUNTY){
            selectedcounty = countyList.get(position);
            Intent intent = new Intent(Myapplication.getContext(),WeatherActivity.class);
            intent.putExtra("county_code",selectedcounty.getCountyCode());
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
       // super.onBackPressed();
        if(currentlevel==LEVEL_COUNTY){
            queryCity();
        }else if(currentlevel==LEVEL_CITY){
            queryProvince();
        }else {
            finish();
        }
    }
}

