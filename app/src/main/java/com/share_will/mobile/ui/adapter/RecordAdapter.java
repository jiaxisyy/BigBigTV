package com.share_will.mobile.ui.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseViewHolder;
import com.share_will.mobile.R;
import com.share_will.mobile.model.entity.RecordEntity;
import com.ubock.library.ui.adapter.LoadMoreAdapter;

import java.text.NumberFormat;

/**
 * Created by ChenGD on 2018/2/27.
 *
 * @author chenguandu
 */

public class RecordAdapter extends LoadMoreAdapter<RecordEntity, BaseViewHolder> {

    public RecordAdapter(Context context) {
        super(context, R.layout.record_item);
    }

    @Override
    protected void convert(BaseViewHolder helper, RecordEntity entity) {
        helper.setText(R.id.tv_item_consumption_time, entity.getDatetime());
        if (entity.getDeposit() > 0) {
            helper.setText(R.id.tv_item_consumption_type,"充值");
            helper.setImageResource(R.id.iv_item_consumption, R.drawable.icon_item_recharge);
            helper.setText(R.id.tv_item_consumption_money, String.format("+%s元", NumberFormat.getInstance().format(entity.getDeposit() / 100f)));
        } else {
            helper.setText(R.id.tv_item_consumption_type,"消费");
            helper.setImageResource(R.id.iv_item_consumption, R.drawable.icon_item_consumption);
            helper.setText(R.id.tv_item_consumption_money, String.format("-%s元", NumberFormat.getInstance().format(entity.getBorrow() / 100f)));
        }
    }
}
