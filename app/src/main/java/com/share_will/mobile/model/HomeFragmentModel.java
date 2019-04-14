package com.share_will.mobile.model;

import com.share_will.mobile.ApiService;
import com.share_will.mobile.model.entity.AlarmEntity;
import com.share_will.mobile.model.entity.BatteryEntity;
import com.share_will.mobile.model.entity.ChargeBatteryEntity;
import com.ubock.library.base.BaseEntity;
import com.ubock.library.base.BaseModel;
import com.ubock.library.http.ApiClient;

import java.util.List;

import rx.Completable;
import rx.Observable;

public class HomeFragmentModel extends BaseModel {

    public Observable<BaseEntity<AlarmEntity>> getAlarmList(String userId, String token) {
        return ApiClient.getInstance().getApiService(ApiService.class).getAlarmList(userId, token);
    }

    public Observable<BaseEntity<ChargeBatteryEntity>> getChargeBatteryInfo(String userId, String token) {
        return ApiClient.getInstance().getApiService(ApiService.class).getChargeBatteryInfo(userId, token);
    }

    public Observable<BaseEntity<BatteryEntity>> getBatteryInfo(String userId, String token) {
        return ApiClient.getInstance().getApiService(ApiService.class).getBatteryInfo(userId, token);
    }

    public Observable<BaseEntity<Object>> bindBattery(String userId, String token) {
        return ApiClient.getInstance().getApiService(ApiService.class).bindBattery(userId, token);
    }

}
