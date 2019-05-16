package com.share_will.mobile.ui.adapter;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.share_will.mobile.App;
import com.share_will.mobile.R;
import com.share_will.mobile.model.entity.AlarmEntity;
import com.share_will.mobile.model.entity.AlarmMultiEntity;
import com.ubock.library.utils.DateUtils;

import java.util.List;

public class MultiAdapter extends BaseMultiItemQuickAdapter<AlarmMultiEntity, BaseViewHolder> {

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public MultiAdapter(List<AlarmMultiEntity> data) {
        super(data);
        addItemType(AlarmMultiEntity.TYPE_SMOKE, R.layout.item_home_alarm);
        addItemType(AlarmMultiEntity.TYPE_RFID, R.layout.item_home_alarm);
    }


    @Override
    protected void convert(BaseViewHolder helper, AlarmMultiEntity item) {

        int itemViewType = helper.getItemViewType();
        switch (itemViewType) {

            case AlarmMultiEntity.TYPE_SMOKE:
                AlarmEntity.SmokeBean smokeBean = item.getSmokeBean();
                if (smokeBean.getConfirmstate() != 1) {
                    helper.setVisible(R.id.item_iv_home_alarm_disposed, false);
                    helper.setVisible(R.id.item_tv_home_alarm_close, true);
                }

                if (!TextUtils.isEmpty(smokeBean.getTitle())) {
                    helper.setText(R.id.item_tv_home_alarm_title, "标题: " + smokeBean.getTitle());
                }
                if (!TextUtils.isEmpty(smokeBean.getPositionName())) {
                    helper.setText(R.id.item_tv_home_alarm_positionName, smokeBean.getPositionName());
                }
                if (!TextUtils.isEmpty(smokeBean.getRemark())) {
                    helper.setText(R.id.item_tv_home_alarm_remark, smokeBean.getRemark());
                }
                if (smokeBean.getAlarmtime() != 0) {
                    helper.setText(R.id.item_tv_home_alarm_time, "告警时间   " + DateUtils.timeStampToString(smokeBean.getAlarmtime(), DateUtils.YYYYMMDD_HHMMSS));
                }
                helper.setText(R.id.item_tv_home_alarm_level, "告警级别   " + smokeBean.getAlarmlevel() + "级");

                helper.addOnClickListener(R.id.item_tv_home_alarm_close);
                break;
            case AlarmMultiEntity.TYPE_RFID:

                AlarmEntity.RfidBean rfidBean = item.getRfidBean();
                helper.setVisible(R.id.item_iv_home_alarm_disposed, false);
                helper.setVisible(R.id.item_tv_home_alarm_close, false);

                helper.getView(R.id.item_tv_home_alarm_level).setVisibility(View.GONE);
                helper.getView(R.id.item_tv_home_alarm_time).setVisibility(View.GONE);
                helper.getView(R.id.item_tv_home_alarm_remark).setVisibility(View.GONE);

                helper.setText(R.id.item_tv_home_alarm_title, "标题: 违规、违禁品告警");
                helper.setTextColor(R.id.item_tv_home_alarm_title, Color.parseColor("#FF6C00"));

                if (!TextUtils.isEmpty(rfidBean.getCommunityName())) {
                    helper.setText(R.id.item_tv_home_alarm_positionName, rfidBean.getCommunityName());
                }
                if (!TextUtils.isEmpty(rfidBean.getAddress())) {
                    helper.setText(R.id.item_tv_home_alarm_rfid, App.getInstance().getUserId() + ",您于"
                            + DateUtils.timeStampToString(rfidBean.getCollecttime(), DateUtils.YYYYMMDD_HHMMSS) + "在"
                            + rfidBean.getAddress() + "被检测发现违规携带电池进入");
                }
                break;

        }

    }
}
