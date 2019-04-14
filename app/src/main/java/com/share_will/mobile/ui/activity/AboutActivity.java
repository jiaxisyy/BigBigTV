package com.share_will.mobile.ui.activity;

import android.os.Bundle;

import com.share_will.mobile.R;
import com.ubock.library.base.BaseFragmentActivity;

public class AboutActivity extends BaseFragmentActivity {


    @Override
    protected int getLayoutId() {
        return R.layout.about;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setTitle("关于我们");

    }
}
