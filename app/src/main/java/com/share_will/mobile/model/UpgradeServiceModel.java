package com.share_will.mobile.model;

import com.share_will.mobile.ApiService;
import com.ubock.library.base.BaseEntity;
import com.ubock.library.base.BaseModel;
import com.ubock.library.http.ApiClient;

import java.util.Map;

import rx.Observable;

public class UpgradeServiceModel extends BaseModel {

    public Observable<BaseEntity<Map<String, String>>> checkVersion(String versionName,
                                                                  int versionCode,
                                                                  int type,
                                                                  String customer,
                                                                  String userId){
        return ApiClient.getInstance().getApiService(ApiService.class).checkVersion(versionName,
                                                                                versionCode,
                                                                                type,
                                                                                customer,
                                                                                userId);
    }
}
