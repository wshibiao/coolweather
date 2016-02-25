package com.coolweather.model;

/**
 * Created by wsb on 2016/1/21.
 */
public class FutureWeather {
    private String week;
    private int imageId;
    private int imageId2;
    private String temperatureRange;

    public int getImageId() {
        return imageId;
    }

    public int getImageId2() {
        return imageId2;
    }

    public void setImageId2(int imageId2) {
        this.imageId2 = imageId2;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getTemperatureRange() {
        return temperatureRange;
    }

    public void setTemperatureRange(String temperatureRange) {
        this.temperatureRange = temperatureRange;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }
}
