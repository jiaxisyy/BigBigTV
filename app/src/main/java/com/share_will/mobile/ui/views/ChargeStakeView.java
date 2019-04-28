package com.share_will.mobile.ui.views;

import com.share_will.mobile.model.entity.ChargeStakeEntity;
import com.ubock.library.base.BaseEntity;
import com.ubock.library.base.BaseView;

public interface ChargeStakeView extends BaseView {

    /**
     * 获取用户充电信息
     * @param data
     */
    void onLoadChargingInfo(BaseEntity<ChargeStakeEntity> data);

    /**
     * 获取充电桩状态
     * @param data
     */
    void onLoadStakeStatus(BaseEntity<Object> data);

    /**
     * 充电桩充电或结束充电结果
     * @param data
     */
    void onChargingResult(BaseEntity<Object> data);

}
