package com.example.ft.ft.db;

import org.litepal.crud.DataSupport;
        /*
        countyName	记录县的名字，
        weatherId	记录县所对应的天气id，
        cityId	记录当前县 所属市的id值
        */
public class Country extends DataSupport {
    private int id;
    private String countryName;
    private String  weatherId;
    private int cityId;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String  getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(String  weatherId) {
        this.weatherId = weatherId;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }
}
