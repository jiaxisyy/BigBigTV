package com.share_will.mobile.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.share_will.mobile.App;
import com.share_will.mobile.R;
import com.share_will.mobile.model.entity.AlarmEntity;
import com.share_will.mobile.model.entity.BatteryEntity;
import com.share_will.mobile.model.entity.ChargeBatteryEntity;
import com.share_will.mobile.presenter.HomeFragmentPresenter;
import com.share_will.mobile.ui.activity.AlarmListActivity;
import com.share_will.mobile.ui.activity.HomeServiceActivity;
import com.share_will.mobile.ui.activity.MyBatteryActivity;
import com.share_will.mobile.ui.views.IHomeFragmentView;
import com.ubock.library.base.BaseEntity;
import com.ubock.library.base.BaseFragment;
import com.ubock.library.utils.DateUtils;

import java.text.DecimalFormat;
import java.util.List;

public class HomeFragment extends BaseFragment<HomeFragmentPresenter> implements IHomeFragmentView, View.OnClickListener {
    private TextView mAlarmTitle;
    private TextView mAlarmPositionName;
    private TextView mAlarmRemark;
    private TextView mAlarmTime;
    private TextView mAlarmLevel;
    private TextView mTopCharge;
    private RelativeLayout mRlAlarm;
    private RelativeLayout mRlBattery;
    private TextView mStartTime;
    private TextView mEnoughTime;
    private TextView mDurationTime;
    private TextView mNowSop;
    private TextView mEnergy;
    private TextView mAddress;
    private TextView mDoor;
    private TextView mMoneyCharge;
    private TextView mMoneManage;
    private TextView mMoneyAll;
    private RelativeLayout mCardMoney;
    private TextView mNoAlarm;
    private TextView mNoBattery;
    private View mLayoutBottom;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        showBackMenu(false);
        setTitleTextColor(Color.parseColor("#000000"));
        setTitle("智慧社区");
        View vie = view.findViewById(R.id.topbar);
        vie.setBackgroundColor(Color.parseColor("#FFFFFF"));
        mAlarmTitle = view.findViewById(R.id.tv_home_alarm_title);
        mAlarmPositionName = view.findViewById(R.id.tv_home_alarm_positionName);
        mAlarmRemark = view.findViewById(R.id.tv_home_alarm_remark);
        mAlarmTime = view.findViewById(R.id.tv_home_alarm_time);
        mAlarmLevel = view.findViewById(R.id.tv_home_alarm_level);
        mTopCharge = view.findViewById(R.id.tv_home_top_charge);
        mRlAlarm = view.findViewById(R.id.rl_home_alarmInfo);
        mRlBattery = view.findViewById(R.id.rl_home_batteryInfo);
        mNoAlarm = view.findViewById(R.id.tv_home_no_alarm);
        mNoBattery = view.findViewById(R.id.tv_home_no_battery);
        mLayoutBottom = view.findViewById(R.id.include_layout_home_bottom);


        mStartTime = view.findViewById(R.id.tv_home_charge_start_time);
        mEnoughTime = view.findViewById(R.id.tv_home_charge_enough_time);
        mDurationTime = view.findViewById(R.id.tv_home_charge_duration_time);
        mNowSop = view.findViewById(R.id.tv_home_charge_now_sop);
        mEnergy = view.findViewById(R.id.tv_home_charge_energy);
        mAddress = view.findViewById(R.id.tv_home_charge_address);
        mDoor = view.findViewById(R.id.tv_home_charge_door);
        mMoneyCharge = view.findViewById(R.id.tv_home_money_charge);
        mMoneManage = view.findViewById(R.id.tv_home_money_manage);
        mMoneyAll = view.findViewById(R.id.tv_home_money_all);
        mCardMoney = view.findViewById(R.id.rl_card_money);

