package com.share_will.mobile.model;

import com.share_will.mobile.ApiService;
import com.share_will.mobile.model.entity.ChargeRecordEntity;
import com.ubock.library.base.BaseEntity;
import com.ubock.library.base.BaseModel;
import com.ubock.library.http.ApiClient;

import java.util.List;

import retrofit2.http.Field;
import rx.Observable;

public class ChargeRecordModel extends BaseModel {

    public Observable<BaseEntity<List<ChargeRecordEntity>>> getChargeRecordList(String userId, int pn, int ps) {
        return ApiClient.getInstance().getApiService(ApiService.class).getChargeRecordList(userId, pn, ps);
    }
}
