package com.share_will.mobile.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class RescueEntity implements Parcelable {

    /**
     * id : 21
     * stationId : 24
     * userId : 13510946094
     * cabinetId : null
     * batteryId : null
     * door : null
     * rescueCause : 测试帐号
     * status : 0
     * auditor : 南京大学站点
     * resolve : null
     * verCode : null
     * active : true
     * createTime : 1530265212000
     * creator : 13510946094
     * modifyTime : null
     * modifier : null
     */

    private long id;
    private long stationId;
    private String userId;
    private String cabinetId;
    private String batteryId;
    private int door;
    private String rescueCause;
    private int status;
    private String auditor;
    private String resolve;
    private String verCode;
    private boolean active;
    private long createTime;
    private String creator;
    private String modifyTime;
    private String modifier;
    private String userName;
    private String customerName;
    private String stationName;
    private String stationMaster;
    private String stationMasterPhone;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getStationId() {
        return stationId;
    }

    public void setStationId(long stationId) {
        this.stationId = stationId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public int getDoor() {
        return door;
    }

    public void setDoor(int door) {
        this.door = door;
    }

    public String getRescueCause() {
        return rescueCause;
    }

    public void setRescueCause(String rescueCause) {
        this.rescueCause = rescueCause;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getAuditor() {
        return auditor;
    }

    public void setAuditor(String auditor) {
        this.auditor = auditor;
    }

    public String getResolve() {
        return resolve;
    }

    public void setResolve(String resolve) {
        this.resolve = resolve;
    }

    public String getVerCode() {
        return verCode;
    }

    public void setVerCode(String verCode) {
        this.verCode = verCode;
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

    public String getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(String modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeLong(this.stationId);
        dest.writeString(this.userId);
        dest.writeString(this.cabinetId);
        dest.writeString(this.batteryId);
        dest.writeInt(this.door);
        dest.writeString(this.rescueCause);
        dest.writeInt(this.status);
        dest.writeString(this.auditor);
        dest.writeString(this.resolve);
        dest.writeString(this.verCode);
        dest.writeByte(this.active ? (byte) 1 : (byte) 0);
        dest.writeLong(this.createTime);
        dest.writeString(this.creator);
        dest.writeString(this.modifyTime);
        dest.writeString(this.modifier);
        dest.writeString(this.userName);
        dest.writeString(this.customerName);
        dest.writeString(this.stationName);
        dest.writeString(this.stationMaster);
        dest.writeString(this.stationMasterPhone);
    }

    public RescueEntity() {
    }

    protected RescueEntity(Parcel in) {
        this.id = in.readLong();
        this.stationId = in.readLong();
        this.userId = in.readString();
        this.cabinetId = in.readString();
        this.batteryId = in.readString();
        this.door = in.readInt();
        this.rescueCause = in.readString();
        this.status = in.readInt();
        this.auditor = in.readString();
        this.resolve = in.readString();
        this.verCode = in.readString();
        this.active = in.readByte() != 0;
        this.createTime = in.readLong();
        this.creator = in.readString();
        this.modifyTime = in.readString();
        this.modifier = in.readString();
        this.userName = in.readString();
        this.customerName = in.readString();
        this.stationName = in.readString();
        this.stationMaster = in.readString();
        this.stationMasterPhone = in.readString();
    }

    public static final Parcelable.Creator<RescueEntity> CREATOR = new Parcelable.Creator<RescueEntity>() {
        @Override
        public RescueEntity createFromParcel(Parcel source) {
            return new RescueEntity(source);
        }

        @Override
        public RescueEntity[] newArray(int size) {
            return new RescueEntity[size];
        }
    };
}
