package com.share_will.mobile.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.bigkoo.pickerview.view.OptionsPickerView;
import com.share_will.mobile.R;
import com.share_will.mobile.model.entity.CityEntity;
import com.share_will.mobile.model.entity.StationEntity;
import com.share_will.mobile.presenter.RegisterPresenter;
import com.share_will.mobile.ui.views.RegisterView;
import com.ubock.library.base.BaseEntity;
import com.ubock.library.base.BaseFragmentActivity;
import com.ubock.library.ui.dialog.ToastExt;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class RegisterActivity extends BaseFragmentActivity<RegisterPresenter> implements RegisterView, View.OnClickListener {
    /**短信倒计时时间*/
    private static final int count = 60;
    private Button btnResign;
    private EditText userPhone;
    private EditText userPwd;
    private EditText userPwd2;
    private EditText edit_userName;
    private EditText edit_verifyCode;
    private OptionsPickerView mStationPickerView;
    private TextView mStation;
    private TextView mTvForgetPassword;

    private int mCityIndex;
    private CityEntity mCityEntity;
    private StationEntity mStationEntity;
    private TextView mSelectStation;
    /**
     * 注册用户类型,默认为骑手用户
     */
    private int REGISTERTYPE = 0;

    /**
     * 站点选择
     */
    private static final int REQUEST_CODE_SELECTSTATION = 10010;
    private TextView mTvRider;
    private TextView mTvPersonal;
    private View mIncludeStation;
    private CheckBox mCbAgree;
    private TextView mVerifyCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.register));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        userPhone = findViewById(R.id.et_phone);
        userPwd = findViewById(R.id.edit_pwd);
        userPwd2 = findViewById(R.id.edit_pwd2);
        edit_userName = findViewById(R.id.edit_username);
        edit_verifyCode = findViewById(R.id.edit_verifyCode);
        mStation = findViewById(R.id.tv_station);
        mSelectStation = findViewById(R.id.tv_register_station);
        mTvForgetPassword = findViewById(R.id.tv_forgetPassword);
        mTvPersonal = findViewById(R.id.tv_register_personal);
        mTvRider = findViewById(R.id.tv_register_rider);
        mIncludeStation = findViewById(R.id.include_register_station);
        mCbAgree = findViewById(R.id.cb_register_protocol_agree);
        mVerifyCode = findViewById(R.id.tv_getVerifyCode);
        mVerifyCode.setOnClickListener(this);
        mIncludeStation.setOnClickListener(this);
        mTvPersonal.setOnClickListener(this);
        mTvRider.setOnClickListener(this);
        mTvForgetPassword.setVisibility(View.GONE);
        btnResign = findViewById(R.id.btn_Resign);
        btnResign.setOnClickListener((v) -> {
            final String userid = userPhone.getText().toString().trim();
            final String password = userPwd.getText().toString().trim();
            final String password2 = userPwd2.getText().toString().trim();
            final String username = edit_userName.getText().toString().trim();
            final String verifyCode = edit_verifyCode.getText().toString().trim();
            if (TextUtils.isEmpty(userid)) {
                showError("请输入手机号码");
                return;
            }
            if (userid.length() != 11 || !userid.startsWith("1")) {
                showError("请输入正确的手机号码");
                return;
            }
            if (TextUtils.isEmpty(verifyCode)) {
                showError("请输入验证码");
                return;
            }
            if (TextUtils.isEmpty(password)) {
                showError("请输入密码");
                return;
            }
            if (TextUtils.isEmpty(password2)) {
                showError("请再输入一次密码");
                return;
            }
            if (!password.equals(password2)) {
                showError("两次输入密码不一致");
                return;
            }
            if (TextUtils.isEmpty(username)) {
                showError("请输入您的姓名");
                return;
            }
            String s = mSelectStation.getText().toString();
            if (TextUtils.isEmpty(s) || s.equals("请选择归属")) {
                showError("请选择归属");
                return;
            }
            if (!mCbAgree.isChecked()) {
                showError("请确认已阅读并同意用户协议");
                return;
            }
            btnResign.setEnabled(false);
            getPresenter().register(userid, username, password, verifyCode,
                    mStationEntity.getCustomerCode(),
                    mStationEntity.getStationId() + "");
        });

