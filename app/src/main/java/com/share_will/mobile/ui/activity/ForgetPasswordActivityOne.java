package com.share_will.mobile.ui.activity;

import android.content.Intent;
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

public class ForgetPasswordActivityOne extends BaseFragmentActivity<ForgetPasswordPresenter> implements ForgetPasswordView {

    private String mPhone;
    private EditText mPhoneEt;
    private EditText mVerifyCodeEt;
    private TextView btnForgetPasswordNext;
    private EditText mPassword;
    private EditText mPassword2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("找回密码");
        mPhoneEt = findViewById(R.id.et_phone);
        mVerifyCodeEt = findViewById(R.id.edit_verifyCode);
        btnForgetPasswordNext = findViewById(R.id.btn_forgetPassword_next);
//        mPassword = findViewById(R.id.edit_pwd);
//        mPassword2 = findViewById(R.id.edit_pwd2);

        String phone = getIntent().getStringExtra("phone");
        if (!TextUtils.isEmpty(phone)){
            mPhoneEt.setText(phone);
        }
        btnForgetPasswordNext.setOnClickListener(v -> {
            String verifyCode = mVerifyCodeEt.getText().toString().trim();
            if (TextUtils.isEmpty(mPhoneEt.getText().toString().trim())) {
                showError("请输入手机号码");
                return;
            }
            if (TextUtils.isEmpty(verifyCode)) {
                showError("请输入验证码");
                return;
            }
            Intent intent = new Intent(this, ForgetPasswordActivityTwo.class);
            intent.putExtra("phone",mPhone);
            intent.putExtra("verifyCode",verifyCode);
            startActivity(intent);
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_forget_password_one;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }

    @Override
    public void onUpdatePassword(boolean success, String message) {
        showError(message);
        if (success){
            finish();
        }
    }

    @Override
    public void onSendVerifyCode(boolean success, String message) {
        showError(message);
    }

    /**
     * 获取验证码
     * @param view
     */
    public void getVerifyCode(View view){
        mPhone = mPhoneEt.getText().toString().trim();
        if (!TextUtils.isEmpty(mPhone)) {
            if (mPhone.length() != 11 || !mPhone.startsWith("1")){
                showError("请输入正确的手机号码");
                return;
            }

            getPresenter().sendVerifyCode(mPhone);

        } else {
            showError("请输入手机号码");
        }
    }

    /**
     * 提交验证码、确认修改密码
     * @param view
     */
    public void submit(View view){
        String password = mPassword.getText().toString().trim();
        String password2 = mPassword2.getText().toString().trim();
        String verifyCode = mVerifyCodeEt.getText().toString().trim();
        if (TextUtils.isEmpty(mPhoneEt.getText().toString().trim())){
            showError("请输入手机号码");
            return;
        }
        if (TextUtils.isEmpty(verifyCode)){
            showError("请输入验证码");
            return;
        }
        if (TextUtils.isEmpty(password)){
            showError("请输入新密码");
            return;
        }
        if (!password.equals(password2)){
            showError("两次输入的密码不一致");
            return;
        }

        getPresenter().updatePassword(mPhone, password, verifyCode);

    }

    /**
     * 显示错误提示
     * @param message
     */
    private void showError(String message){
        ToastExt.showExt(message);
    }
}
