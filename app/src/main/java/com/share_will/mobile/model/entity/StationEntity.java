package com.share_will.mobile.model.entity;

import com.contrarywind.interfaces.IPickerViewData;

import java.io.Serializable;

public class StationEntity implements IPickerViewData, Serializable {

    /**
     * customerCode : shunwei
     * stationName : 南京大学
     * customerName : 顺为
     * stationAddress : 深圳市科技园南区南京大学产学研基地
     * stationId : 1
     */

    private String customerCode;
    private String stationName;
    private String customerCodeXX;
    private String customerCodeX;
    private String customerName;
    private String stationAddress;
    private long stationId;
    private boolean isClick;


    public boolean isClick() {
        return isClick;
    }

    public void setClick(boolean click) {
        isClick = click;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public long getStationId() {
        return stationId;
    }

    public void setStationId(long stationId) {
        this.stationId = stationId;
    }

    @Override
    public String getPickerViewText() {
        return stationName;
    }

    public String getCustomerCodeX() {
        return customerCodeX;
    }

    public void setCustomerCodeX(String customerCodeX) {
        this.customerCodeX = customerCodeX;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getStationAddress() {
        return stationAddress;
    }

    public void setStationAddress(String stationAddress) {
        this.stationAddress = stationAddress;
    }


    public String getCustomerCodeXX() {
        return customerCodeXX;
    }

    public void setCustomerCodeXX(String customerCodeXX) {
        this.customerCodeXX = customerCodeXX;
    }

    public StationEntity(String stationName, boolean isClick) {
        this.stationName = stationName;
        this.isClick = isClick;
    }

    @Override
    public String toString() {
        return "StationEntity{" +
                "customerCode='" + customerCode + '\'' +
                ", stationName='" + stationName + '\'' +
                ", customerCodeXX='" + customerCodeXX + '\'' +
                ", customerCodeX='" + customerCodeX + '\'' +
                ", customerName='" + customerName + '\'' +
                ", stationAddress='" + stationAddress + '\'' +
                ", stationId=" + stationId +
                ", isClick=" + isClick +
                '}';
    }
}
