package com.share_will.mobile.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import com.share_will.mobile.R;
import com.ubock.library.base.BaseFragmentActivity;

public class MyDepositActivity extends BaseFragmentActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_deposit;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setTitle("我的押金");
        findViewById(R.id.tv_my_deposit_retreat).setOnClickListener(v -> {
            startActivity(new Intent(this, RefundActivity.class));
        });
    }
}
