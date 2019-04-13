package com.share_will.mobile.model;

import com.share_will.mobile.ApiService;
import com.share_will.mobile.model.entity.DepositRefundEntity;
import com.ubock.library.base.BaseEntity;
import com.ubock.library.base.BaseModel;
import com.ubock.library.http.ApiClient;

import rx.Observable;

public class RefundModel extends BaseModel {

    public Observable<BaseEntity<Object>> applyRefund(String userId,
                                                      String bankUserName,
                                                      String bankName,
                                                      String bankCard,
                                                      String desc){
        Observable<BaseEntity<Object>> ret = ApiClient.getInstance().getApiService(ApiService.class).applyRefund(userId,
                bankUserName, bankName, bankCard, desc);
        return ret;
    }

    public Observable<BaseEntity<Object>> cancelApplyRefund(long id){
        Observable<BaseEntity<Object>> ret = ApiClient.getInstance().getApiService(ApiService.class).cancelApplyRefund(id);
        return ret;
    }

    public Observable<BaseEntity<DepositRefundEntity>> applyRefundDetail(String userId){
        Observable<BaseEntity<DepositRefundEntity>> ret = ApiClient.getInstance().getApiService(ApiService.class).applyRefundDetail(userId);
        return ret;
    }
}
