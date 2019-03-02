package com.example.ft.ft.db;

import org.litepal.crud.DataSupport;
            /*
            cityName 记录市的名字，
            cityCode 记录市的代号，
            provinceId 记录当前市所属省 的id值
            */
public class City extends DataSupport {
    private int id;
    private String cityName;
    private int cityCode;
    private int provinceId;


    public int getid() {
        return id;
    }

    public void setid(int id) {
        this.id = id;
    }

    public String getcityName() {
        return cityName;
    }

    public void setcityName(String cityName) {
        this.cityName = cityName;
    }

    public int getcityCode() {
        return cityCode;
    }

    public void setcityCode(int cityCode) {
        this.cityCode = cityCode;
    }

    public int getprovinceId() {
        return provinceId;
    }

    public void setprovinceId(int provinceId) {
        this.provinceId = provinceId;
    }
}
