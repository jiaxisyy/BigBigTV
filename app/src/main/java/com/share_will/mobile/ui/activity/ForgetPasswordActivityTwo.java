package com.share_will.mobile.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.share_will.mobile.R;
import com.share_will.mobile.presenter.ForgetPasswordPresenter;
import com.share_will.mobile.ui.views.ForgetPasswordView;
import com.ubock.library.base.BaseFragmentActivity;
import com.ubock.library.ui.dialog.ToastExt;

public class ForgetPasswordActivityTwo extends BaseFragmentActivity<ForgetPasswordPresenter> implements ForgetPasswordView {
    private EditText mPassword;
    private EditText mPassword2;
    private TextView mTvForgetPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_forget_password_two;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setTitle("找回密码");
        mPassword = findViewById(R.id.edit_pwd);
        mPassword2 = findViewById(R.id.edit_pwd2);
        mTvForgetPwd = findViewById(R.id.tv_forgetPassword);
        mTvForgetPwd.setVisibility(View.GONE);
    }

    @Override
    public void onUpdatePassword(boolean success, String message) {
        showError(message);
        if (success) {
            finish();
        }
    }

    @Override
    public void onSendVerifyCode(boolean success, String message) {
        showError(message);
    }

    /**
     * 提交验证码、确认修改密码
     *
     * @param view
     */
    public void submit(View view) {
        String phone = getIntent().getStringExtra("phone");
        String verifyCode = getIntent().getStringExtra("verifyCode");
        String password = mPassword.getText().toString().trim();
        String password2 = mPassword2.getText().toString().trim();

        if (TextUtils.isEmpty(password)) {
            showError("请输入新密码");
            return;
        }
        if (!password.equals(password2)) {
            showError("两次输入的密码不一致");
            return;
        }
        getPresenter().updatePassword(phone, password, verifyCode);
    }

    /**
     * 显示错误提示
     *
     * @param message
     */
    private void showError(String message) {
        ToastExt.showExt(message);
    }
}
