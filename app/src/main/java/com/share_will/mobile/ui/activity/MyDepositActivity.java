package com.share_will.mobile.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.share_will.mobile.R;
import com.share_will.mobile.model.entity.UserInfo;
import com.share_will.mobile.presenter.UserCenterPresenter;
import com.share_will.mobile.ui.views.UserCenterView;
import com.ubock.library.annotation.PresenterInjector;
import com.ubock.library.base.BaseEntity;
import com.ubock.library.base.BaseFragmentActivity;

public class MyDepositActivity extends BaseFragmentActivity implements View.OnClickListener {


    private int mDeposit;
    private TextView mBtnRetreat;
    private TextView mTvMoney;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_deposit;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setTitle("我的押金");

        mBtnRetreat = findViewById(R.id.tv_my_deposit_retreat);
        mTvMoney = findViewById(R.id.tv_my_deposit_money);
        mBtnRetreat.setOnClickListener(this);

        mDeposit = getIntent().getIntExtra("deposit", 0);
        int cause_status = getIntent().getIntExtra("cause_status", 0);
        if (mDeposit > 0) {
            mTvMoney.setText("您已缴纳押金" + mDeposit + "元");
        } else {
            mTvMoney.setText("未缴押金");
            mBtnRetreat.setText("缴纳押金");
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.tv_my_deposit_retreat) {
            if (mDeposit != 0) {
                startActivity(new Intent(this, RefundActivity.class));
            } else {
                //TODO 跳转商城

            }
        }
    }


}
