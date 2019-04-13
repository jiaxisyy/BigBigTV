package com.share_will.mobile.model.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;

import java.io.Serializable;
import java.util.Date;

import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by ChenGD on 2018/3/22.
 *
 * @author chenguandu
 */

@Entity(nameInDb = "location")
public class LocationEntity implements Serializable {
    private static final long serialVersionUID = 8063931607029857832L;
    @Id(autoincrement = true)
    @Unique
    private Long id;

    private double longitude;
    private double latitude;
    private float range;
    private long timestamp;
    private String date;

    @Generated(hash = 1137043500)
    public LocationEntity(Long id, double longitude, double latitude, float range,
            long timestamp, String date) {
        this.id = id;
        this.longitude = longitude;
        this.latitude = latitude;
        this.range = range;
        this.timestamp = timestamp;
        this.date = date;
    }

    @Generated(hash = 1723987110)
    public LocationEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public float getRange() {
        return range;
    }

    public void setRange(float range) {
        this.range = range;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "LocationEntity{" +
                "id=" + id +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", range=" + range +
                ", timestamp=" + timestamp +
                ", date='" + date + '\'' +
                '}';
    }
}
