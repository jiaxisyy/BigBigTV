package com.share_will.mobile.ui.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.share_will.mobile.R;
import com.share_will.mobile.model.entity.BespeakEntity;

import java.util.List;

public class BespeakAdapter extends BaseQuickAdapter<BespeakEntity, BaseViewHolder> {


    public BespeakAdapter(int layoutResId, @Nullable List<BespeakEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BespeakEntity item) {
        helper.setText(R.id.item_tv_bespeak_catalog, item.getCatalog());
        helper.setText(R.id.item_tv_bespeak_result, item.getResult());
    }
}
