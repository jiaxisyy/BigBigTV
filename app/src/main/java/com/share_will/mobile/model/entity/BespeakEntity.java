package com.share_will.mobile.model.entity;


/**
 * Created by Chenguandu on 2018/10/15.
 */
public class BespeakEntity {
    /**
     * 预约信息目录
     */
    private String catalog;
    /**
     * 预约信息结果
     */
    private String result;


    public static class DataBean {
        /**
         * door : 5
         * address : 深圳市南山区南京大学产学研基地305室
         * cabinetId : E201805301000001
         * batteryId : 1122110081330052
         * latitude : 22.528391
         * userId : 18328317002
         * createTime : 1541757469000
         * areaName : 深圳市
         * expirationTime : 1541758069000
         * stationName : 南京大学站点
         * id : 123
         * status : 1
         * desc : 预约成功
         * longitude : 113.944797
         */

        private int door;
        private String address;
        private String cabinetId;
        private String batteryId;
        private double latitude;
        private String userId;
        private long createTime;
        private String areaName;
        private long expirationTime;
        private String stationName;
        private int id;
        private int status;
        private String desc;
        private double longitude;

        public int getDoor() {
            return door;
        }

        public void setDoor(int door) {
            this.door = door;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getCabinetId() {
            return cabinetId;
        }

        public void setCabinetId(String cabinetId) {
            this.cabinetId = cabinetId;
        }

        public String getBatteryId() {
            return batteryId;
        }

        public void setBatteryId(String batteryId) {
            this.batteryId = batteryId;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public String getAreaName() {
            return areaName;
        }

        public void setAreaName(String areaName) {
            this.areaName = areaName;
        }

        public long getExpirationTime() {
            return expirationTime;
        }

        public void setExpirationTime(long expirationTime) {
            this.expirationTime = expirationTime;
        }

        public String getStationName() {
            return stationName;
        }

        public void setStationName(String stationName) {
            this.stationName = stationName;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }
    }


    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public BespeakEntity(String catalog, String result) {
        this.catalog = catalog;
        this.result = result;
    }

    public static class AddDataBean {


        /**
         * door : null
         * createTime : 1542165182733
         * expirationTime : 1542165782733
         * batteryId : null
         * id : 134
         * userId : 18328317002
         * cabinetTd : E201805301000001
         */

        private Object door;
        private long createTime;
        private long expirationTime;
        private Object batteryId;
        private int id;
        private String userId;
        private String cabinetTd;

        public Object getDoor() {
            return door;
        }

        public void setDoor(Object door) {
            this.door = door;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public long getExpirationTime() {
            return expirationTime;
        }

        public void setExpirationTime(long expirationTime) {
            this.expirationTime = expirationTime;
        }

        public Object getBatteryId() {
            return batteryId;
        }

        public void setBatteryId(Object batteryId) {
            this.batteryId = batteryId;
        }

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

        public String getCabinetTd() {
            return cabinetTd;
        }

        public void setCabinetTd(String cabinetTd) {
            this.cabinetTd = cabinetTd;
        }

    }

}
