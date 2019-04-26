package com.share_will.mobile.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.share_will.mobile.App;
import com.share_will.mobile.R;
import com.share_will.mobile.ui.adapter.ChargeStakeAdapter;
import com.ubock.library.base.BaseFragmentActivity;
import com.ubock.library.ui.dialog.ToastExt;
import com.ubock.library.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

public class ChargeStakeActivity extends BaseFragmentActivity implements View.OnClickListener {

    /**
     * 充电扫码
     */
    private static final int REQUEST_CODE_CHARGESCAN = 10010;
    /**
     * 扫码选择界面
     */
    private static final int REQUEST_CODE_CHARGECHOOSE = 10011;
    private TextView mStartTime;
    private TextView mDurationTime;
    private TextView mEnergy;
    private TextView mAddress;
    private TextView mDoor;
    private TextView mPrice;
    private TextView mScan;
    private TextView mTvNoInfo;
    private LinearLayout mLlInfo;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_charge_stake;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setTitle("充电桩");
        mStartTime = findViewById(R.id.tv_home_charge_stake_start_time);
        mDurationTime = findViewById(R.id.tv_home_charge_duration_time);
        mEnergy = findViewById(R.id.tv_home_charge_stake_energy);
        mAddress = findViewById(R.id.tv_home_charge_stake_address);
        mDoor = findViewById(R.id.tv_home_charge_stake_door);
        mPrice = findViewById(R.id.tv_home_charge_stake_price);
        mScan = findViewById(R.id.tv_home_charge_stake_scan);
        mLlInfo = findViewById(R.id.tv_home_charge_stake_info);
        mTvNoInfo = findViewById(R.id.tv_home_charge_stake_no_info);
        mScan.setOnClickListener(this);
        hasChargeInfoView(false);
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
            mScan.setText("结束充电");
        } else {
            mLlInfo.setVisibility(View.INVISIBLE);
            mTvNoInfo.setVisibility(View.VISIBLE);
            mScan.setText("扫一扫");
        }
    }

    /**
     * 充电扫码
     */
    private void chargeScan() {
        Intent intent = new Intent(this, CaptureActivity.class);
        startActivityForResult(intent, REQUEST_CODE_CHARGESCAN);
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
                startActivityForResult(new Intent(this, ChooseChargeStakeActivity.class), REQUEST_CODE_CHARGECHOOSE);

            } else if (requestCode == REQUEST_CODE_CHARGECHOOSE) {
                boolean result = data.getBooleanExtra("key_refresh", false);
                if (result) {
                    initData();
                }
            }
        }
    }

    private void initData() {
        LogUtils.d("刷新成功");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_home_charge_stake_scan:
                chargeScan();
                break;
        }
    }

}
