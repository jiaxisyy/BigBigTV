package com.share_will.mobile.model;

import com.share_will.mobile.ApiService;
import com.share_will.mobile.model.entity.ChargeBatteryEntity;
import com.ubock.library.base.BaseEntity;
import com.ubock.library.base.BaseModel;
import com.ubock.library.http.ApiClient;

import rx.Observable;

public class HomeServiceModel extends BaseModel {
    public Observable<BaseEntity<Object>> chargeScan(String userId, String token,
                                                     String cabinetId,
                                                     String time,
                                                     int type,
                                                     int appType) {
        return ApiClient.getInstance().getApiService(ApiService.class).chargeScan(userId, token, cabinetId, time, type, appType);
    }
    public Observable<BaseEntity<Object>> stopChargeScan(String userId, String token,
                                                     String cabinetId) {
        return ApiClient.getInstance().getApiService(ApiService.class).stopChargeScan(userId, token, cabinetId);
    }

    public Observable<BaseEntity<ChargeBatteryEntity>> getChargeBatteryInfo(String userId, String token) {
        return ApiClient.getInstance().getApiService(ApiService.class).getChargeBatteryInfo(userId, token);
    }
}
