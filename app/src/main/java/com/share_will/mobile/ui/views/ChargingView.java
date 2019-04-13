package com.share_will.mobile.ui.views;

import com.share_will.mobile.model.entity.ChargingEntity;
import com.share_will.mobile.model.entity.UserInfo;
import com.ubock.library.base.BaseEntity;
import com.ubock.library.base.BaseView;

import java.util.List;

public interface ChargingView extends BaseView {

    /**
     * 获取充电选项回调
     * @param data
     */
    void onLoadChargingList(BaseEntity<List<ChargingEntity>> data);

    /**
     * 生成订单回调
     * @param data
     */
    void onCreateChargingOrder(BaseEntity<Object> data);

}
