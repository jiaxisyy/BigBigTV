package com.share_will.mobile.model.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;

public class AlarmMultiEntity implements MultiItemEntity {
    public static final int TYPE_SMOKE = 1;
    public static final int TYPE_RFID = 2;
    //smoke
//    private int id;
//    private String userId;
//    private String devtype;
//    private String deveui;
//    private String positionName;
//    private String deviceAddress;
//    private double longitude;
//    private double latitude;
//    private String remark;
//    private boolean active;
//    private long createTime;
//    private String creator;
//    private Object modifyTime;
//    private Object modifier;
//    private String alarmcode;
//    private int typeflag;
//    private String title;
//    private int alarmlevel;
//    private long alarmtime;
//    private String descp;
//    private int confirmstate;

    //rfid
//    private String epcid;
//    private int state;
//    private long collecttime;
//    private String communityName;
//    private String address;


    private int itemType;
    private AlarmEntity.SmokeBean smokeBean;
    private AlarmEntity.RfidBean rfidBean;


    public AlarmMultiEntity(int itemType, AlarmEntity.SmokeBean smokeBean) {
        this.itemType = itemType;
        this.smokeBean = smokeBean;
    }

    public AlarmMultiEntity(int itemType, AlarmEntity.RfidBean rfidBean) {
        this.itemType = itemType;
        this.rfidBean = rfidBean;
    }

    public AlarmEntity.SmokeBean getSmokeBean() {
        return smokeBean;
    }

    public void setSmokeBean(AlarmEntity.SmokeBean smokeBean) {
        this.smokeBean = smokeBean;
    }

    public AlarmEntity.RfidBean getRfidBean() {
        return rfidBean;
    }

    public void setRfidBean(AlarmEntity.RfidBean rfidBean) {
        this.rfidBean = rfidBean;
    }

    @Override
    public int getItemType() {
        return itemType;
    }
}