//        getPresenter().getCityList();
    }

    public void showStationDialog(View view) {
        if (getPresenter().getModel().getCity() == null ||
                getPresenter().getModel().getCity().isEmpty()) {
            getPresenter().getCityList();
        }
        mStationPickerView.show();
    }

    @Override
    public void onRegister(boolean success, String message) {
        btnResign.setEnabled(true);
        showError(message);
        if (success) {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            RegisterActivity.this.startActivity(intent);
            finish();
        }
    }

    @Override
    public void onLoadCityList(BaseEntity<List<CityEntity>> ret) {
        if (ret != null && ret.getCode() == 0 && ret.getData().size() > 0) {
            String cityCode = ret.getData().get(0).getAreaCode();
            getPresenter().getStationList(cityCode, REGISTERTYPE);
        }
    }

    @Override
    public void onLoadStationForPhone(BaseEntity<List<StationEntity>> ret) {

    }

    @Override
    public void onLoadStationList(BaseEntity<List<StationEntity>> ret) {
        if (ret != null && ret.getCode() == 0) {
//            mStationEntity = ret.getData().get(0);
//            mStationPickerView.setNPicker(getPresenter().getModel().getCity(), ret.getData(), null);
//            mStationPickerView.setSelectOptions(mCityIndex);
        }
    }

    @Override
    public void onSendVerifyCode(boolean success, String message) {
        showError(message);
    }

    public void openUserProtocol(View view) {
        startActivity(new Intent(this, ProtocolActivity.class));
    }

    /**
     * 获取验证码
     *
     * @param
     */
    public void getVerifyCode() {
        final String phone = userPhone.getText().toString().trim();
        if (!TextUtils.isEmpty(phone)) {
            if (phone.length() != 11 || !phone.startsWith("1")) {
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
                            mVerifyCode.setText( aLong + "s");
                        }
                    });

            getPresenter().sendVerifyCode(phone,"c-teng");
        } else {
            showError("请输入手机号码");
        }
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
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.tv_register_personal:
                mTvRider.setBackgroundResource(R.drawable.shape_bg_rect_contour_red);
                mTvPersonal.setTextColor(Color.WHITE);
                mTvPersonal.setBackgroundColor(Color.parseColor("#F43838"));
                mTvRider.setTextColor(Color.parseColor("#BDBDBD"));
                REGISTERTYPE = 1;
                break;
            case R.id.tv_register_rider:
                mTvPersonal.setBackgroundResource(R.drawable.shape_bg_rect_contour_red);
                mTvRider.setTextColor(Color.WHITE);
                mTvRider.setBackgroundColor(Color.parseColor("#F43838"));
                mTvPersonal.setTextColor(Color.parseColor("#BDBDBD"));
                REGISTERTYPE = 0;
                break;
            case R.id.include_register_station:
                Intent intent = new Intent(this, SelectStationActivity.class);
                intent.putExtra("register_type", REGISTERTYPE);
                startActivityForResult(intent, REQUEST_CODE_SELECTSTATION);
                break;
            case R.id.tv_getVerifyCode:
                getVerifyCode();
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_SELECTSTATION:
                if (resultCode == RESULT_OK) {
                    StationEntity stationEntity = (StationEntity) data.getSerializableExtra("station_entity");
                    if (stationEntity != null) {
                        mStationEntity = stationEntity;
                        if (!TextUtils.isEmpty(stationEntity.getStationName())) {
                            mSelectStation.setText(stationEntity.getStationName());
                        }
                    }
                }
                break;
        }
    }
}
