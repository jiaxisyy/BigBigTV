package com.share_will.mobile.model;

import com.share_will.mobile.ApiService;
import com.share_will.mobile.model.entity.UserInfo;
import com.ubock.library.base.BaseEntity;
import com.ubock.library.base.BaseModel;
import com.ubock.library.http.ApiClient;

import rx.Observable;

public class LoginModel extends BaseModel {

    public Observable<BaseEntity<UserInfo>> login(String userId, String password, int userType){
        return ApiClient.getInstance().getApiService(ApiService.class).login(userId, password, userType);
    }


}
