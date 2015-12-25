package com.coolweather.app.model;

/**
 * Created by wsb on 2015/12/25.
 */
public class Province {
    int id;
    private String provinceName;
    private String provinceCode;
    int  provinceId;



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }
}
