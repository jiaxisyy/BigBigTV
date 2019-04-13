package com.share_will.mobile.model;

import com.share_will.mobile.ApiService;
import com.share_will.mobile.model.entity.UserInfo;
import com.ubock.library.base.BaseEntity;
import com.ubock.library.base.BaseModel;
import com.ubock.library.http.ApiClient;

import java.util.Map;

import rx.Observable;

public class BatteryServiceModel extends BaseModel {

    public Observable<BaseEntity<Map<String, String>>> getBattery(String userId){
        return ApiClient.getInstance().getApiService(ApiService.class).getBattery(userId);
    }
}
