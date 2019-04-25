package com.share_will.mobile.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class UserInfo implements Serializable, Parcelable {

    /**
     * payTime : 10
     * expirationTime : 10
     * vip : false
     * account : 0
     */

    private long payTime;
    private long expirationTime;
    private boolean vip;
    private long account;
    private long beginTime;
    private int deposit;
    private String causeStatus;
    private boolean adminStatus;

    public boolean isAdminStatus() {
        return adminStatus;
    }

    public void setAdminStatus(boolean adminStatus) {
        this.adminStatus = adminStatus;
    }

    private String userName;
    private String userId;
    private String token;

    public long getPayTime() {
        return payTime;
    }

    public void setPayTime(long payTime) {
        this.payTime = payTime;
    }

    public long getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(long expirationTime) {
        this.expirationTime = expirationTime;
    }

    public boolean isVip() {
        return vip;
    }

    public void setVip(boolean vip) {
        this.vip = vip;
    }

    public long getAccount() {
        return account;
    }

    public void setAccount(long account) {
        this.account = account;
    }

    public long getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(long beginTime) {
        this.beginTime = beginTime;
    }

    public int getDeposit() {
        return deposit;
    }

    public void setDeposit(int deposit) {
        this.deposit = deposit;
    }

    public String getCauseStatus() {
        return causeStatus;
    }

    public void setCauseStatus(String causeStatus) {
        this.causeStatus = causeStatus;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.payTime);
        dest.writeLong(this.expirationTime);
        dest.writeByte(this.vip ? (byte) 1 : (byte) 0);
        dest.writeLong(this.account);
        dest.writeLong(this.beginTime);
        dest.writeInt(this.deposit);
        dest.writeString(this.causeStatus);
        dest.writeString(this.userName);
        dest.writeString(this.userId);
        dest.writeString(this.token);
    }

    public UserInfo() {
    }

    protected UserInfo(Parcel in) {
        this.payTime = in.readLong();
        this.expirationTime = in.readLong();
        this.vip = in.readByte() != 0;
        this.account = in.readLong();
        this.beginTime = in.readLong();
        this.deposit = in.readInt();
        this.causeStatus = in.readString();
        this.userName = in.readString();
        this.userId = in.readString();
        this.token = in.readString();
    }

    public static final Parcelable.Creator<UserInfo> CREATOR = new Parcelable.Creator<UserInfo>() {
        @Override
        public UserInfo createFromParcel(Parcel source) {
            return new UserInfo(source);
        }

        @Override
        public UserInfo[] newArray(int size) {
            return new UserInfo[size];
        }
    };
}
