package com.share_will.mobile.ui.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.share_will.mobile.App;
import com.share_will.mobile.R;
import com.share_will.mobile.model.entity.BatteryEntity;
import com.share_will.mobile.model.entity.ChargeBatteryEntity;
import com.share_will.mobile.presenter.HomeFragmentPresenter;
import com.share_will.mobile.ui.views.IHomeFragmentView;
import com.ubock.library.annotation.PresenterInjector;
import com.ubock.library.base.BaseConfig;
import com.ubock.library.base.BaseEntity;
import com.ubock.library.base.BaseFragmentActivity;
import com.ubock.library.utils.DateUtils;
import com.ubock.library.utils.LogUtils;

import java.text.DecimalFormat;

public class MyBatteryActivity extends BaseFragmentActivity implements IHomeFragmentView, View.OnClickListener, View.OnTouchListener {
    private static final int REQUEST_CODE_SCAN_SN = 10010;
    @PresenterInjector
    HomeFragmentPresenter homeFragmentPresenter;

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
    private LinearLayout mLlCardBatteryInfo;
    private LinearLayout mLlCardBatteryBind;
    private EditText mBindSn;
    private EditText mBindSnAgain;
    private TextView mBindSubmit;
    private View mLayoutBottom;
    private SwipeRefreshLayout mRefreshLayout;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_battery;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setTitle("我的电池");

        mStartTime = findViewById(R.id.tv_home_charge_start_time);
        mEnoughTime = findViewById(R.id.tv_home_charge_enough_time);
        mDurationTime = findViewById(R.id.tv_home_charge_duration_time);
        mNowSop = findViewById(R.id.tv_home_charge_now_sop);
        mEnergy = findViewById(R.id.tv_home_charge_energy);
        mAddress = findViewById(R.id.tv_home_charge_address);
        mDoor = findViewById(R.id.tv_home_charge_door);

        mMoneyCharge = findViewById(R.id.tv_home_money_charge);
        mMoneManage = findViewById(R.id.tv_home_money_manage);
        mMoneyAll = findViewById(R.id.tv_home_money_all);
        mCardMoney = findViewById(R.id.rl_card_money);
        mLlCardBatteryInfo = findViewById(R.id.ll_card_battery_info);
        mLlCardBatteryBind = findViewById(R.id.rl_card_bind);
        mLayoutBottom = findViewById(R.id.include_layout_my_battery_bottom);
        mBindSn = findViewById(R.id.et_my_battery_bind_sn);
        mBindSnAgain = findViewById(R.id.et_my_battery_bind_sn_again);
        mBindSubmit = findViewById(R.id.tv_my_battery_bind_submit);
        mRefreshLayout = findViewById(R.id.refresh_my_battery);
        mRefreshLayout.setOnRefreshListener(this::initData);
        mBindSubmit.setOnClickListener(this);
        mBindSn.setOnTouchListener(this);
        initData();
    }

    private void initData() {
        homeFragmentPresenter.getChargeBatteryInfo(App.getInstance().getUserId(), App.getInstance().getToken());
    }

    @Override
    public void onLoadBatteryInfoResult(BaseEntity<BatteryEntity> data) {
        BatteryEntity entity = data.getData();
        if (data != null && entity != null && !TextUtils.isEmpty(entity.getSn())) {
            if (!TextUtils.isEmpty(entity.getSn())) {
                mStartTime.setText("电池SN:   " + entity.getSn());
            }
            if (!TextUtils.isEmpty(entity.getSop())) {
                mEnoughTime.setText("当前电量:   " + entity.getSop() + "%");
            }
            mDurationTime.setVisibility(View.GONE);
            mNowSop.setVisibility(View.GONE);
            mEnergy.setVisibility(View.GONE);
            mAddress.setVisibility(View.GONE);
            mDoor.setVisibility(View.GONE);
            mLlCardBatteryInfo.setVisibility(View.VISIBLE);
            mCardMoney.setVisibility(View.GONE);
            mLlCardBatteryBind.setVisibility(View.GONE);
            mLayoutBottom.setVisibility(View.VISIBLE);
        } else {
            mLlCardBatteryInfo.setVisibility(View.GONE);
            mCardMoney.setVisibility(View.GONE);
            mLlCardBatteryBind.setVisibility(View.VISIBLE);
            mLayoutBottom.setVisibility(View.VISIBLE);
        }
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_my_battery_bind_submit:
                bindSubmit();
                break;
        }

    }


    private void bindSubmit() {
        String sn = mBindSn.getText().toString().trim();
        String snAgain = mBindSnAgain.getText().toString().trim();
        if (!sn.equals(snAgain) || TextUtils.isEmpty(sn)) {
            showMessage("SN码错误");
        } else {
            homeFragmentPresenter.bindBattery(App.getInstance().getUserId(), sn);
        }
    }

    @Override
    public void onBindBatteryResult(BaseEntity<Object> data) {
        homeFragmentPresenter.getBatteryInfo(App.getInstance().getUserId(), App.getInstance().getToken());
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        Drawable drawable = mBindSn.getCompoundDrawables()[2];
        if (drawable == null)
            return false;
        if (motionEvent.getAction() != MotionEvent.ACTION_UP)
            return false;
        if (motionEvent.getX() > mBindSn.getWidth()
                - mBindSn.getPaddingRight()
                - drawable.getIntrinsicWidth()) {
            scanSn();
        }
        return false;
    }

    private void scanSn() {
        Intent intent = new Intent(this, CaptureActivity.class);
        startActivityForResult(intent, REQUEST_CODE_SCAN_SN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SCAN_SN) {
            //处理扫码换电
            if (resultCode == RESULT_OK) {
                String result = data.getStringExtra("scan_result");
                LogUtils.d(result + "=============");
                mBindSn.setText(result);
                mBindSnAgain.setText(result);
            }
        }

    }

    @Override
    public void onLoadChargeBatteryInfoResult(BaseEntity<ChargeBatteryEntity> data) {
        if (data != null) {
            mLayoutBottom.setVisibility(View.VISIBLE);
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

            mMoneyCharge.setText(intChange(entity.getMoney() / 100f) + "元");
            mMoneManage.setText(intChange(entity.getManageMoney() / 100f) + "元");
            int all = entity.getMoney() + entity.getManageMoney();
            mMoneyAll.setText("合计:" + intChange(all / 100f) + "元");
        } else {
            //没有充电电池,展示已有电池信息
            homeFragmentPresenter.getBatteryInfo(App.getInstance().getUserId(), App.getInstance().getToken());
        }
    }

    public String intChange(float num) {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        return decimalFormat.format(num);
    }
}
