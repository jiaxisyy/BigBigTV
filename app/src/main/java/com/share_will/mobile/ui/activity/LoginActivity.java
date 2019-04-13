package com.share_will.mobile.ui.activity;


import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.share_will.mobile.App;
import com.share_will.mobile.R;
import com.share_will.mobile.presenter.LoginPresenter;
import com.share_will.mobile.services.BatteryService;
import com.share_will.mobile.services.LocationService;
import com.share_will.mobile.ui.views.LoginView;
import com.share_will.mobile.utils.PressUtils;
import com.ubock.library.base.BaseApp;
import com.ubock.library.base.BaseFragmentActivity;
import com.ubock.library.ui.dialog.ToastExt;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class LoginActivity extends BaseFragmentActivity<LoginPresenter> implements LoginView {

    private Button Btn_login;
    private TextView Btn_goResign;
    private EditText userPhone, userPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LoginActivityPermissionsDispatcher.allowPermissionWithPermissionCheck(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.welcome_activity;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setTitle("登录");
        userPhone = findViewById(R.id.et_phone);
        userPwd = findViewById(R.id.edit_pwd);
        Btn_goResign = findViewById(R.id.btn_goResign);
        Btn_goResign.setOnClickListener((v) -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            LoginActivity.this.startActivity(intent);
        });

        Btn_login = findViewById(R.id.btn_login);
        Btn_login.setOnClickListener((v) -> {
                    String userId = userPhone.getText().toString().trim();
                    String password = userPwd.getText().toString().trim();
                    if (TextUtils.isEmpty(userId)) {
                        ToastExt.showExt("请输入手机号码");
                        return;
                    }
                    if (TextUtils.isEmpty(password)) {
                        ToastExt.showExt("请输入密码");
                        return;
                    }
                    Btn_login.setEnabled(false);
                    getPresenter().login(userId, password);
                }
        );
    }

    @Override
    public void onLogin(boolean success, String message) {
        Btn_login.setEnabled(true);
        ToastExt.showExt(message);
        if (success) {
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            intent.setData(getIntent().getData());
            LoginActivity.this.startActivity(intent);
            startService(new Intent(LoginActivity.this, BatteryService.class));
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Btn_login.setEnabled(true);
    }

    /**
     * 是否登录
     */
    private boolean isLogin() {
        return App.getInstance().isLogin();
    }

    /**
     * 忘记密码功能
     *
     * @param view
     */
    public void forgetPassword(View view) {
        Intent intent = new Intent(this, ForgetPasswordActivityOne.class);
        intent.putExtra("phone", userPhone.getText().toString().trim());
        startActivity(intent);
    }

    @NeedsPermission({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    void allowPermission() {
        Intent intent = new Intent(this, LocationService.class);
        stopService(intent);
        startService(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        LoginActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        BaseApp.getInstance().exitApp(false);
    }

    private PressUtils mPressUtils = new PressUtils(this);

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        mPressUtils.press(event);
        return super.dispatchKeyEvent(event);
    }
}