        mTopCharge.setOnClickListener(this);
        mRlAlarm.setOnClickListener(this);
        mRlBattery.setOnClickListener(this);
        initData();
    }

    private void initData() {
        getPresenter().getAlarmList(App.getInstance().getUserId(), App.getInstance().getToken());
        getPresenter().getChargeBatteryInfo(App.getInstance().getUserId(), App.getInstance().getToken());
    }

    @Override
    public void onLoadAlarmResult(BaseEntity<AlarmEntity> data) {
        int validPos = -1;
        if (data != null) {
            List<AlarmEntity.SmokeBean> smokeBeanList = data.getData().getSmoke();
            int size = smokeBeanList.size();
            for (int i = 0; i < size; i++) {
                if (smokeBeanList.get(i).getAlarmcode() != 0) {
                    validPos = i;
                }
            }
            if (validPos != -1) {
                AlarmEntity.SmokeBean alarmEntity = data.getData().getSmoke().get(validPos);
                mAlarmTitle.setVisibility(View.VISIBLE);
                mAlarmPositionName.setVisibility(View.VISIBLE);
                mAlarmRemark.setVisibility(View.VISIBLE);
                mAlarmTime.setVisibility(View.VISIBLE);
                mAlarmLevel.setVisibility(View.VISIBLE);
                mNoAlarm.setVisibility(View.INVISIBLE);
                mAlarmTitle.setText("标题: " + alarmEntity.getTitle());
                mAlarmPositionName.setText(alarmEntity.getPositionName());
                mAlarmRemark.setText(alarmEntity.getRemark());
                mAlarmTime.setText("告警时间   " + DateUtils.timeStampToString(alarmEntity.getAlarmtime(), DateUtils.YYYYMMDD_HHMMSS));
                mAlarmLevel.setText("告警级别   " + alarmEntity.getAlarmlevel() + "级");
            } else {
                mNoAlarm.setVisibility(View.VISIBLE);
                mAlarmTitle.setVisibility(View.INVISIBLE);
                mAlarmPositionName.setVisibility(View.INVISIBLE);
                mAlarmRemark.setVisibility(View.INVISIBLE);
                mAlarmTime.setVisibility(View.INVISIBLE);
                mAlarmLevel.setVisibility(View.INVISIBLE);
            }
        }
    }


    @Override
    public void onLoadChargeBatteryInfoResult(BaseEntity<ChargeBatteryEntity> data) {
        if (data != null) {
            ChargeBatteryEntity entity = data.getData();
            mStartTime.setText("开始时间:   " + DateUtils.unixToLocalTime(String.valueOf(entity.getStartTime())));
            if (entity.getFullTime() != 0) {
                mEnoughTime.setText("充满时间:   " + DateUtils.timeStampToString(entity.getFullTime(), DateUtils.YYYYMMDD_HHMMSS));
                long l = entity.getFullTime() - entity.getStartTime();
                mDurationTime.setText("充电时长:   " + DateUtils.unixToUTcTimeTest(l));
            } else {
                mEnoughTime.setVisibility(View.GONE);
                long l = entity.getNowTime() - entity.getStartTime();
                mDurationTime.setText("充电时长:   " + DateUtils.unixToUTcTimeTest(l));
            }
            mNowSop.setText("当前电量:   " + entity.getSop() + "%");
            mEnergy.setText("已充能量点:   " + entity.getEnergy());
            mAddress.setText("电池位置:   " + entity.getCabinetAddress());
            mDoor.setText("仓门号:   " + entity.getDoor());

            mMoneyCharge.setText(intChange(entity.getMoney()) + "元");
            mMoneManage.setText(intChange(entity.getManageMoney()) + "元");
            int all = entity.getMoney() + entity.getManageMoney();
            mMoneyAll.setText("合计:" + intChange(all) + "元");
        } else {
            //没有充电电池,展示已有电池信息
            getPresenter().getBatteryInfo(App.getInstance().getUserId(), App.getInstance().getToken());
        }

    }

    public String intChange(int num) {
        DecimalFormat df = new DecimalFormat("#.00");
        return df.format(num);
    }

    @Override
    public void onLoadBatteryInfoResult(BaseEntity<BatteryEntity> data) {
        BatteryEntity entity = data.getData();
        if (data != null) {
            mStartTime.setText("电池SN:   " + entity.getSn());
            mEnoughTime.setText("当前电量:   " + entity.getSop() + "%");
            mStartTime.setVisibility(View.VISIBLE);
            mEnoughTime.setVisibility(View.VISIBLE);
            mDurationTime.setVisibility(View.GONE);
            mNowSop.setVisibility(View.GONE);
            mEnergy.setVisibility(View.GONE);
            mAddress.setVisibility(View.GONE);
            mDoor.setVisibility(View.GONE);
            mCardMoney.setVisibility(View.GONE);
            mNoBattery.setVisibility(View.GONE);
            mLayoutBottom.setVisibility(View.VISIBLE);
        } else {
            mLayoutBottom.setVisibility(View.GONE);
            mNoBattery.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_home_top_charge:
                startActivity(new Intent(getActivity(), HomeServiceActivity.class));
                break;
            case R.id.rl_home_alarmInfo:
                startActivity(new Intent(getActivity(), AlarmListActivity.class));
                break;
            case R.id.rl_home_batteryInfo:
                startActivity(new Intent(getActivity(), MyBatteryActivity.class));
                break;

        }
    }
}
