package com.share_will.mobile.ui.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.share_will.mobile.R;
import com.share_will.mobile.model.entity.StationEntity;

import java.util.List;

public class StationForPhoneAdapter extends BaseQuickAdapter<StationEntity, BaseViewHolder> {
    public StationForPhoneAdapter(int layoutResId, @Nullable List<StationEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, StationEntity item) {
        helper.setText(R.id.tv_item_select_station_info, item.getStationName());
        helper.setVisible(R.id.iv_item_select_station_selected, item.isClick());
    }


}
