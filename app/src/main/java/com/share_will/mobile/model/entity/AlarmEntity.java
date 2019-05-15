package com.share_will.mobile.model.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

public class AlarmEntity  {

    public static final int TYPE_SMOKE = 1;
    public static final int TYPE_RFID = 2;
    private List<SmokeBean> smoke;
    private List<RfidBean> rfid;
    private int itemType;

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public List<SmokeBean> getSmoke() {
        return smoke;
    }

    public void setSmoke(List<SmokeBean> smoke) {
        this.smoke = smoke;
    }

    public List<RfidBean> getRfid() {
        return rfid;
    }

    public void setRfid(List<RfidBean> rfid) {
        this.rfid = rfid;
    }



    public static class SmokeBean {
        /**
         * id : 46
         * userId : 13510946094
         * devtype : xinshanying_smoke_v2
         * deveui : 004a770066010af8
         * positionName : 顺为新能源
         * deviceAddress : 深圳市南京大学深圳产学研基地
         * longitude : 113.945105
         * latitude : 22.527907
         * remark : 三楼卫生间
         * active : true
         * createTime : 1554709485000
         * creator : system
         * modifyTime : null
         * modifier : null
         * alarmcode : 1
         * typeflag : 0
         * title : 探测器烟雾报警
         * alarmlevel : 1
         * alarmtime : 1554705778000
         * descp :
         * confirmstate : 0 未处理 1已处理
         */

        private int id;
        private String userId;
        private String devtype;
        private String deveui;
        private String positionName;
        private String deviceAddress;
        private double longitude;
        private double latitude;
        private String remark;
        private boolean active;
        private long createTime;
        private String creator;
        private Object modifyTime;
        private Object modifier;
        private String alarmcode;
        private int typeflag;
        private String title;
        private int alarmlevel;
        private long alarmtime;
        private String descp;
        private int confirmstate;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getDevtype() {
            return devtype;
        }

        public void setDevtype(String devtype) {
            this.devtype = devtype;
        }

        public String getDeveui() {
            return deveui;
        }

        public void setDeveui(String deveui) {
            this.deveui = deveui;
        }

        public String getPositionName() {
            return positionName;
        }

        public void setPositionName(String positionName) {
            this.positionName = positionName;
        }

        public String getDeviceAddress() {
            return deviceAddress;
        }

        public void setDeviceAddress(String deviceAddress) {
            this.deviceAddress = deviceAddress;
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

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public boolean isActive() {
            return active;
        }

        public void setActive(boolean active) {
            this.active = active;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public String getCreator() {
            return creator;
        }

        public void setCreator(String creator) {
            this.creator = creator;
        }

        public Object getModifyTime() {
            return modifyTime;
        }

        public void setModifyTime(Object modifyTime) {
            this.modifyTime = modifyTime;
        }

        public Object getModifier() {
            return modifier;
        }

        public void setModifier(Object modifier) {
            this.modifier = modifier;
        }

        public String getAlarmcode() {
            return alarmcode;
        }

        public void setAlarmcode(String alarmcode) {
            this.alarmcode = alarmcode;
        }

        public int getTypeflag() {
            return typeflag;
        }

        public void setTypeflag(int typeflag) {
            this.typeflag = typeflag;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getAlarmlevel() {
            return alarmlevel;
        }

        public void setAlarmlevel(int alarmlevel) {
            this.alarmlevel = alarmlevel;
        }

        public long getAlarmtime() {
            return alarmtime;
        }

        public void setAlarmtime(long alarmtime) {
            this.alarmtime = alarmtime;
        }

        public String getDescp() {
            return descp;
        }

        public void setDescp(String descp) {
            this.descp = descp;
        }

        public int getConfirmstate() {
            return confirmstate;
        }

        public void setConfirmstate(int confirmstate) {
            this.confirmstate = confirmstate;
        }
    }

    public static class RfidBean {
        /**
         * id : 354
         * devtype : sensor_electrombile_monitor
         * deveui : 0@004a7700620000c8
         * epcid : 300833b2ddd9014000150527
         * state : 1
         * collecttime : 1554974727000
         * userId : 13510946094
         * communityName : 南京大学
         * address : 深圳市南山区南京大学产学研基地A305后门
         */

        private int id;
        private String devtype;
        private String deveui;
        private String epcid;
        private int state;
        private long collecttime;
        private String userId;
        private String communityName;
        private String address;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getDevtype() {
            return devtype;
        }

        public void setDevtype(String devtype) {
            this.devtype = devtype;
        }

        public String getDeveui() {
            return deveui;
        }

        public void setDeveui(String deveui) {
            this.deveui = deveui;
        }

        public String getEpcid() {
            return epcid;
        }

        public void setEpcid(String epcid) {
            this.epcid = epcid;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public long getCollecttime() {
            return collecttime;
        }

        public void setCollecttime(long collecttime) {
            this.collecttime = collecttime;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getCommunityName() {
            return communityName;
        }

        public void setCommunityName(String communityName) {
            this.communityName = communityName;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }
    }


}
