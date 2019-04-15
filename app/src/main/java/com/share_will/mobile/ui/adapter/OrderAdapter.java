package com.share_will.mobile.ui.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.share_will.mobile.R;
import com.share_will.mobile.model.entity.OrderDetailsEntity;

import java.util.List;

public class OrderAdapter extends BaseQuickAdapter<OrderDetailsEntity, BaseViewHolder> {


    public OrderAdapter(int layoutResId, @Nullable List<OrderDetailsEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, OrderDetailsEntity entity) {
        helper.setText(R.id.tv_item_my_order_name, entity.getName());
    }
}
