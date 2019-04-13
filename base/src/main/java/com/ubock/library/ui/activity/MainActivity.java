package com.ubock.library.ui.activity;

import android.os.Bundle;

import com.ubock.library.base.BaseApp;
import com.ubock.library.base.BaseFragmentActivity;
import com.ubock.library.R;

public class MainActivity extends BaseFragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_base;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }

    @Override
    public void onBackPressed() {
        BaseApp.getInstance().exitApp(false);
    }
}
