package com.share_will.mobile.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.share_will.mobile.App;
import com.share_will.mobile.R;

import com.share_will.mobile.model.entity.ChargeBatteryEntity;
import com.share_will.mobile.presenter.HomeServicePresenter;

import com.share_will.mobile.ui.views.IHomeServiceView;
import com.ubock.library.base.BaseEntity;
import com.ubock.library.base.BaseFragmentActivity;
import com.ubock.library.utils.DateUtils;
import com.ubock.library.utils.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Map;


public class HomeServiceActivity extends BaseFragmentActivity<HomeServicePresenter> implements IHomeServiceView, View.OnClickListener {
    private static final int REQUEST_CODE_CHARGESCAN = 10010;
    private static final int REQUEST_CODE_STOP_CHARGESCAN = 10011;
    private static final int REQUEST_CODE_ORDERFORM = 10012;
    private TextView mChargeScan;
    private TextView mTextServiceCharge;
    private LinearLayout mLlInCludeHomeBottom;


    private TextView mStartTime;
    private TextView mEnoughTime;
    private TextView mDurationTime;
    private TextView mNowSop;
    private TextView mEnergy;
    private TextView mAddress;
    private TextView mDoor;
    private TextView mMoneyCharge;
    private TextView mMoneyManage;
    private TextView mMoneyAll;
    private RelativeLayout mCardMoney;
    private int mPrice;
    private SwipeRefreshLayout mRefreshLayout;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_service;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setTitle("充电服务");
        mChargeScan = findViewById(R.id.tv_home_service_charge_scan);
        mLlInCludeHomeBottom = findViewById(R.id.include_layout_home_bottom);

