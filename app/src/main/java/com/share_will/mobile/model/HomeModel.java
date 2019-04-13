package com.share_will.mobile.model;

import com.share_will.mobile.ApiService;
import com.share_will.mobile.model.entity.CabinetEntity;
import com.share_will.mobile.model.entity.CityEntity;
import com.share_will.mobile.model.entity.NotifyMessageEntity;
import com.ubock.library.base.BaseEntity;
import com.ubock.library.base.BaseModel;
import com.ubock.library.http.ApiClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.functions.Func1;

public class HomeModel extends BaseModel {

    private List<CityEntity> mCityList = new ArrayList<>();
    public List<CityEntity> getCity(){
        return mCityList;
    }

    public CityEntity getCity(int position){
        return mCityList.get(position);
    }

    public Observable<BaseEntity<List<CabinetEntity>>> getCabinetList(String city,double longitude, double latitude){
        return ApiClient.getInstance().getApiService(ApiService.class).getCabinetList(city, longitude, latitude);
    }

    public Observable<BaseEntity<Map<String, Integer>>> getFullBattery(String sn){
        return ApiClient.getInstance().getApiService(ApiService.class).getFullBattery(sn);
    }

    public Observable<BaseEntity<Object>> scanCodeLogin(String userId,String cabinetId,String time, int appType){
        return ApiClient.getInstance().getApiService(ApiService.class).scanCodeLogin(userId,cabinetId,time, appType);
    }

    public Observable<BaseEntity<List<CityEntity>>> getCityList(){
        Observable<BaseEntity<List<CityEntity>>> ret = ApiClient.getInstance().getApiService(ApiService.class).getCityList();
        return ret.flatMap(new Func1<BaseEntity<List<CityEntity>>, Observable<BaseEntity<List<CityEntity>>>>() {
            @Override
            public Observable<BaseEntity<List<CityEntity>>> call(BaseEntity<List<CityEntity>> listBaseEntity) {
                mCityList.clear();
                if (listBaseEntity != null && listBaseEntity.getData() != null){
                    mCityList.addAll(listBaseEntity.getData());
                }
                return Observable.just(listBaseEntity);
            }
        });
    }

    public Observable<BaseEntity<NotifyMessageEntity>> getNotifyMessage(String userId) {
        return ApiClient.getInstance().getApiService(ApiService.class).getNotifyMessage(userId);
    }

}
