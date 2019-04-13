package com.share_will.mobile.model.entity;

/**
 * Created by ChenGD on 2018/3/13.
 *
 * @author chenguandu
 */

public class ChargingEntity {
    private long minute;
    private long money;
    private String id;

    public long getMinute() {
        return minute;
    }

    public void setMinute(long minute) {
        this.minute = minute;
    }

    public long getMoney() {
        return money;
    }

    public void setMoney(long money) {
        this.money = money;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "ChargingEntity{" +
                "minute='" + minute + '\'' +
                ", money='" + money + '\'' +
                ", id=" + id +
                '}';
    }
}
