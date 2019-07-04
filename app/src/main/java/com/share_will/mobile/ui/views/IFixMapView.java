package com.share_will.mobile.ui.views;

import com.share_will.mobile.model.entity.FixStationEnity;
import com.ubock.library.base.BaseEntity;
import com.ubock.library.base.BaseView;

import java.util.List;

public interface IFixMapView extends BaseView {

    void onLoadStationResult(BaseEntity<List<FixStationEnity>> data);

}
