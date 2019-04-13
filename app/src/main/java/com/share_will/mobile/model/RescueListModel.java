package com.share_will.mobile.model;

import com.share_will.mobile.ApiService;
import com.share_will.mobile.model.entity.CityEntity;
import com.share_will.mobile.model.entity.RescueEntity;
import com.ubock.library.base.BaseEntity;
import com.ubock.library.base.BaseModel;
import com.ubock.library.http.ApiClient;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

public class RescueListModel extends BaseModel {

    private List<RescueEntity> mRescueList = new ArrayList<>();

    public List<RescueEntity> getRescue(){
        return mRescueList;
    }
    public RescueEntity getRescue(int position){
        return mRescueList.get(position);
    }

    public Observable<BaseEntity<List<RescueEntity>>> getRescueList(String userId){
        Observable<BaseEntity<List<RescueEntity>>> ret = ApiClient.getInstance().getApiService(ApiService.class).getRescueList(userId);
        return ret.flatMap(new Func1<BaseEntity<List<RescueEntity>>, Observable<BaseEntity<List<RescueEntity>>>>() {
            @Override
            public Observable<BaseEntity<List<RescueEntity>>> call(BaseEntity<List<RescueEntity>> listBaseEntity) {
                mRescueList.clear();
                if (listBaseEntity != null && listBaseEntity.getData() != null){
                    mRescueList.addAll(listBaseEntity.getData());
                }
                return Observable.just(listBaseEntity);
            }
        });
    }

    public Observable<BaseEntity<Object>> cancelRescue(long id, String userId){
        Observable<BaseEntity<Object>> ret = ApiClient.getInstance().getApiService(ApiService.class).cancelRescue(id, userId);
        return ret;
    }

}