        mTextServiceCharge = findViewById(R.id.tv_text_home_service_charge);
        mChargeScan.setOnClickListener(this);
        mRefreshLayout = findViewById(R.id.refresh_service);
        mRefreshLayout.setOnRefreshListener(this::initData);
        mStartTime = findViewById(R.id.tv_home_charge_start_time);
        mEnoughTime = findViewById(R.id.tv_home_charge_enough_time);
        mDurationTime = findViewById(R.id.tv_home_charge_duration_time);
        mNowSop = findViewById(R.id.tv_home_charge_now_sop);
        mEnergy = findViewById(R.id.tv_home_charge_energy);
        mAddress = findViewById(R.id.tv_home_charge_address);
        mDoor = findViewById(R.id.tv_home_charge_door);
        mMoneyCharge = findViewById(R.id.tv_home_money_charge);
        mMoneyManage = findViewById(R.id.tv_home_money_manage);
        mMoneyAll = findViewById(R.id.tv_home_money_all);
        mCardMoney = findViewById(R.id.rl_card_money);
        initData();
    }

    private void initData() {
        getPresenter().getChargeBatteryInfo(App.getInstance().getUserId(), App.getInstance().getToken());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_home_service_charge_scan:
                if (mChargeScan.getText().equals("扫一扫")) {
                    chargeScan();
                } else if (mChargeScan.getText().equals("扫一扫结束充电")) {
                    stopChargeScan();
                }
                break;
        }

    }

    /**
     * 充电扫码
     */
    private void chargeScan() {
        Intent intent = new Intent(this, CaptureActivity.class);
        startActivityForResult(intent, REQUEST_CODE_CHARGESCAN);
    }

    /**
     * 结束充电扫码
     */
    private void stopChargeScan() {
        Intent intent = new Intent(this, CaptureActivity.class);
        startActivityForResult(intent, REQUEST_CODE_STOP_CHARGESCAN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHARGESCAN) {
            //处理扫码换电
            if (resultCode == RESULT_OK) {
                String result = data.getStringExtra("scan_result");
                LogUtils.d(result + "=============");
                if (!TextUtils.isEmpty(result)) {
                    if (result.contains("sn=") && result.contains("&time")) {
                        String sn = result.substring(result.indexOf("sn=") + 3, result.indexOf("&time"));
                        getPresenter().chargeScan(App.getInstance().getUserId(), App.getInstance().getToken(), sn, "1", 1, 1);
                    }
                }
            }
        } else if (requestCode == REQUEST_CODE_STOP_CHARGESCAN) {
            //处理扫码换电
            if (resultCode == RESULT_OK) {
                String result = data.getStringExtra("scan_result");
                LogUtils.d(result + "=============");
                if (!TextUtils.isEmpty(result)) {
                    if (result.contains("sn=") && result.contains("&time")) {
                        String sn = result.substring(result.indexOf("sn=") + 3, result.indexOf("&time"));
                        getPresenter().stopChargeScan(App.getInstance().getUserId(), App.getInstance().getToken(), sn);
                    }
                }
            } else {
                LogUtils.d("=============");
            }
        }

    }


    @Override
    public void OnChargeResult(BaseEntity<ChargeBatteryEntity> s) {
        if (s != null) {
            ChargeBatteryEntity entity = s.getData();
            mChargeScan.setText("扫一扫结束充电");
            mLlInCludeHomeBottom.setVisibility(View.VISIBLE);
            mTextServiceCharge.setVisibility(View.INVISIBLE);
            mStartTime.setText("开始时间:   " + DateUtils.timeStampToString(entity.getStartTime(), DateUtils.YYYYMMDD_HHMMSS));
            if (entity.getFullTime() != 0) {
                mEnoughTime.setText("充满时间:   " + DateUtils.timeStampToString(entity.getFullTime(), DateUtils.YYYYMMDD_HHMMSS));
                long l = entity.getFullTime() - entity.getStartTime();
                mDurationTime.setText("充电时长:   " + DateUtils.unixToUTcTimeDuration(l));
            } else {
                mEnoughTime.setVisibility(View.GONE);
                long l = entity.getNowTime() - entity.getStartTime();
                mDurationTime.setText("充电时长:   " + DateUtils.unixToUTcTimeDuration(l));
            }
            if (!TextUtils.isEmpty(entity.getSop())) {
                mNowSop.setText("当前电量:   " + entity.getSop() + "%");
            }

            mEnergy.setText("已充能量点:   " + entity.getEnergy());
            if (!TextUtils.isEmpty(entity.getCabinetAddress())) {
                mAddress.setText("电池位置:   " + entity.getCabinetAddress());
            }

            mDoor.setText("仓门号:   " + entity.getDoor());

            mMoneyCharge.setText(intChange(entity.getMoney() / 100f) + "元");
            mMoneyManage.setText(intChange(entity.getManageMoney() / 100f) + "元");
            mPrice = entity.getMoney() + entity.getManageMoney();
            mMoneyAll.setText(String.format("合计:%s元", intChange(mPrice / 100f) ));
        } else {
            mChargeScan.setText("扫一扫");
            mLlInCludeHomeBottom.setVisibility(View.INVISIBLE);
            mTextServiceCharge.setVisibility(View.VISIBLE);
        }
        mRefreshLayout.setRefreshing(false);
    }

    public String intChange(float num) {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(num);
    }

    @Override
    public void OnChargeScanResult(BaseEntity<Object> s) {
        if (s != null) {
            getPresenter().getChargeBatteryInfo(App.getInstance().getUserId(), App.getInstance().getToken());
        }
    }

    @Override
    public void OnStopChargeScanResult(BaseEntity<Object> s) {
        if (s != null) {
            try {
                //处理数字过长变成科学计数法
                Gson gson = new Gson();
                Map<String, String> resStr = gson.fromJson(s.getData().toString(),
                        new TypeToken<Map<String, String>>() {
                        }.getType());
                JSONObject jsonObject = new JSONObject(resStr);
                String id = String.valueOf(jsonObject.get("orderId"));
                Intent intent = new Intent(this, OrderFormActivity.class);
                intent.putExtra("orderId", id);
                intent.putExtra("orderType", 0);
                intent.putExtra("price", mPrice);
                intent.putExtra("body", "充电费用");

                startActivityForResult(intent, REQUEST_CODE_ORDERFORM);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        LogUtils.d("onRestart");
//        getPresenter().getChargeBatteryInfo(App.getInstance().getUserId(), App.getInstance().getToken());
    }
}
