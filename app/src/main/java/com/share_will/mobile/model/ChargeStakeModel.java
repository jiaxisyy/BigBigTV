package com.share_will.mobile.model;

import com.share_will.mobile.ApiService;
import com.share_will.mobile.model.entity.ChargeStakeEntity;
import com.share_will.mobile.model.entity.ChargeStakeOrderInfoEntity;
import com.ubock.library.base.BaseEntity;
import com.ubock.library.base.BaseModel;
import com.ubock.library.http.ApiClient;

import java.util.List;

import rx.Observable;

public class ChargeStakeModel extends BaseModel {

    public Observable<BaseEntity<ChargeStakeOrderInfoEntity>> getChargingInfo(){
        return ApiClient.getInstance().getApiService(ApiService.class).getChargingInfo();
    }

    public Observable<BaseEntity<List<ChargeStakeEntity>>> getStakeStatus(String cabinetId){
        return ApiClient.getInstance().getApiService(ApiService.class).getStakeStatus(cabinetId);
    }

    public Observable<BaseEntity<Object>> stakeCharging(String cabinetId,String userId,int index,int status){
        return ApiClient.getInstance().getApiService(ApiService.class).stakeCharging(cabinetId,userId,index,status);
    }
}
