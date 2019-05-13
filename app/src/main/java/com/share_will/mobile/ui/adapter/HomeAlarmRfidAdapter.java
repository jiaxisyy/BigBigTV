package com.share_will.mobile.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.share_will.mobile.App;
import com.share_will.mobile.R;
import com.share_will.mobile.model.entity.AlarmEntity;
import com.ubock.library.ui.adapter.LoadMoreAdapter;
import com.ubock.library.utils.DateUtils;

import java.util.List;

public class HomeAlarmRfidAdapter extends LoadMoreAdapter<AlarmEntity.RfidBean, BaseViewHolder> {


    public HomeAlarmRfidAdapter(Context context, int layoutResId) {
        super(context, layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, AlarmEntity.RfidBean item) {

        helper.setVisible(R.id.item_iv_home_alarm_disposed, false);
        helper.setVisible(R.id.item_tv_home_alarm_close, false);

        helper.getView(R.id.item_tv_home_alarm_level).setVisibility(View.GONE);
        helper.getView(R.id.item_tv_home_alarm_time).setVisibility(View.GONE);
        helper.getView(R.id.item_tv_home_alarm_remark).setVisibility(View.GONE);

        helper.setText(R.id.item_tv_home_alarm_title, "标题: 违规、违禁品告警");
        helper.setTextColor(R.id.item_tv_home_alarm_title, Color.parseColor("#EEDE81"));

        if(!TextUtils.isEmpty(item.getCommunityName())){
            helper.setText(R.id.item_tv_home_alarm_positionName, item.getCommunityName());
        }
        if(!TextUtils.isEmpty(item.getAddress())){
            helper.setText(R.id.item_tv_home_alarm_rfid, App.getInstance().getUserId() + ",您于"
                    + DateUtils.timeStampToString(item.getCollecttime(), DateUtils.YYYYMMDD_HHMMSS) + "在"
                    + item.getAddress() + "被检测发现违规携带电池进入");
        }
    }

}
