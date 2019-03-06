package com.example.ft.ft;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ft.ft.db.City;
import com.example.ft.ft.db.Country;
import com.example.ft.ft.db.Province;
import com.example.ft.ft.util.HttpUtil;
import com.example.ft.ft.util.Utility;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ChooseAreaFragment extends Fragment {

    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    public static final int LEVEL_COUNTRY = 2;
    private TextView titleText;
    private Button backButton;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> dataList=new ArrayList<>();
    private int currentLevel;/*当前选中的级别*/
    private List<Province> provinceList;/*省列表*/
    private List<City> cityList;/*市列表*/
    private List<Country> countryList;/*县&区列表*/
    private Province selectedProvince;/*选择的省*/
    private City selectedCity;/*选中的市*/
    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.choose_area,container,false);
        titleText=view.findViewById(R.id.title_text);
        backButton=view.findViewById(R.id.back_button);
        listView=view.findViewById(R.id.list_view);
        adapter=new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1,dataList);
        listView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (currentLevel==LEVEL_PROVINCE){
                    selectedProvince=provinceList.get(position);
                    queryCities();
                }else if (currentLevel==LEVEL_CITY){
                    selectedCity=cityList.get(position);
                    queryCuontries();
                }else if (currentLevel==LEVEL_COUNTRY){
                    String weatherId=countryList.get(position).getWeatherId();
                    Intent intent=new Intent(getActivity(),WeatherActivity.class);
                    intent.putExtra("weather_id",weatherId);
                    startActivity(intent);
                    getActivity().finish();
                }
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentLevel==LEVEL_COUNTRY){
                    queryCities();
                }else if (currentLevel==LEVEL_CITY){
                    queryProvinces();
                }
            }
        });
        queryProvinces();
    }
/*
* 查询全国所有的省，优先从数据库查询，如果没有查询到再去服务器上查询
* */
    private void queryProvinces() {
        titleText.setText("中国");
        backButton.setVisibility(View.GONE);
        provinceList=DataSupport.findAll(Province.class);
        if (provinceList.size()>0){
            dataList.clear();
            for (Province province:provinceList){
                dataList.add(province.getprovinceName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel=LEVEL_PROVINCE;
        }else {
            String address="http://guolin.tech/api/china";
            queryFromeServer(address,"province");
        }
    }
    /**
     *  查询选中省内所有的市，优先从数据库查询，如果没有查询到再去服务器上查询
     *  */
    private void queryCities() {
        titleText.setText(selectedProvince.getprovinceName());
        backButton.setVisibility(View.VISIBLE);
        cityList=DataSupport.where("provinceid = ?",String.valueOf(selectedProvince.getid())).find(City.class);
        if (cityList.size()>0){
            dataList.clear();
            for(City city:cityList){
                dataList.add(city.getcityName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel=LEVEL_CITY;
        }else {
            int proviceCode=selectedProvince.getprovinceCode();
            String address="http://guolin.tech/api/china/"+proviceCode;
            queryFromeServer(address,"city");
        }
    }
    /**
     *  查询选中市内所有的县，优先从数据库查询，如果没有查询到再去服务器上查询
     *  */
    private void queryCuontries() {
        titleText.setText(selectedCity.getcityName());
        backButton.setVisibility(View.VISIBLE);
        countryList=DataSupport.where("cityid = ?",String.valueOf(selectedCity.getid())).find(Country.class);
        if (countryList.size()>0){
            dataList.clear();
            for(Country country:countryList){
                dataList.add(country.getCountryName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel=LEVEL_COUNTRY;
        }else {
            int proviceCode=selectedProvince.getprovinceCode();
            int cityCode=selectedCity.getcityCode();
            String address="http://guolin.tech/api/china/"+proviceCode+"/"+cityCode;
            queryFromeServer(address,"country");
        }
    }
    /**
     *  根据传入的地址和类型从服务器上查询省市县数据
     *  */
    private void queryFromeServer(final String address, final String type) {
        showProgressDialog();
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(getContext(), "加载失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText=response.body().string();
                boolean result=false;
                if ("province".equals(type)){
                    result=Utility.handleProvinceResponse(responseText);
                }else if ("city".equals(type)){
                    result=Utility.handleCityResponse(responseText,selectedProvince.getid());
                }else if("country".equals(type)){
                    result=Utility.handleCountryResponse(responseText,selectedCity.getid());
                }
                if (result){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            if ("province".equals(type)){
                                queryProvinces();
                            }else if ("city".equals(type)){
                                queryCities();
                            }else if ("country".equals(type)){
                                queryCuontries();
                            }
                        }
                    });
                }
            }
        });

    }
/*
* 关闭对话框
* */
    private void closeProgressDialog() {
        if (progressDialog!=null){
            progressDialog.dismiss();
        }
    }
/*
* 显示对话框
* */
    private void showProgressDialog() {
        if (progressDialog==null){
            progressDialog=new ProgressDialog(getContext());
            progressDialog.setMessage("正在加载...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

}
