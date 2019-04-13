package com.share_will.mobile.model;

import com.share_will.mobile.ApiService;
import com.share_will.mobile.model.entity.ChargingEntity;
import com.ubock.library.base.BaseEntity;
import com.ubock.library.base.BaseModel;
import com.ubock.library.http.ApiClient;

import java.util.List;

import rx.Observable;

public class ChargingModel extends BaseModel {

    public Observable<BaseEntity<List<ChargingEntity>>> getChargingList(String sn){
        return ApiClient.getInstance().getApiService(ApiService.class).getChargingList(sn);
    }

    public Observable<BaseEntity<Object>> createChargingOrder(String sn, String userid, int channel, String chargeid){
        return ApiClient.getInstance().getApiService(ApiService.class).createChargingOrder(sn, userid ,channel, chargeid);
    }
}
