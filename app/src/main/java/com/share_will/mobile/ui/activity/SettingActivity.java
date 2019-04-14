package com.share_will.mobile.ui.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.share_will.mobile.App;
import com.share_will.mobile.Constant;
import com.share_will.mobile.R;
import com.ubock.library.base.BaseApp;
import com.ubock.library.base.BaseFragmentActivity;
import com.ubock.library.utils.SharedPreferencesUtils;

public class SettingActivity extends BaseFragmentActivity implements View.OnClickListener {

    private TextView mChangePwd;
    private TextView mUpdate;
    private TextView mAbout;
    private TextView mProtocol;
    private TextView mLogout;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setTitle("设置");
        mChangePwd = findViewById(R.id.tv_setting_changePwd);
        mUpdate = findViewById(R.id.tv_setting_update);
        mAbout = findViewById(R.id.tv_setting_about);
        mProtocol = findViewById(R.id.tv_setting_protocol);
        mLogout = findViewById(R.id.tv_setting_logout);
        mChangePwd.setOnClickListener(this);
        mUpdate.setOnClickListener(this);
        mAbout.setOnClickListener(this);
        mProtocol.setOnClickListener(this);
        mLogout.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_setting_changePwd:
                startActivity(new Intent(this, ForgetPasswordActivityOne.class));
                break;
            case R.id.tv_setting_update:
                checkVersion();
                break;
            case R.id.tv_setting_about:
                startActivity(new Intent(this, AboutActivity.class));
                break;
            case R.id.tv_setting_protocol:
                startActivity(new Intent(this, ProtocolActivity.class));
                break;
            case R.id.tv_setting_logout:
                logout();
                break;
        }

    }

    private void checkVersion() {

    }

    /**
     * 退出对话框
     */
    public void logout() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.alerm_title)
                .setIcon(null)
                .setCancelable(true)
                .setMessage("确认退出当前账号吗?")
                .setPositiveButton(R.string.alert_yes_button,
                        (dialog, which) -> {
                            App.getInstance().getGlobalModel().setUserInfo(null);
                            SharedPreferencesUtils.saveDeviceData(SettingActivity.this, Constant.USER_INFO, null);
                            Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        })
                .setNegativeButton(R.string.alert_no_button,
                        (dialog, which) -> dialog.dismiss()).create().show();
    }
}
