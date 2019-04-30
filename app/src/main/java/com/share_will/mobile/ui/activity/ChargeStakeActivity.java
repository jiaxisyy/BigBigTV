package com.share_will.mobile.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.share_will.mobile.App;
import com.share_will.mobile.R;
import com.share_will.mobile.model.entity.ChargeStakeEntity;
import com.share_will.mobile.presenter.ChargeStakePresenter;
import com.share_will.mobile.ui.views.ChargeStakeView;
import com.ubock.library.base.BaseEntity;
import com.ubock.library.base.BaseFragmentActivity;
import com.ubock.library.ui.dialog.ToastExt;
import com.ubock.library.utils.DateUtils;
import com.ubock.library.utils.LogUtils;

public class ChargeStakeActivity extends BaseFragmentActivity<ChargeStakePresenter> implements View.OnClickListener
    ,ChargeStakeView {

    /**
     * 充电扫码
     */
    private static final int REQUEST_CODE_CHARGESCAN = 10010;
    /**
     * 扫码选择界面
     */
    private static final int REQUEST_CODE_CHARGECHOOSE = 10011;

    /**
     * 轮询结束充电结束
     */
    private static final int MSG_FINISH_CHARGE = 1;
    private boolean mIsFinishing = false;

    private TextView mStartTime;
    private TextView mDurationTime;
    private TextView mEnergy;
    private TextView mAddress;
    private TextView mDoor;
    private TextView mPrice;
    private TextView mScanStart;
    private TextView mScanStop;
    private TextView mTvNoInfo;
    private LinearLayout mLlInfo;
    private ChargeStakeEntity mChargeStakeEntity;
    private TextView mFinishCharge;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_charge_stake;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setTitle("充电桩");
        mFinishCharge = findViewById(R.id.tv_finish_charge);
        mStartTime = findViewById(R.id.tv_home_charge_stake_start_time);
        mDurationTime = findViewById(R.id.tv_home_charge_stake_duration_time);
        mEnergy = findViewById(R.id.tv_home_charge_stake_energy);
        mAddress = findViewById(R.id.tv_home_charge_stake_address);
        mDoor = findViewById(R.id.tv_home_charge_stake_door);
        mPrice = findViewById(R.id.tv_home_charge_stake_price);
        mScanStart = findViewById(R.id.tv_home_charge_stake_scan_start);
        mScanStop = findViewById(R.id.tv_home_charge_stake_scan_stop);
        mLlInfo = findViewById(R.id.tv_home_charge_stake_info);
        mTvNoInfo = findViewById(R.id.tv_home_charge_stake_no_info);
        mScanStart.setOnClickListener(this);
        mScanStop.setOnClickListener(this);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        getPresenter().getChargingInfo(true);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
//        getPresenter().getChargingInfo();
    }

    /**
     * 是否显示充电状态信息
     *
     * @param isVisibility
     */
    private void hasChargeInfoView(boolean isVisibility) {

        if (isVisibility) {
            mLlInfo.setVisibility(View.VISIBLE);
            mTvNoInfo.setVisibility(View.INVISIBLE);
            mScanStart.setVisibility(View.GONE);
            mScanStop.setVisibility(View.VISIBLE);

            mStartTime.setText("开始时间: "+DateUtils.timeStampToString(mChargeStakeEntity.getTime(), "YYYY-MM-dd HH:mm:ss"));
            long time = System.currentTimeMillis() - mChargeStakeEntity.getTime();
            mDurationTime.setText("充电时长: "+formatTime(time));
            mAddress.setText("机柜: "+mChargeStakeEntity.getCabinet());
            mDoor.setText("插座号:"+ (mChargeStakeEntity.getIndex() + 1));
            mPrice.setText("费用: 0元");
        } else {
            mLlInfo.setVisibility(View.INVISIBLE);
            mTvNoInfo.setVisibility(View.VISIBLE);
            mScanStart.setVisibility(View.VISIBLE);
            mScanStop.setVisibility(View.GONE);
        }
    }

    private String formatTime(long time){
        int h = (int) (time / (60*60*1000));
        time = (time % (60*60*1000));
        int m = (int) (time / (60*1000));
        return String.format("%d小时%d分钟", h, m);
    }

    /**
     * 充电扫码
     */
    private void chargeScanStart() {
        Intent intent = new Intent(this, CaptureActivity.class);
        startActivityForResult(intent, REQUEST_CODE_CHARGESCAN);
    }

    /**
     * 结束充电
     */
    private void chargeScanStop() {
        getPresenter().stakeCharging(mChargeStakeEntity.getCabinet(), App.getInstance().getUserId(), mChargeStakeEntity.getIndex(), 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_CHARGESCAN) {
                String result = data.getStringExtra("scan_result");
                LogUtils.d(result + "=====");
                if (TextUtils.isEmpty(result)) {
                    ToastExt.showExt("无效二维码");
                    return;
                }
                String sn;
                String time;
                try {
                    Uri uri = Uri.parse(result);
                    sn = uri.getQueryParameter("sn");
                    time = uri.getQueryParameter("time");
                } catch (Exception e) {
                    LogUtils.e(e);
                    ToastExt.showExt("无效二维码");
                    return;
                }
                if (TextUtils.isEmpty(sn) || TextUtils.isEmpty(time)) {
                    ToastExt.showExt("无效二维码");
                    return;
                }
                //result  =   http://www.ep-ai.com/4GAgreement/qr/scanQR.html?customerCode=null&sn=E201805301000001&time=1547014958567
                Intent intent = new Intent(this, ChooseChargeStakeActivity.class);
                intent.putExtra("sn", sn);
                startActivityForResult(intent, REQUEST_CODE_CHARGECHOOSE);

            } else if (requestCode == REQUEST_CODE_CHARGECHOOSE) {
                boolean result = data.getBooleanExtra("key_refresh", false);
                if (result) {
                    getPresenter().getChargingInfo(true);
                }
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_home_charge_stake_scan_start:
                chargeScanStart();
                break;
            case R.id.tv_home_charge_stake_scan_stop:
                mScanStop.setEnabled(false);
                chargeScanStop();
                break;
            default:
                break;
        }
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what){
            case MSG_FINISH_CHARGE:
                getPresenter().getChargingInfo(false);
                sendEmptyMessageDelayed(MSG_FINISH_CHARGE, 3000);
                break;
            default:
                break;
        }
    }

    @Override
    public void onLoadChargingInfo(BaseEntity<ChargeStakeEntity> data) {
        if (data != null){
            mChargeStakeEntity = data.getData();
            if (mChargeStakeEntity!= null){
                if (TextUtils.isEmpty(mChargeStakeEntity.getCabinet())){
                    mChargeStakeEntity = null;
                }
            }
            if (mIsFinishing){
                if (mChargeStakeEntity == null) {
                    mIsFinishing = false;
                    mScanStop.setEnabled(true);
                    mFinishCharge.setVisibility(View.GONE);
                    getBaseHandler().removeMessages(MSG_FINISH_CHARGE);
                } else {
                    return;
                }
            }
        } else {
            mChargeStakeEntity = null;
        }
        hasChargeInfoView(mChargeStakeEntity != null);
    }

    @Override
    public void onChargingResult(BaseEntity<Object> data) {
        if (data != null){
            if (data.getCode() == 0) {
                mIsFinishing = true;
                mFinishCharge.setVisibility(View.VISIBLE);
                sendEmptyMessage(MSG_FINISH_CHARGE);
            } else {
                ToastExt.showExt(data.getMessage());
                mScanStop.setEnabled(true);
            }
        } else {
            ToastExt.showExt("结束充电失败，请稍候再试");
            mScanStop.setEnabled(true);
        }
    }
}
