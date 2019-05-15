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
import com.share_will.mobile.model.entity.ChargeStakeOrderInfoEntity;
import com.share_will.mobile.presenter.ChargeStakePresenter;
import com.share_will.mobile.ui.views.ChargeStakeView;
import com.ubock.library.base.BaseEntity;
import com.ubock.library.base.BaseFragmentActivity;
import com.ubock.library.ui.dialog.ToastExt;
import com.ubock.library.utils.DateUtils;
import com.ubock.library.utils.LogUtils;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class ChargeStakeActivity extends BaseFragmentActivity<ChargeStakePresenter> implements View.OnClickListener
        , ChargeStakeView {

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
    /**
     * 支付成功
     */
    private static final int REQUEST_CODE_PAY_SUCCESS = 10012;
    private boolean mIsFinishing = false;

    private TextView mStartTime;
    private TextView mDurationTime;
    private TextView mEnergy;
    private TextView mAddress;
    private TextView mDoor;
    private TextView mStopType;
    private TextView mPrice;
    private TextView mManagePrice;
    private TextView mAllPrice;

    private TextView mScanStart;
    private TextView mScanStop;
    private TextView mTvNoInfo;
    private LinearLayout mLlInfo;
    private ChargeStakeEntity mChargeStakeEntity;
    private TextView mFinishCharge;
    private ChargeStakeOrderInfoEntity mOrderInfoEntity;


    private boolean mOrderStatus = false;

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
        mStopType = findViewById(R.id.tv_home_charge_stake_stopType);

        mPrice = findViewById(R.id.tv_home_charge_stake_price);
        mManagePrice = findViewById(R.id.tv_home_charge_stake_manage_price);
        mAllPrice = findViewById(R.id.tv_home_charge_stake_all_price);

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
        getPresenter().getChargingInfo(true, true);
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

            if (mOrderInfoEntity.getStartTime() > 0) {
                mStartTime.setText("开始时间: " + DateUtils.timeStampToString(mOrderInfoEntity.getStartTime(), "YYYY-MM-dd HH:mm:ss"));
            }
            if (mOrderInfoEntity.getEndTime() > 0) {
                long time = mOrderInfoEntity.getEndTime() - mOrderInfoEntity.getStartTime();
                mDurationTime.setText("充电时长: " + formatTime(time));
            } else {
                long time = System.currentTimeMillis() - mOrderInfoEntity.getStartTime();
                mDurationTime.setText("充电时长: " + formatTime(time));
            }
            mEnergy.setText("已充电量: " + mOrderInfoEntity.getEnergy() / 100f + "度");
            if (!TextUtils.isEmpty(mOrderInfoEntity.getCabinetAddress())) {
                mAddress.setText("充电座位置: " + mOrderInfoEntity.getCabinetAddress());
            }
            mDoor.setText("插座号: " + (mOrderInfoEntity.getDoor())+"号");
            int balanceType = mOrderInfoEntity.getBalanceType();
            if (balanceType == 0) {
                mStopType.setVisibility(View.GONE);
            } else if (balanceType == 1) {
                mStopType.setVisibility(View.VISIBLE);
                mStopType.setText("结束充电方式：插头已脱落或电池已充满");
            } else if (balanceType == 2) {
                mStopType.setVisibility(View.VISIBLE);
                mStopType.setText("结束充电方式：超功率自动结束充电");
            }
            mPrice.setText("充电费用: " + changeMoney(mOrderInfoEntity.getMoney() / 100f));
            mManagePrice.setText("服务费用: " + changeMoney(mOrderInfoEntity.getManageMoney() / 100f));
            mAllPrice.setText("合计: " + changeMoney((mOrderInfoEntity.getManageMoney() + mOrderInfoEntity.getMoney()) / 100f));
        } else {
            mLlInfo.setVisibility(View.INVISIBLE);
            mTvNoInfo.setVisibility(View.VISIBLE);
            mScanStart.setVisibility(View.VISIBLE);
            mScanStop.setVisibility(View.GONE);
        }
    }

    private String changeMoney(float money) {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        return decimalFormat.format(money) + "元";
    }

    private String formatTime(long time) {
        int h = (int) (time / (60 * 60 * 1000));
        time = (time % (60 * 60 * 1000));
        int m = (int) (time / (60 * 1000));
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
        getPresenter().stakeCharging(mOrderInfoEntity.getCabinetId(), App.getInstance().getUserId(), mOrderInfoEntity.getDoor(), 0);
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
                    getPresenter().getChargingInfo(true, true);
//                    flag = true;
//                    sendEmptyMessageDelayed(MSG_FINISH_CHARGE, 3000);
                }
            } else if (requestCode == REQUEST_CODE_PAY_SUCCESS) {
                finish();
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
                LogUtils.d("mIsFinishing=" + mIsFinishing);
                LogUtils.d("mOrderStatus=" + mOrderStatus);
                if (mOrderStatus) {
                    pay();
                } else {
                    mScanStop.setEnabled(false);

                    chargeScanStop();
                }
                break;
            default:
                break;
        }
    }

    private void pay() {
        Intent intent = new Intent(this, OrderFormActivity.class);
        intent.putExtra("orderId", mOrderInfoEntity.getOrderId());
        intent.putExtra("orderType", 0);
        intent.putExtra("price", mOrderInfoEntity.getMoney() + mOrderInfoEntity.getManageMoney());
        intent.putExtra("body", "充电费用");
        startActivityForResult(intent, REQUEST_CODE_PAY_SUCCESS);
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            case MSG_FINISH_CHARGE:
                getPresenter().getChargingInfo(false, true);
                sendEmptyMessageDelayed(MSG_FINISH_CHARGE, 3000);
                break;
            default:
                break;
        }
    }

    @Override
    public void onLoadChargingInfo(BaseEntity<ChargeStakeOrderInfoEntity> data) {
        LogUtils.d("加载");
        if (data != null) {
            mOrderInfoEntity = data.getData();
            if (mOrderInfoEntity != null) {
                if (TextUtils.isEmpty(mOrderInfoEntity.getOrderId())) {
                    mOrderInfoEntity = null;
                } else {
                    mOrderStatus = mOrderInfoEntity.isStatus();
                    if (mOrderStatus) {
                        //已经结束充电,待支付
                        mScanStop.setText("立即支付");
                        if (mIsFinishing) {
                            if (mOrderInfoEntity != null) {
                                mIsFinishing = false;
                                mScanStop.setEnabled(true);
                                mFinishCharge.setVisibility(View.GONE);
                                getBaseHandler().removeMessages(MSG_FINISH_CHARGE);
                                pay();
                            } else {
                                return;
                            }
                        }
                    } else {
                        mScanStop.setText("结束充电");
                    }

                }

            }
        } else {
            mOrderInfoEntity = null;
        }
        hasChargeInfoView(mOrderInfoEntity != null);
    }

    @Override
    public void onChargingResult(BaseEntity<Object> data) {
        if (data != null) {
            if (data.getCode() == 0) {
                mIsFinishing = true;
                mFinishCharge.setVisibility(View.VISIBLE);
                LogUtils.d("正在等待结束充电");
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
