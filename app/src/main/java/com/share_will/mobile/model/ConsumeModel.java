package com.share_will.mobile.model;

import com.share_will.mobile.ApiService;
import com.share_will.mobile.model.entity.RecordEntity;
import com.share_will.mobile.model.entity.UserInfo;
import com.ubock.library.base.BaseEntity;
import com.ubock.library.base.BaseModel;
import com.ubock.library.http.ApiClient;

import java.util.List;

import rx.Observable;

public class ConsumeModel extends BaseModel {

    public Observable<BaseEntity<List<RecordEntity>>> getConsumeList(String userId, int page, int size){
        return ApiClient.getInstance().getApiService(ApiService.class).getConsumeList(userId,page, size);
    }
}
