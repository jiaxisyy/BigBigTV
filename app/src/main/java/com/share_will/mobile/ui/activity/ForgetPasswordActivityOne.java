package com.share_will.mobile.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.share_will.mobile.App;
import com.share_will.mobile.R;
import com.share_will.mobile.presenter.ForgetPasswordPresenter;
import com.share_will.mobile.ui.views.ForgetPasswordView;
import com.ubock.library.base.BaseFragmentActivity;
import com.ubock.library.ui.dialog.ToastExt;
import com.ubock.library.utils.LogUtils;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class ForgetPasswordActivityOne extends BaseFragmentActivity<ForgetPasswordPresenter> implements ForgetPasswordView, View.OnClickListener {
    private static final int REQUEST_CODE_FORGETPWD = 10010;
    private String mPhone;
    private EditText mPhoneEt;
    private EditText mVerifyCodeEt;
    private TextView btnForgetPasswordNext;
    private EditText mPassword;
    private EditText mPassword2;
    private TextView mTvForgetPwd;
    private TextView mVerifyCode;
    /**短信倒计时时间*/
    private static final int count = 60;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("找回密码");
        mPhoneEt = findViewById(R.id.et_phone);
        mVerifyCodeEt = findViewById(R.id.edit_verifyCode);
        btnForgetPasswordNext = findViewById(R.id.btn_forgetPassword_next);
        mPassword = findViewById(R.id.edit_pwd);
        mPassword2 = findViewById(R.id.edit_pwd2);
        mTvForgetPwd = findViewById(R.id.tv_forgetPassword);
        mTvForgetPwd.setVisibility(View.GONE);
        mVerifyCode = findViewById(R.id.tv_getVerifyCode);
        mVerifyCode.setOnClickListener(this);
        String phone = getIntent().getStringExtra("phone");
        if (!TextUtils.isEmpty(phone)) {
            mPhoneEt.setText(phone);
        }
        String userId = App.getInstance().getUserId();
        if (!TextUtils.isEmpty(userId)) {
            mPhoneEt.setText(userId);
        }
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
        if (success) {
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_FORGETPWD) {
            //处理扫码换电
            if (resultCode == RESULT_OK) {
                String result = data.getStringExtra("result");
                if (result.equals("change_pwd_ok")) {
                    finish();
                }
            }
        }

    }

    @Override
    public void onSendVerifyCode(boolean success, String message) {
        showError(message);
    }

    /**
     * 获取验证码
     *
     * @param
     */
    public void getVerifyCode() {
        mPhone = mPhoneEt.getText().toString().trim();
        if (!TextUtils.isEmpty(mPhone)) {
            if (mPhone.length() != 11 || !mPhone.startsWith("1")) {
                showError("请输入正确的手机号码");
                return;
            }
            Observable.interval(0, 1, TimeUnit.SECONDS)//设置0延迟，每隔一秒发送一条数据
                    .take(count + 1)
                    .map(new Func1<Long, Long>() {
                        @Override
                        public Long call(Long aLong) {
                            return count - aLong; //
                        }
                    })
                    .doOnSubscribe(new Action0() {
                        @Override
                        public void call() {
                            mVerifyCode.setEnabled(false);//在发送数据的时候设置为不能点击
                        }
                    })

                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Observer<Long>() {
                        @Override
                        public void onCompleted() {
                            mVerifyCode.setText("获取验证码");//数据发送完后设置为原来的文字
                            mVerifyCode.setEnabled(true);
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onNext(Long aLong) { //接受到一条就是会操作一次UI
                            mVerifyCode.setText(aLong + "s");
                        }
                    });

            getPresenter().sendVerifyCode(mPhone);

        } else {
            showError("请输入手机号码");
        }
    }

    /**
     * 提交验证码、确认修改密码
     *
     * @param view
     */
    public void submit(View view) {
        String password = mPassword.getText().toString().trim();
        String password2 = mPassword2.getText().toString().trim();
        String verifyCode = mVerifyCodeEt.getText().toString().trim();
        if (TextUtils.isEmpty(mPhoneEt.getText().toString().trim())) {
            showError("请输入手机号码");
            return;
        }
        if (TextUtils.isEmpty(verifyCode)) {
            showError("请输入验证码");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            showError("请输入新密码");
            return;
        }
        if (!password.equals(password2)) {
            showError("两次输入的密码不一致");
            return;
        }

        getPresenter().updatePassword(mPhone, password, verifyCode);

    }

    /**
     * 显示错误提示
     *
     * @param message
     */
    private void showError(String message) {
        ToastExt.showExt(message);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.tv_getVerifyCode:
                getVerifyCode();
                break;

        }

    }
}
