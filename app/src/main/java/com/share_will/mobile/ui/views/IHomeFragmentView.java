package com.share_will.mobile.ui.views;

import com.share_will.mobile.model.entity.AlarmEntity;
import com.share_will.mobile.model.entity.BatteryEntity;
import com.share_will.mobile.model.entity.BespeakEntity;
import com.share_will.mobile.model.entity.ChargeBatteryEntity;
import com.ubock.library.base.BaseEntity;
import com.ubock.library.base.BaseView;

import java.util.List;

public interface IHomeFragmentView extends BaseView {

    default void onLoadAlarmResult(BaseEntity<AlarmEntity> data) {
    }

    default void onLoadChargeBatteryInfoResult(BaseEntity<ChargeBatteryEntity> data) {
    }

    default void onLoadBatteryInfoResult(BaseEntity<BatteryEntity> data) {
    }


}
