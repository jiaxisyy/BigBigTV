package com.share_will.mobile.ui.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.share_will.mobile.R;
import com.share_will.mobile.model.entity.BespeakEntity;
import com.share_will.mobile.model.entity.HomeTopBarEntity;

import java.util.List;

public class HomeTopBarAdapter extends BaseQuickAdapter<HomeTopBarEntity, BaseViewHolder> {


    public HomeTopBarAdapter(int layoutResId, @Nullable List<HomeTopBarEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, HomeTopBarEntity item) {
        helper.setImageResource(R.id.ivItemHomeTopBarIcon, item.getIcon());
        helper.setText(R.id.tvItemHomeTopBarTitle, item.getTitle());

    }


}
