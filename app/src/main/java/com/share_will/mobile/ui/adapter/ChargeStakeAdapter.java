package com.share_will.mobile.ui.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.share_will.mobile.R;

import java.util.List;

public class ChargeStakeAdapter extends BaseQuickAdapter<Integer, BaseViewHolder> {
    public ChargeStakeAdapter(int layoutResId, @Nullable List<Integer> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Integer item) {

        helper.setText(R.id.tv_item_charge_stake_num, item+"");

    }
}
