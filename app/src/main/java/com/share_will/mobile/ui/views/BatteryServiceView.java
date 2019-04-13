package com.share_will.mobile.ui.views;

import com.ubock.library.base.BaseEntity;
import com.ubock.library.base.BaseView;

import java.util.Map;

public interface BatteryServiceView extends BaseView {

    /**
     * 获取电量回调
     * @param data
     */
    void onLoadBattery(BaseEntity<Map<String, String>> data);

}
