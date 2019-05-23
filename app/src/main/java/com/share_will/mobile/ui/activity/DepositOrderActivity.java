package com.share_will.mobile.ui.activity;

import android.os.Bundle;

import com.share_will.mobile.R;
import com.ubock.library.base.BaseFragmentActivity;

public class DepositOrderActivity extends BaseFragmentActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_deposit_order;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setTitle("申请退履约保证金");

    }
}
