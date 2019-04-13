package com.share_will.mobile.model;

import com.share_will.mobile.ApiService;
import com.share_will.mobile.model.entity.BespeakEntity;
import com.ubock.library.base.BaseEntity;
import com.ubock.library.base.BaseModel;
import com.ubock.library.http.ApiClient;

import rx.Observable;

public class BespeakModel extends BaseModel {

    public Observable<BaseEntity<BespeakEntity.DataBean>> getBespeakInfo(String userId) {
        return ApiClient.getInstance().getApiService(ApiService.class).getBespeakInfo(userId);
    }

    public Observable<BaseEntity<BespeakEntity.AddDataBean>> addBespeak(String userId, String cabinetId) {
        return ApiClient.getInstance().getApiService(ApiService.class).addBespeak(userId, cabinetId);
    }

    public Observable<BaseEntity<Object>> updateBespeak(String id, String userId, String status, String desc) {
        return ApiClient.getInstance().getApiService(ApiService.class).updateBespeak(id, userId, status, desc);
    }

}
