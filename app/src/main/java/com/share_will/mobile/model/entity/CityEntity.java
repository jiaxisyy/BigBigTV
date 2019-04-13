package com.share_will.mobile.model.entity;

import com.contrarywind.interfaces.IPickerViewData;

public class CityEntity implements IPickerViewData {

    /**
     * areaCode : 0769
     * stationCity : 东莞
     */

    private String areaCode;
    private String stationCity;
    private double latitude;
    private double longitude;

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getStationCity() {
        return stationCity;
    }

    public void setStationCity(String stationCity) {
        this.stationCity = stationCity;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String getPickerViewText() {
        return stationCity;
    }
}
