package com.share_will.mobile.model.entity;

/**
 * 车辆维修站点信息
 */
public class FixStationEnity {


    /**
     * id : 189
     * areaCityId : 2
     * stationName : 广和电单车行
     * stationAddress : 广东省深圳市龙华区工业路广和电单车行
     * stationMaster :
     * stationMasterPhone : 13025453999
     * longitude : 114.042862
     * latitude : 22.70943
     * businessHours : 9：30-21：30
     * active : true
     * createTime : 1561513494000
     * creator : gangchens
     * modifyTime : null
     * modifier : null
     */

    private int id;
    private int areaCityId;
    private String stationName;
    private String stationAddress;
    private String stationMaster;
    private String stationMasterPhone;
    private double longitude;
    private double latitude;
    private String businessHours;
    private boolean active;
    private long createTime;
    private String creator;
    private Object modifyTime;
    private Object modifier;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAreaCityId() {
        return areaCityId;
    }

    public void setAreaCityId(int areaCityId) {
        this.areaCityId = areaCityId;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getStationAddress() {
        return stationAddress;
    }

    public void setStationAddress(String stationAddress) {
        this.stationAddress = stationAddress;
    }

    public String getStationMaster() {
        return stationMaster;
    }

    public void setStationMaster(String stationMaster) {
        this.stationMaster = stationMaster;
    }

    public String getStationMasterPhone() {
        return stationMasterPhone;
    }

    public void setStationMasterPhone(String stationMasterPhone) {
        this.stationMasterPhone = stationMasterPhone;
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

    public String getBusinessHours() {
        return businessHours;
    }

    public void setBusinessHours(String businessHours) {
        this.businessHours = businessHours;
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

}
