package com.share_will.mobile.model;

import com.share_will.mobile.ApiService;
import com.share_will.mobile.model.entity.UserInfo;
import com.ubock.library.base.BaseEntity;
import com.ubock.library.base.BaseModel;
import com.ubock.library.http.ApiClient;

import rx.Observable;

public class UserCenterModel extends BaseModel {

    public Observable<BaseEntity<UserInfo>> getBalance(String userId){
        return ApiClient.getInstance().getApiService(ApiService.class).getBalance(userId);
    }
    /**
     * 登录机柜后台
     *
     */
    public Observable<BaseEntity<Object>> loginCMS(String loginName, String cabinetId, int userType){
        return ApiClient.getInstance().getApiService(ApiService.class).loginCMS(loginName, cabinetId, userType);
    }

    public Observable<BaseEntity<Object>> exceptionScanCodeGetBattery(String cabinetId, String userId){
        return ApiClient.getInstance().getApiService(ApiService.class).exceptionScanCodeGetBattery(cabinetId, userId);
    }
}
