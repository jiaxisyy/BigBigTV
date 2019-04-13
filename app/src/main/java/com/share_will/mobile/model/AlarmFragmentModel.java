package com.share_will.mobile.model;

import com.share_will.mobile.ApiService;
import com.ubock.library.base.BaseEntity;
import com.ubock.library.base.BaseModel;
import com.ubock.library.http.ApiClient;

import rx.Observable;

public class AlarmFragmentModel extends BaseModel {

    public Observable<BaseEntity<Object>> closeAlarm(String userId, String devtype, String deveui) {
        return ApiClient.getInstance().getApiService(ApiService.class).closeAlarm(userId, devtype, deveui);
    }
}
