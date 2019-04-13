package com.share_will.mobile.ui.views;

import com.share_will.mobile.model.entity.ChargeBatteryEntity;
import com.ubock.library.base.BaseEntity;
import com.ubock.library.base.BaseView;

public interface IHomeServiceView extends BaseView {

    /**
     * 充电回调
     */
    void OnChargeResult(BaseEntity<ChargeBatteryEntity> s);

    /**
     * 充电扫码回调
     */
    void OnChargeScanResult(BaseEntity<Object> s);

    /**
     * 停止充电扫码回调
     */
    void OnStopChargeScanResult(BaseEntity<Object> s);


}
