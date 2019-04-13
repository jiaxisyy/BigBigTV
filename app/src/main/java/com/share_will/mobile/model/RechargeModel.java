package com.share_will.mobile.model;

import com.share_will.mobile.ApiService;
import com.ubock.library.base.BaseEntity;
import com.ubock.library.base.BaseModel;
import com.ubock.library.http.ApiClient;

import rx.Observable;

public class RechargeModel extends BaseModel {

    public Observable<BaseEntity<String>> crateRechargeOrder(String userId, int money){
        return ApiClient.getInstance().getApiService(ApiService.class).crateRechargeOrder(userId, money);
    }
}
