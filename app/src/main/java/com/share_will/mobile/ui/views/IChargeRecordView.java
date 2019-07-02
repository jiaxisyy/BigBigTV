package com.share_will.mobile.ui.views;

import com.share_will.mobile.model.entity.ChargeRecordEntity;
import com.ubock.library.base.BaseEntity;
import com.ubock.library.base.BaseView;

import java.util.List;
import java.util.Map;

public interface IChargeRecordView extends BaseView {


    void onLoadChargeRecordResult(BaseEntity<List<ChargeRecordEntity>> data);
}
