package com.share_will.mobile.model.entity;

import java.util.List;

public class BannerEntity {

    /**
     * advert_path : http://www.share-will.com:80/upload/yipai_poster_3.jpg
     * advert_name : 蚁电
     * advert_duration : 5
     */

    private String advert_path;
    private String advert_name;
    private int advert_duration;

    public String getAdvert_path() {
        return advert_path;
    }

    public void setAdvert_path(String advert_path) {
        this.advert_path = advert_path;
    }

    public String getAdvert_name() {
        return advert_name;
    }

    public void setAdvert_name(String advert_name) {
        this.advert_name = advert_name;
    }

    public int getAdvert_duration() {
        return advert_duration;
    }

    public void setAdvert_duration(int advert_duration) {
        this.advert_duration = advert_duration;
    }

}
