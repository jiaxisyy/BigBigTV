package com.share_will.mobile.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class PackageEntity implements Serializable, Parcelable {

    /**
     * activityPoster : http://www.share-will.com/download/images/monthly_activity_422.jpg
     * activityContent : 优惠来啦！2018年4月23日~2018年7月23日：只要花199就能享受原价299不限次数的换电服务啦！
     * packageId : 1
     * packagePrice : 29900
     * description : 包一个月套餐享有一个月不限次数换电服务
     * sort : 2
     * activityId : 2
     * activityPrice : 19900
     * activityTitle : 首三月大优惠活动
     * customerId : 1
     * startTime : 2018-04-23 00:00:00
     * packageName : 一个月
     * endTime : 2018-07-23 23:59:59
     */

    private String poster;
    private String activityPoster;
    private String activityContent;
    private long packageId;
    private int packagePrice;
    private String description;
    private long sort;
    private long activityId;
    private int activityPrice;
    private String activityTitle;
    private long customerId;
    private String startTime;
    private String packageName;
    private String endTime;
    private int packageType;

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getActivityPoster() {
        return activityPoster;
    }

    public void setActivityPoster(String activityPoster) {
        this.activityPoster = activityPoster;
    }

    public String getActivityContent() {
        return activityContent;
    }

    public void setActivityContent(String activityContent) {
        this.activityContent = activityContent;
    }

    public long getPackageId() {
        return packageId;
    }

    public void setPackageId(long packageId) {
        this.packageId = packageId;
    }

    public int getPackagePrice() {
        return packagePrice;
    }

    public void setPackagePrice(int packagePrice) {
        this.packagePrice = packagePrice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getSort() {
        return sort;
    }

    public void setSort(long sort) {
        this.sort = sort;
    }

    public long getActivityId() {
        return activityId;
    }

    public void setActivityId(long activityId) {
        this.activityId = activityId;
    }

    public int getActivityPrice() {
        return activityPrice;
    }

    public void setActivityPrice(int activityPrice) {
        this.activityPrice = activityPrice;
    }

    public String getActivityTitle() {
        return activityTitle;
    }

    public void setActivityTitle(String activityTitle) {
        this.activityTitle = activityTitle;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getPackageType() {
        return packageType;
    }

    public void setPackageType(int packageType) {
        this.packageType = packageType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.poster);
        dest.writeString(this.activityPoster);
        dest.writeString(this.activityContent);
        dest.writeLong(this.packageId);
        dest.writeInt(this.packagePrice);
        dest.writeString(this.description);
        dest.writeLong(this.sort);
        dest.writeLong(this.activityId);
        dest.writeInt(this.activityPrice);
        dest.writeString(this.activityTitle);
        dest.writeLong(this.customerId);
        dest.writeString(this.startTime);
        dest.writeString(this.packageName);
        dest.writeString(this.endTime);
        dest.writeInt(this.packageType);
    }

    public PackageEntity() {
    }

    protected PackageEntity(Parcel in) {
        this.poster = in.readString();
        this.activityPoster = in.readString();
        this.activityContent = in.readString();
        this.packageId = in.readLong();
        this.packagePrice = in.readInt();
        this.description = in.readString();
        this.sort = in.readLong();
        this.activityId = in.readLong();
        this.activityPrice = in.readInt();
        this.activityTitle = in.readString();
        this.customerId = in.readLong();
        this.startTime = in.readString();
        this.packageName = in.readString();
        this.endTime = in.readString();
        this.packageType = in.readInt();
    }

    public static final Parcelable.Creator<PackageEntity> CREATOR = new Parcelable.Creator<PackageEntity>() {
        @Override
        public PackageEntity createFromParcel(Parcel source) {
            return new PackageEntity(source);
        }

        @Override
        public PackageEntity[] newArray(int size) {
            return new PackageEntity[size];
        }
    };

    @Override
    public String toString() {
        return "PackageEntity{" +
                "poster='" + poster + '\'' +
                "activityPoster='" + activityPoster + '\'' +
                ", activityContent='" + activityContent + '\'' +
                ", packageId=" + packageId +
                ", packagePrice=" + packagePrice +
                ", description='" + description + '\'' +
                ", sort=" + sort +
                ", activityId=" + activityId +
                ", activityPrice=" + activityPrice +
                ", activityTitle='" + activityTitle + '\'' +
                ", customerId=" + customerId +
                ", startTime='" + startTime + '\'' +
                ", packageName='" + packageName + '\'' +
                ", endTime='" + endTime + '\'' +
                ", packageType=" + packageType +
                '}';
    }
}
