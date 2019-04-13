package com.share_will.mobile.ui.views;

import com.share_will.mobile.model.entity.CityEntity;
import com.share_will.mobile.model.entity.StationEntity;
import com.ubock.library.base.BaseEntity;
import com.ubock.library.base.BaseView;

import java.util.List;

public interface NewRescueView extends BaseView {

    /**
     * 获取站点列表回调
     * @param ret
     */
    void onLoadStationList(BaseEntity<List<StationEntity>> ret);

    /**
     * 获取开通城市列表回调
     * @param ret 返回数据
     */
    void onLoadCityList(BaseEntity<List<CityEntity>> ret);

    /**
     * 申请救援回调
     * @param ret 返回数据
     */
    void onApplyRescue(BaseEntity<Object> ret);

}
