package com.share_will.mobile.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectChangeListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.share_will.mobile.App;
import com.share_will.mobile.R;
import com.share_will.mobile.model.entity.CityEntity;
import com.share_will.mobile.model.entity.DepositRefundEntity;
import com.share_will.mobile.model.entity.StationEntity;
import com.share_will.mobile.presenter.NewRescuePresenter;
import com.share_will.mobile.presenter.RefundPresenter;
import com.share_will.mobile.ui.views.NewRescueView;
import com.share_will.mobile.ui.views.RefundView;
import com.ubock.library.base.BaseEntity;
import com.ubock.library.base.BaseFragmentActivity;
import com.ubock.library.ui.dialog.ToastExt;

import java.util.List;

public class RefundActivity extends BaseFragmentActivity<RefundPresenter> implements RefundView {

    private EditText mUserName;
    private EditText mBankName;
    private EditText mBankCard;
    private EditText mReason;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("申请退押金");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_refund;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mUserName = findViewById(R.id.et_user_name);
        mBankName = findViewById(R.id.et_bank_name);
        mBankCard = findViewById(R.id.et_bank_card);
        mReason = findViewById(R.id.et_reason);
    }

    public void onSubmit(View view){
        if (TextUtils.isEmpty(mUserName.getText().toString().trim())){
            ToastExt.showExt("请输入用户名");
            return;
        }
        if (TextUtils.isEmpty(mBankName.getText().toString().trim())){
            ToastExt.showExt("请输入银行名称");
            return;
        }
        if (TextUtils.isEmpty(mBankCard.getText().toString().trim())){
            ToastExt.showExt("请输入银行卡号");
            return;
        }
        getPresenter().applyRefund(App.getInstance().getUserId(),
                mUserName.getText().toString().trim(),
                mBankName.getText().toString().trim(),
                mBankCard.getText().toString().trim(),
                mReason.getText().toString().trim());
    }

    @Override
    public void onApplyRefund(BaseEntity<Object> ret) {
        if (ret != null){
            if (ret.getCode() == 0){
                ToastExt.showExt("已成功提交申请");
                finish();
            } else {
                ToastExt.showExt(ret.getMessage());
            }
        }
    }

    @Override
    public void onCancel(BaseEntity<Object> ret) {

    }

    @Override
    public void onLoadRefund(BaseEntity<DepositRefundEntity> ret) {

    }
}
