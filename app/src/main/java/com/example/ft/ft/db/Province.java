package com.example.ft.ft.db;
import org.litepal.crud.DataSupport;
        /*
        id 是每个实体类中都应该有的字段，
        provinceName 记录省的名字，
        provinceCode 记录省的代号
         */
public class Province extends DataSupport {
    private int id;
    private String provinceName;
    private int  provinceCode;


    public int getprovinceCode() {
        return provinceCode;
    }

    public void setprovinceCode(int provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getprovinceName() {
        return provinceName;
    }

    public void setprovinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public int getid() {
        return id;
    }

    public void setid(int id) {
        this.id = id;
    }
}
