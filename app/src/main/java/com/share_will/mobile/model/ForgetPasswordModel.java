package com.share_will.mobile.model;

import com.share_will.mobile.ApiService;
import com.ubock.library.base.BaseEntity;
import com.ubock.library.base.BaseModel;
import com.ubock.library.http.ApiClient;

import rx.Observable;

public class ForgetPasswordModel extends BaseModel {

    public Observable<BaseEntity<Object>> updatePassword(String userId, String password, String verCode){
        return ApiClient.getInstance().getApiService(ApiService.class).updatePassword(userId, password, verCode);
    }

    public Observable<BaseEntity<Object>> sendVerifyCode(String userId){
        return ApiClient.getInstance().getApiService(ApiService.class).sendVerifyCode(userId);
    }
}
