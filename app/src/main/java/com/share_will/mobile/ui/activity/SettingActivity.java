package com.share_will.mobile.ui.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.share_will.mobile.App;
import com.share_will.mobile.Constant;
import com.share_will.mobile.R;
import com.share_will.mobile.presenter.UpgradeServicePresenter;
import com.share_will.mobile.ui.dialog.DialogActivity;
import com.share_will.mobile.ui.views.UpgradeServiceView;
import com.share_will.mobile.utils.Utils;
import com.ubock.library.annotation.PresenterInjector;
import com.ubock.library.base.BaseApp;
import com.ubock.library.base.BaseFragmentActivity;
import com.ubock.library.ui.dialog.ToastExt;
import com.ubock.library.utils.AppUtils;
import com.ubock.library.utils.SharedPreferencesUtils;

import java.util.Map;

public class SettingActivity extends BaseFragmentActivity implements View.OnClickListener, UpgradeServiceView {

    @PresenterInjector
    UpgradeServicePresenter upgradeServicePresenter;

    private TextView mAutonym;
    private TextView mChangePwd;
    private RelativeLayout mUpdate;
    private TextView mAbout;
    private TextView mProtocol;
    private TextView mLogout;
    private TextView mClean;
    private TextView mVersion;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setTitle("设置");
        mAutonym = findViewById(R.id.tv_setting_autonym);
        mChangePwd = findViewById(R.id.tv_setting_changePwd);
        mUpdate = findViewById(R.id.rl_setting_update);
        mAbout = findViewById(R.id.tv_setting_about);
        mProtocol = findViewById(R.id.tv_setting_protocol);
        mLogout = findViewById(R.id.tv_setting_logout);
        mClean = findViewById(R.id.tv_setting_clear);
        mVersion = findViewById(R.id.tv_setting_version);
        mAutonym.setOnClickListener(this);
        mChangePwd.setOnClickListener(this);
        mUpdate.setOnClickListener(this);
        mAbout.setOnClickListener(this);
        mProtocol.setOnClickListener(this);
        mLogout.setOnClickListener(this);
        mClean.setOnClickListener(this);
        mVersion.setText("V" + AppUtils.getVersionName(this));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_setting_autonym:
                startActivity(new Intent(this, AutonymActivity.class));
                break;
            case R.id.tv_setting_changePwd:
                startActivity(new Intent(this, ForgetPasswordActivityOne.class));
                break;
            case R.id.rl_setting_update:
                checkVersion();
                break;
            case R.id.tv_setting_clear:
                ToastExt.showExt("清除缓存成功");
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
        String versionName = Utils.getVersionName(this);
        int versionCode = Utils.getVersionCode(this);
        String channel = App.getChannel();
        String userId = App.getInstance().getUserId();
        upgradeServicePresenter.checkVersion(versionName, versionCode, 1, channel, userId, true);
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

    @Override
    public void onLoadVersion(Map<String, String> data) {
        if (data != null) {
            String url = data.get("downloadUrl");
            String description = data.get("description");
            if (!TextUtils.isEmpty(url)) {
                Log.d("cgd", url);
                showDialog(description == null ? "" : description, true);
                return;
            } else {
                showDialog("已是最新版本", false);
            }

        } else {
            showDialog("已是最新版本", false);
        }
    }

    private void showDialog(String desc, boolean newVersion) {
        Intent intent = new Intent(this, DialogActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(DialogActivity.PARAM_TITLE, "检测更新");
        intent.putExtra(DialogActivity.PARAM_MESSAGE, desc);
        intent.putExtra(DialogActivity.PARAM_NEED_PERMISSION, true);
        if (newVersion) {
            intent.putExtra(DialogActivity.PARAM_OK, "升级");
            intent.putExtra(DialogActivity.PARAM_CANCEL, "取消");
            startActivity(intent);
        } else {
//            intent.putExtra(DialogActivity.PARAM_SHOW_OK, false);
//            intent.putExtra(DialogActivity.PARAM_CANCEL, "确定");
            ToastExt.showExt("已是最新版本");
        }
    }
}
