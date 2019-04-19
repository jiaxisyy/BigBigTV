package com.share_will.mobile.model.entity;

/**
 * Created by ChenGD on 2018/3/21.
 *
 * @author chenguandu
 */

public class CabinetEntity {

    /**
     * id : 722560c92c1a11e89c6a00ffb4f29193
     * cabinetSn : E201802240000001
     * longitude : 114.335622
     * latitude : 30.539592
     * areaCode : 027
     * address : ????28?
     * station : ????????
     * creator : sys
     * createTime : 2018-03-21 16:16:37.0
     * usableCount:0 可预约电池数量
     * subscribe : 是否可以预约
     * batteryCount: 电池数量
     * emptyHouse: 空舱数量
     * wareHouseTotal: 舱门数量
     */

    private String id;
    private String cabinetSn;
    private double longitude;
    private double latitude;
    private String areaCode;
    private String address;
    private String station;
    private String creator;
    private String createTime;
    private int usableCount;
    private int batteryCount;
    private int emptyHouse;
    private int wareHouseTotal;

    public int getWareHouseTotal() {
        return wareHouseTotal;
    }

    public void setWareHouseTotal(int wareHouseTotal) {
        this.wareHouseTotal = wareHouseTotal;
    }

    public int getBatteryCount() {
        return batteryCount;
    }

    public void setBatteryCount(int batteryCount) {
        this.batteryCount = batteryCount;
    }

    public int getEmptyHouse() {
        return emptyHouse;
    }

    public void setEmptyHouse(int emptyHouse) {
        this.emptyHouse = emptyHouse;
    }

    private boolean subscribe;

    public boolean isSubscribe() {
        return subscribe;
    }

    public void setSubscribe(boolean subscribe) {
        this.subscribe = subscribe;
    }


    public int getUsableCount() {
        return usableCount;
    }

    public void setUsableCount(int usableCount) {
        this.usableCount = usableCount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCabinetSn() {
        return cabinetSn;
    }

    public void setCabinetSn(String cabinetSn) {
        this.cabinetSn = cabinetSn;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "CabinetEntity{" +
                "id='" + id + '\'' +
                ", cabinetSn='" + cabinetSn + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", areaCode='" + areaCode + '\'' +
                ", address='" + address + '\'' +
                ", station='" + station + '\'' +
                ", creator='" + creator + '\'' +
                ", createTime='" + createTime + '\'' +
                ", usableCount=" + usableCount +
                ", batteryCount=" + batteryCount +
                ", emptyHouse=" + emptyHouse +
                ", wareHouseTotal=" + wareHouseTotal +
                ", subscribe=" + subscribe +
                '}';
    }
}
