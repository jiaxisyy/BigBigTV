package com.share_will.mobile.model.entity;

public class ChargeStakeOrderInfoEntity {


    /**
     * door : 3
     * manageMoney : 150
     * cabinetAddress : 广东省深圳市南山区粤海街道南京大学深圳产学研基地
     * money : 0
     * orderId : 18328317002201905081205010793
     * cabinetId : EYX1805301000001
     * startTime : 1557288302000
     * endTime : null
     * energy : 0
     * status : false
     */

    private int door;
    private int manageMoney;
    private String cabinetAddress;
    private int money;
    private String orderId;
    private String cabinetId;
    private long startTime;
    private long endTime;
    private int energy;
    private boolean status;

    public int getDoor() {
        return door;
    }

    public void setDoor(int door) {
        this.door = door;
    }

    public int getManageMoney() {
        return manageMoney;
    }

    public void setManageMoney(int manageMoney) {
        this.manageMoney = manageMoney;
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

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCabinetId() {
        return cabinetId;
    }

    public void setCabinetId(String cabinetId) {
        this.cabinetId = cabinetId;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

}
