package com.ubock.library.ui.activity;

import android.os.Bundle;

import com.ubock.library.base.BaseFragmentActivity;
import com.ubock.library.R;

/**
 * 不能直接启动来退出应用，必须通过MainApplication.exitApp才能正常退出,否则可能会重启而不是退出
 */
public class ExitAppActivity extends BaseFragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        finish();
        System.exit(0);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_exit_app;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
    }
}
