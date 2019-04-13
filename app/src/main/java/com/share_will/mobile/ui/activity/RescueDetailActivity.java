package com.share_will.mobile.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.share_will.mobile.Constant;
import com.share_will.mobile.R;
import com.share_will.mobile.listener.DetachDialogClickListener;
import com.share_will.mobile.model.entity.RescueEntity;
import com.share_will.mobile.presenter.RescueListPresenter;
import com.share_will.mobile.ui.views.RescueListView;
import com.ubock.library.base.BaseEntity;
import com.ubock.library.base.BaseFragmentActivity;
import com.ubock.library.ui.dialog.ToastExt;

import java.text.SimpleDateFormat;
import java.util.List;

public class RescueDetailActivity extends BaseFragmentActivity<RescueListPresenter> implements RescueListView
        ,DialogInterface.OnClickListener{

    private RescueEntity mRescueEntity;
    private TextView mStatus;
    private TextView mStation;
    private TextView mStationMaster;
    private TextView mStationMasterPhone;
    private TextView mVerifyCode;
    private TextView mCabinet;
    private TextView mBattery;
    private TextView mDoor;
    private TextView mRescueReason;
    private TextView mRescueComment;
    private TextView mDate;
    private Button mCancelBtn;
    private AlertDialog mAlertDialog;
    private DetachDialogClickListener mDetachClickListener = DetachDialogClickListener.wrap(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mRescueEntity = getIntent().getParcelableExtra("entity");
        super.onCreate(savedInstanceState);
        setTitle("救援详情");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_rescue_detail;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mStatus = findViewById(R.id.tv_status);
        mStation = findViewById(R.id.tv_station);
        mStationMaster = findViewById(R.id.tv_station_master);
        mStationMasterPhone = findViewById(R.id.tv_station_master_phone);
        mVerifyCode = findViewById(R.id.tv_verify_code);
        mCabinet = findViewById(R.id.tv_cabinet);
        mBattery = findViewById(R.id.tv_battery);
        mDoor = findViewById(R.id.tv_door);
        mRescueReason = findViewById(R.id.tv_rescue_reason);
        mRescueComment = findViewById(R.id.tv_comment);
        mDate = findViewById(R.id.tv_date);
        mCancelBtn = findViewById(R.id.btn_cancel);
        if (mRescueEntity != null) {
            mStatus.setText(Constant.RescueStatus.get(mRescueEntity.getStatus()));
            mStation.setText(mRescueEntity.getStationName());
            mStationMaster.setText(mRescueEntity.getStationMaster());
            mStationMasterPhone.setText(mRescueEntity.getStationMasterPhone());
            mVerifyCode.setText(mRescueEntity.getVerCode());
            mCabinet.setText(mRescueEntity.getCabinetId());
            mBattery.setText(mRescueEntity.getBatteryId());
            mDoor.setText(mRescueEntity.getDoor()+"");
            mRescueReason.setText(mRescueEntity.getRescueCause());
            mRescueComment.setText(mRescueEntity.getResolve());
            SimpleDateFormat format = new SimpleDateFormat("YYYY.MM.dd");
            mDate.setText(format.format(mRescueEntity.getCreateTime()));
            if (mRescueEntity.getStatus() == 0){
                mCancelBtn.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onLoadRescueList(BaseEntity<List<RescueEntity>> ret) {

    }

    @Override
    public void onCancelRescue(BaseEntity<Object> ret) {
        if (ret.getCode() == 0){
            ToastExt.showExt("已取消申请");
            finish();
        } else {
            ToastExt.showExt(ret.getMessage());
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (which == DialogInterface.BUTTON_POSITIVE){
            getPresenter().cancelRescue(mRescueEntity.getId());
        } else {

        }
    }

    public void onCancel(View view){
        if (mAlertDialog == null) {
            mAlertDialog = new AlertDialog.Builder(this)
                    .setTitle(R.string.alerm_title)
                    .setIcon(null)
                    .setCancelable(true)
                    .setMessage("确认取消救援申请吗?")
                    .setPositiveButton(R.string.alert_yes_button, mDetachClickListener)
                    .setNegativeButton(R.string.alert_no_button, mDetachClickListener).create();
            mDetachClickListener.clearOnDetach(mAlertDialog);
        }
        mAlertDialog.show();
    }

    @Override
    protected void onDestroy() {
        mDetachClickListener = null;
        super.onDestroy();
    }
}
