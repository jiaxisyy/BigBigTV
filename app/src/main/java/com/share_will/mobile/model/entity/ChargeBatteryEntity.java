package com.share_will.mobile.model.entity;

/**
 * 充电电池
 */
public class ChargeBatteryEntity {

    /**
     * rsoc : 99
     * manageMoney : 0
     * door : 5
     * cabinetAddress : 深圳市南山区南京大学产学研基地305室
     * money : 0
     * sop : 100
     * startTime : 1554985911000
     * fullTime : null
     * energy : 0
     * nowTime : 1555035069916
     */

    private String rsoc;
    private int manageMoney;
    private int door;
    private String cabinetAddress;
    private int money;
    private String sop;
    private long startTime;
    private long fullTime;
    private int energy;
    private long nowTime;

    public String getRsoc() {
        return rsoc;
    }

    public void setRsoc(String rsoc) {
        this.rsoc = rsoc;
    }

    public int getManageMoney() {
        return manageMoney;
    }

    public void setManageMoney(int manageMoney) {
        this.manageMoney = manageMoney;
    }

    public int getDoor() {
        return door;
    }

    public void setDoor(int door) {
        this.door = door;
    }

    public String getCabinetAddress() {
        return cabinetAddress;
    }

    public void setCabinetAddress(String cabinetAddress) {
        this.cabinetAddress = cabinetAddress;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public String getSop() {
        return sop;
    }

    public void setSop(String sop) {
        this.sop = sop;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getFullTime() {
        return fullTime;
    }

    public void setFullTime(long fullTime) {
        this.fullTime = fullTime;
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public long getNowTime() {
        return nowTime;
    }

    public void setNowTime(long nowTime) {
        this.nowTime = nowTime;
    }
}
