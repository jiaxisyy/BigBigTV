package com.share_will.mobile.model;

import com.share_will.mobile.App;
import com.share_will.mobile.Constant;
import com.share_will.mobile.model.entity.CityEntity;
import com.share_will.mobile.model.entity.UserInfo;
import com.ubock.library.base.BaseModel;
import com.ubock.library.utils.SharedPreferencesUtils;

/**
 * Created by ChenGD on 2017/3/15.
 */
public class GlobalModel extends BaseModel {

    private UserInfo mUserInfo;
    private CityEntity mCityEntity;

    public UserInfo getUserInfo() {
        return mUserInfo == null ? new UserInfo() : mUserInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.mUserInfo = userInfo;
    }

    public CityEntity getCityEntity() {
        return mCityEntity == null?new CityEntity():mCityEntity;
    }

    public void setCityEntity(CityEntity cityEntity) {
        this.mCityEntity = cityEntity;
    }

    @Override
    public void logout() {
        super.logout();
        setUserInfo(null);
        SharedPreferencesUtils.saveDeviceData(App.getContext(), Constant.USER_INFO, null);
    }
}
