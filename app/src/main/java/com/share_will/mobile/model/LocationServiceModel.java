package com.share_will.mobile.model;

import com.share_will.mobile.ApiService;
import com.ubock.library.base.BaseEntity;
import com.ubock.library.base.BaseModel;
import com.ubock.library.http.ApiClient;

import java.util.Map;

import rx.Observable;

public class LocationServiceModel extends BaseModel {

    public Observable<BaseEntity<Object>> uploadLocation(String userId, double longitude, double latitude, float range){
        return ApiClient.getInstance().getApiService(ApiService.class).uploadLocation(userId, longitude, latitude, range);
    }
}
