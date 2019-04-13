package com.share_will.mobile.model;

import com.share_will.mobile.ApiService;
import com.ubock.library.base.BaseEntity;
import com.ubock.library.base.BaseModel;
import com.ubock.library.http.ApiClient;

import java.util.Map;

import rx.Observable;

public class PayModel extends BaseModel {

    public Observable<BaseEntity<String>> crateRechargeOrder(String userId, int money){
        return ApiClient.getInstance().getApiService(ApiService.class).crateRechargeOrder(userId, money);
    }

    public Observable<BaseEntity<String>> getAliPayOrder(int payType,
                                                         String appId,
                                                         int orderType,
                                                         String orderId,
                                                         String body){
        return ApiClient.getInstance().getApiService(ApiService.class).getAliPayOrder(payType, appId,orderType,orderId,body);
    }

    public Observable<BaseEntity<Map<String, String>>> getWeiXinOrder(String appId, int orderType,
                                                                      String orderId,
                                                                      String body){
        return ApiClient.getInstance().getApiService(ApiService.class).getWeiXinOrder(appId, orderType,orderId,body);
    }

    public Observable<BaseEntity<Object>> payPackageOrder(String userId, String orderId){
        return ApiClient.getInstance().getApiService(ApiService.class).payPackageOrder(userId, orderId);
    }
    public Observable<BaseEntity<Object>> payMoneyOrder(String userId, String orderId){
        return ApiClient.getInstance().getApiService(ApiService.class).payMoneyOrder(userId, orderId);
    }
}
