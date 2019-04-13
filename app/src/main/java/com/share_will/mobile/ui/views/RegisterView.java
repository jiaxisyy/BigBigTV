package com.share_will.mobile.ui.views;

import com.share_will.mobile.model.entity.CityEntity;
import com.share_will.mobile.model.entity.StationEntity;
import com.ubock.library.base.BaseEntity;
import com.ubock.library.base.BaseView;

import java.util.List;

public interface RegisterView extends BaseView {

    /**
     * 注册回调
     *
     * @param success 注册成功
     * @param message 消息
     */
    void onRegister(boolean success, String message);

    /**
     * 发送验证码回调
     *
     * @param success 发送验证码成功
     * @param message 消息
     */
    void onSendVerifyCode(boolean success, String message);

    /**
     * 获取站点列表回调
     *
     * @param ret
     */
    void onLoadStationList(BaseEntity<List<StationEntity>> ret);

    /**
     * 获取开通城市列表回调
     *
     * @param ret 返回数据
     */
    void onLoadCityList(BaseEntity<List<CityEntity>> ret);


    /**
     * 通过站长手机号查询站点
     *
     * @param ret
     */
    void onLoadStationForPhone(BaseEntity<List<StationEntity>> ret);

}
