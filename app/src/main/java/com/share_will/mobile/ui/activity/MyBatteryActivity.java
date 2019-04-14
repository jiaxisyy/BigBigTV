package com.share_will.mobile.ui.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
import com.share_will.mobile.presenter.HomeFragmentPresenter;
import com.share_will.mobile.ui.views.IHomeFragmentView;
import com.ubock.library.annotation.PresenterInjector;
import com.ubock.library.base.BaseEntity;
import com.ubock.library.base.BaseFragmentActivity;

public class MyBatteryActivity extends BaseFragmentActivity implements IHomeFragmentView, View.OnClickListener, View.OnTouchListener {
    @PresenterInjector
    HomeFragmentPresenter homeFragmentPresenter;

    private TextView mStartTime;
    private TextView mNowSop;
    private RelativeLayout mCardMoney;
    private LinearLayout mLlCardBatteryInfo;
    private LinearLayout mLlCardBatteryBind;
    private EditText mBindSn;
    private EditText mBindSnAgain;
    private TextView mBindSubmit;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_battery;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setTitle("我的电池");

        mStartTime = findViewById(R.id.tv_home_charge_start_time);
        mNowSop = findViewById(R.id.tv_home_charge_now_sop);
        mCardMoney = findViewById(R.id.rl_card_money);
        mLlCardBatteryInfo = findViewById(R.id.ll_card_battery_info);
        mLlCardBatteryBind = findViewById(R.id.rl_card_bind);

        mBindSn = findViewById(R.id.et_my_battery_bind_sn);
        mBindSnAgain = findViewById(R.id.et_my_battery_bind_sn_again);
        mBindSubmit = findViewById(R.id.tv_my_battery_bind_submit);
        mBindSubmit.setOnClickListener(this);
        homeFragmentPresenter.getBatteryInfo(App.getInstance().getUserId(), App.getInstance().getToken());
    }

    @Override
    public void onLoadBatteryInfoResult(BaseEntity<BatteryEntity> data) {
        BatteryEntity entity = data.getData();
        if (data == null) {
            mNowSop.setText("当前电量:   " + entity.getSop() + "%");
            mStartTime.setText("电池SN:   " + entity.getSn());
            mCardMoney.setVisibility(View.GONE);
        } else {
            mLlCardBatteryInfo.setVisibility(View.GONE);
            mCardMoney.setVisibility(View.GONE);
            mLlCardBatteryBind.setVisibility(View.VISIBLE);
        }
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
    public boolean onTouch(View view, MotionEvent motionEvent) {
        Drawable drawable = mBindSn.getCompoundDrawables()[2];
        if (drawable == null)
            return false;
        if (motionEvent.getAction() != MotionEvent.ACTION_UP)
            return false;
        if (motionEvent.getX() > mBindSn.getWidth()
                - mBindSn.getPaddingRight()
                - drawable.getIntrinsicWidth()) {
            mBindSn.setText("123");
        }
        return false;
    }
}
