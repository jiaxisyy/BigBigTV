package com.share_will.mobile.model;

import com.share_will.mobile.ApiService;
import com.share_will.mobile.model.entity.FixStationEnity;
import com.ubock.library.base.BaseEntity;
import com.ubock.library.base.BaseModel;
import com.ubock.library.http.ApiClient;

import java.util.List;

import rx.Observable;

public class FixMapModel extends BaseModel {

    public Observable<BaseEntity<List<FixStationEnity>>> loadFixStation() {
        return ApiClient.getInstance().getApiService(ApiService.class).loadFixStation();
    }
}
