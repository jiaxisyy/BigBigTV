package com.share_will.mobile.ui.views;

import com.share_will.mobile.model.entity.CabinetEntity;
import com.share_will.mobile.model.entity.CityEntity;
import com.share_will.mobile.model.entity.NotifyMessageEntity;
import com.ubock.library.base.BaseEntity;
import com.ubock.library.base.BaseView;

import java.util.List;
import java.util.Map;

public interface HomeView extends BaseView {

    /**
     * 加载机柜列表回调
     *
     * @param ret 返回数据
     */
    void onLoadCabinetList(BaseEntity<List<CabinetEntity>> ret);

    /**
     * 获取可换电池数量回调
     *
     * @param ret 返回数据
     */
    void onLoadFullBattery(BaseEntity<Map<String, Integer>> ret);

    /**
     * 扫码换电回调
     *
     * @param ret 返回数据
     */
    void onScanCode(BaseEntity<Object> ret);

    /**
     * 充电
     */
    default void chargingBattery(String sn, int channel){}

    /**
     * 获取开通城市列表回调
     *
     * @param ret 返回数据
     */
    void onLoadCityList(BaseEntity<List<CityEntity>> ret);

    /**
     * 每日提示
     *
     * @param isShow
     * @param data
     */
    default void showNotifyMessage(boolean isShow, NotifyMessageEntity data){}

    default int getTabSize(){return 0;}

    default void goTo(int tabIndex){}

}
