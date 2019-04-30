package com.share_will.mobile.ui.views;

import com.share_will.mobile.model.entity.ChargeStakeEntity;
import com.ubock.library.base.BaseEntity;
import com.ubock.library.base.BaseView;

import java.util.List;

public interface ChargeStakeView extends BaseView {

    /**
     * 获取用户充电信息
     * @param data
     */
    default void onLoadChargingInfo(BaseEntity<ChargeStakeEntity> data){}

    /**
     * 获取充电桩状态
     * @param data
     */
    default void onLoadStakeStatus(BaseEntity<List<ChargeStakeEntity>> data){}

    /**
     * 充电桩充电或结束充电结果
     * @param data
     */
    default void onChargingResult(BaseEntity<Object> data){}

}
