package com.share_will.mobile.model.entity;

public class BatteryEntity {

    /**
     * rsoc : 99
     * current1 : 0
     * surplus : 1000
     * sop : 100
     * latitude : 22.5287620
     * sn : 1122110081330048
     * time : 1555039013652
     * longitude : 113.9445080
     */

    private String rsoc;
    private String current1;
    private String surplus;
    private String sop;
    private String latitude;
    private String sn;
    private long time;
    private String longitude;
    private boolean online;

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public String getDischarges() {
        return discharges;
    }

    public void setDischarges(String discharges) {
        this.discharges = discharges;
    }

    private String discharges;

    public String getRsoc() {
        return rsoc;
    }

    public void setRsoc(String rsoc) {
        this.rsoc = rsoc;
    }

    public String getCurrent1() {
        return current1;
    }

    public void setCurrent1(String current1) {
        this.current1 = current1;
    }

    public String getSurplus() {
        return surplus;
    }

    public void setSurplus(String surplus) {
        this.surplus = surplus;
    }

    public String getSop() {
        return sop;
    }

    public void setSop(String sop) {
        this.sop = sop;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

}
