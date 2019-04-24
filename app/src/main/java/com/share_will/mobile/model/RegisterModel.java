package com.share_will.mobile.model;

import com.share_will.mobile.ApiService;
import com.share_will.mobile.model.entity.CityEntity;
import com.share_will.mobile.model.entity.StationEntity;
import com.share_will.mobile.model.entity.UserInfo;
import com.ubock.library.base.BaseEntity;
import com.ubock.library.base.BaseModel;
import com.ubock.library.http.ApiClient;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

public class RegisterModel extends BaseModel {

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
        if (mStationList.size() > 0) {
            return mStationList.get(position);
        } else {
            return null;
        }
    }

    public Observable<BaseEntity<Object>> register(String userId, String userName, String password, String verCode, String customer, String stationId) {
        return ApiClient.getInstance().getApiService(ApiService.class).register(userId, userName, password, verCode, customer, stationId);
    }

    public Observable<BaseEntity<Object>> sendVerifyCode(String userId) {
        return ApiClient.getInstance().getApiService(ApiService.class).sendVerifyCode(userId);
    }

    public Observable<BaseEntity<List<StationEntity>>> getStationList(String areaCode,int type) {
        Observable<BaseEntity<List<StationEntity>>> ret = ApiClient.getInstance().getApiService(ApiService.class).getStationList(areaCode,type);
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

    public Observable<BaseEntity<List<StationEntity>>> getStationForPhone(String phone) {
        return ApiClient.getInstance().getApiService(ApiService.class).getStationForPhone(phone);
    }
}
