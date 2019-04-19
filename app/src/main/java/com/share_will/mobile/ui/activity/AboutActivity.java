package com.share_will.mobile.ui.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.share_will.mobile.App;
import com.share_will.mobile.R;
import com.ubock.library.base.BaseFragmentActivity;
import com.ubock.library.utils.AppUtils;

public class AboutActivity extends BaseFragmentActivity {


    @Override
    protected int getLayoutId() {
        return R.layout.about;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setTitle("关于我们");
        TextView mTvVersion = findViewById(R.id.tv_about_version);

        mTvVersion.setText(String.format("智慧社区 V%s", AppUtils.getVersionName(this)));

    }
}
