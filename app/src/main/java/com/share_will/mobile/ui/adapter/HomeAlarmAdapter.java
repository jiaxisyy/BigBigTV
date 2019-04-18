package com.share_will.mobile.ui.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.share_will.mobile.R;
import com.share_will.mobile.model.entity.AlarmEntity;
import com.share_will.mobile.model.entity.BespeakEntity;
import com.ubock.library.utils.DateUtils;

import java.util.List;

public class HomeAlarmAdapter extends BaseQuickAdapter<AlarmEntity.SmokeBean, BaseViewHolder> {

    public HomeAlarmAdapter(int layoutResId, @Nullable List<AlarmEntity.SmokeBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AlarmEntity.SmokeBean item) {
        if (item.getAlarmcode() != 0) {
            helper.setVisible(R.id.item_iv_home_alarm_disposed, false);
            helper.setVisible(R.id.item_tv_home_alarm_close, true);
        }
        if (!TextUtils.isEmpty(item.getTitle())) {
            helper.setText(R.id.item_tv_home_alarm_title, "标题: " + item.getTitle());
        }
        if (!TextUtils.isEmpty(item.getPositionName())) {
            helper.setText(R.id.item_tv_home_alarm_positionName, item.getPositionName());
        }
        if (!TextUtils.isEmpty(item.getRemark())) {
            helper.setText(R.id.item_tv_home_alarm_remark, item.getRemark());
        }
        helper.setText(R.id.item_tv_home_alarm_time, "告警时间   " + DateUtils.timeStampToString(item.getAlarmtime(), DateUtils.YYYYMMDD_HHMMSS));
        helper.setText(R.id.item_tv_home_alarm_level, "告警级别   " + item.getAlarmlevel() + "级");


    }
}
