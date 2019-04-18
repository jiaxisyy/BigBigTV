package com.share_will.mobile.ui.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.share_will.mobile.App;
import com.share_will.mobile.Constant;
import com.share_will.mobile.R;
import com.share_will.mobile.model.entity.DepositRefundEntity;
import com.share_will.mobile.model.entity.RescueEntity;
import com.share_will.mobile.presenter.RefundPresenter;
import com.share_will.mobile.ui.views.RefundView;
import com.ubock.library.base.BaseEntity;
import com.ubock.library.base.BaseFragmentActivity;
import com.ubock.library.ui.dialog.ToastExt;

import java.text.SimpleDateFormat;
import java.util.List;

public class RefundDetailActivity extends BaseFragmentActivity<RefundPresenter> implements RefundView {

    private DepositRefundEntity mRefundEntity;
    private TextView mStatus;
    private TextView mUserName;
    private TextView mBankName;
    private TextView mBankCard;
    private TextView mReason;
    private TextView mComment;
    private TextView mDate;
    private Button mCancelBtn;
    private Button mApplyRefundBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("查看退押金进度");
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        getPresenter().getRefundDetail(App.getInstance().getUserId());
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getPresenter().getRefundDetail(App.getInstance().getUserId());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_refund_detail;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mStatus = findViewById(R.id.tv_status);
        mUserName = findViewById(R.id.tv_userName);
        mBankName = findViewById(R.id.tv_bankName);
        mBankCard = findViewById(R.id.tv_bankCard);
        mReason = findViewById(R.id.tv_reason);
        mComment = findViewById(R.id.tv_comment);
        mDate = findViewById(R.id.tv_date);
        mCancelBtn = findViewById(R.id.btn_cancel);
        mApplyRefundBtn = findViewById(R.id.btn_applyRefund);
    }

    private void renderView(){
        if (mRefundEntity == null){
            return;
        }
        mStatus.setText(Constant.RefundStatus.get(mRefundEntity.getCauseStatus()));
        if(!TextUtils.isEmpty(mRefundEntity.getBankUserName())){
            mUserName.setText(mRefundEntity.getBankUserName());
        }
        if(!TextUtils.isEmpty(mRefundEntity.getBankName())){
            mBankName.setText(mRefundEntity.getBankName());
        }
        if(!TextUtils.isEmpty(mRefundEntity.getBankCard())){
            mBankCard.setText(mRefundEntity.getBankCard());
        }
        if(!TextUtils.isEmpty(mRefundEntity.getDescription())){
            mReason.setText(mRefundEntity.getDescription());
        }
        if(!TextUtils.isEmpty(mRefundEntity.getResolve())){
            mComment.setText(mRefundEntity.getResolve());
        }
        SimpleDateFormat format = new SimpleDateFormat("YYYY.MM.dd");
        mDate.setText(format.format(mRefundEntity.getCreateTime()));
        if (mRefundEntity.getCauseStatus() == 0){
            mCancelBtn.setVisibility(View.VISIBLE);
            mApplyRefundBtn.setVisibility(View.GONE);
        } else if (mRefundEntity.getCauseStatus() == 2){
            mApplyRefundBtn.setVisibility(View.VISIBLE);
            mCancelBtn.setVisibility(View.GONE);
        } else {
            mCancelBtn.setVisibility(View.GONE);
            mApplyRefundBtn.setVisibility(View.GONE);
        }
    }

    @Override
    public void onApplyRefund(BaseEntity<Object> ret) {

    }

    @Override
    public void onCancel(BaseEntity<Object> ret) {
        if (ret.getCode() == 0){
            ToastExt.showExt("已取消申请");
            finish();
        } else {
            ToastExt.showExt(ret.getMessage());
        }
    }

    @Override
    public void onLoadRefund(BaseEntity<DepositRefundEntity> ret) {
        if (ret != null && ret.getCode() == 0){
            mRefundEntity = ret.getData();
            renderView();
        }
    }

    public void applyRefund(View view){
        Intent intent = new Intent(this, RefundActivity.class);
        startActivity(intent);
    }

    public void onCancel(View view){
        if (mRefundEntity == null){
            return;
        }
        new AlertDialog.Builder(this)
                .setTitle(R.string.alerm_title)
                .setIcon(null)
                .setCancelable(true)
                .setMessage("确认取消申请吗?")
                .setPositiveButton(R.string.alert_yes_button,
                        (dialog, which) -> getPresenter().cancelApplyRefund(mRefundEntity.getId()))
                .setNegativeButton(R.string.alert_no_button,
                        (dialog, which) -> dialog.dismiss()).create().show();
    }
}
