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

    public RecordAdapter(Context context){
        super(context, R.layout.record_item);
    }

    @Override
    protected void convert(BaseViewHolder helper, RecordEntity entity) {
        helper.setText(R.id.time, entity.getDatetime());
        helper.setText(R.id.recharge, String.format("%s元", NumberFormat.getInstance().format(entity.getDeposit()/100f)));
        helper.setText(R.id.consume, String.format("%s元", NumberFormat.getInstance().format(entity.getBorrow()/100f)));
    }
}
