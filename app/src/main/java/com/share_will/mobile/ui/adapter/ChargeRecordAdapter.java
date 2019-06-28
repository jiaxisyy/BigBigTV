package com.share_will.mobile.ui.adapter;

import android.content.Context;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.share_will.mobile.R;
import com.share_will.mobile.model.entity.ChargeRecordEntity;
import com.ubock.library.ui.adapter.LoadMoreAdapter;
import com.ubock.library.utils.DateUtils;
import com.ubock.library.utils.NumberUtils;

import java.util.List;

public class ChargeRecordAdapter extends LoadMoreAdapter<ChargeRecordEntity, BaseViewHolder> {


    public ChargeRecordAdapter(Context context, int layoutResId) {
        super(context, layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, ChargeRecordEntity item) {
        if (item.getStartTime() > 0) {
            helper.setText(R.id.tv_item_charge_record_startTime, DateUtils.timeStampToString(item.getStartTime(), DateUtils.YYYYMMDD_HHMMSS));
        }

        if (item.getEndTime() > 0) {
            helper.setText(R.id.tv_item_charge_record_duration, formatTime(item.getEndTime() - item.getStartTime()));
        }
        if (item.getMoney() > 0) {
            helper.setText(R.id.tv_item_charge_record_price, NumberUtils.formatNum(item.getMoney() / 100f) + "元");
        }
    }
    private String formatTime(long time) {
        int h = (int) (time / (60 * 60 * 1000));
        time = (time % (60 * 60 * 1000));
        int m = (int) (time / (60 * 1000));
        return String.format("%d小时%d分钟", h, m);
    }
}
