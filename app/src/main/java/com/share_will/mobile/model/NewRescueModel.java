package com.share_will.mobile.model;

import com.share_will.mobile.ApiService;
import com.share_will.mobile.model.entity.CityEntity;
import com.share_will.mobile.model.entity.StationEntity;
import com.ubock.library.base.BaseEntity;
import com.ubock.library.base.BaseModel;
import com.ubock.library.http.ApiClient;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

public class NewRescueModel extends BaseModel {

    private List<CityEntity> mCityList = new ArrayList<>();
    private List<StationEntity> mStationList = new ArrayList<>();

    public List<CityEntity> getCity() {
        return mCityList;
    }

    public CityEntity getCity(int position) {
        return mCityList.get(position);
    }

    public List<StationEntity> getStation() {
        return mStationList;
    }

    public StationEntity getStation(int position) {
        return mStationList.get(position);
    }

    public Observable<BaseEntity<List<StationEntity>>> getStationList(String areaCode) {
        Observable<BaseEntity<List<StationEntity>>> ret = ApiClient.getInstance().getApiService(ApiService.class).getStationList(areaCode);
        return ret.flatMap(new Func1<BaseEntity<List<StationEntity>>, Observable<BaseEntity<List<StationEntity>>>>() {
            @Override
            public Observable<BaseEntity<List<StationEntity>>> call(BaseEntity<List<StationEntity>> listBaseEntity) {
                mStationList.clear();
                if (listBaseEntity != null && listBaseEntity.getData() != null) {
                    mStationList.addAll(listBaseEntity.getData());
                }
                return Observable.just(listBaseEntity);
            }
        });
    }

    public Observable<BaseEntity<List<CityEntity>>> getCityList() {
        Observable<BaseEntity<List<CityEntity>>> ret = ApiClient.getInstance().getApiService(ApiService.class).getCityList();
        return ret.flatMap(new Func1<BaseEntity<List<CityEntity>>, Observable<BaseEntity<List<CityEntity>>>>() {
            @Override
            public Observable<BaseEntity<List<CityEntity>>> call(BaseEntity<List<CityEntity>> listBaseEntity) {
                mCityList.clear();
                if (listBaseEntity != null && listBaseEntity.getData() != null) {
                    mCityList.addAll(listBaseEntity.getData());
                }
                return Observable.just(listBaseEntity);
            }
        });
    }

    public Observable<BaseEntity<Object>> applyRescue(long stationId, String userId, String rescueCause) {
        Observable<BaseEntity<Object>> ret = ApiClient.getInstance().getApiService(ApiService.class).applyRescue(stationId, userId, rescueCause);
        return ret;
    }

}
