package com.share_will.mobile.model;

import com.share_will.mobile.ApiService;
import com.share_will.mobile.model.entity.PackageEntity;
import com.share_will.mobile.model.entity.PackageOrderEntity;
import com.share_will.mobile.model.entity.UserInfo;
import com.ubock.library.base.BaseEntity;
import com.ubock.library.base.BaseModel;
import com.ubock.library.http.ApiClient;

import java.util.List;

import rx.Observable;

public class ShopModel extends BaseModel {

    public Observable<BaseEntity<List<PackageEntity>>> getPackageList(String userId){
        return ApiClient.getInstance().getApiService(ApiService.class).getPackageList(userId);
    }

    public Observable<BaseEntity<PackageOrderEntity>> createPackageOrder(String userId, long packageId, long activityId, int money, int type){
        return ApiClient.getInstance().getApiService(ApiService.class).createPackageOrder(userId, packageId, activityId, money, type);
    }
}
