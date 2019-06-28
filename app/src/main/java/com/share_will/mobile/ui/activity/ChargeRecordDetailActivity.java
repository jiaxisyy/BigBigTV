package com.share_will.mobile.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.share_will.mobile.R;
import com.share_will.mobile.model.entity.ChargeRecordEntity;
import com.ubock.library.base.BaseFragmentActivity;
import com.ubock.library.utils.DateUtils;

import java.text.DecimalFormat;

public class ChargeRecordDetailActivity extends BaseFragmentActivity {
    private TextView tvChargeStartTime;
    private TextView tvChargeDurationTime;
    private TextView tvChargeEnergy;
    private TextView tvChargeAddress;
    private TextView tvChargeDoor;
    private TextView tvChargeStopType;
    private TextView tvChargePrice;
    private TextView tvChargeManagePrice;
    private TextView tvChargeAllPrice;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_charge_record_detail;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setTitle("充电详情");
        ChargeRecordEntity mOrderInfoEntity = (ChargeRecordEntity) getIntent().getSerializableExtra("data");
        tvChargeStartTime = findViewById(R.id.tv_charge_start_time);
        tvChargeDurationTime = findViewById(R.id.tv_charge_duration_time);
        tvChargeEnergy = findViewById(R.id.tv_charge_energy);
        tvChargeAddress = findViewById(R.id.tv_charge_address);
        tvChargeDoor = findViewById(R.id.tv_charge_door);
        tvChargeStopType = findViewById(R.id.tv_charge_stopType);
        tvChargePrice = findViewById(R.id.tv_charge_price);
        tvChargeManagePrice = findViewById(R.id.tv_charge_manage_price);
        tvChargeAllPrice = findViewById(R.id.tv_charge_all_price);

        if (mOrderInfoEntity.getStartTime() > 0) {
            tvChargeStartTime.setText("开始时间: " + DateUtils.timeStampToString(mOrderInfoEntity.getStartTime(), "YYYY-MM-dd HH:mm:ss"));
        }
        if (mOrderInfoEntity.getEndTime() > 0) {
            long time = mOrderInfoEntity.getEndTime() - mOrderInfoEntity.getStartTime();
            tvChargeDurationTime.setText("充电时长: " + formatTime(time));
        } else {
            long time = System.currentTimeMillis() - mOrderInfoEntity.getStartTime();
            tvChargeDurationTime.setText("充电时长: " + formatTime(time));
        }
        tvChargeEnergy.setText("已充能量点: " + mOrderInfoEntity.getEnergyUsed());
        if (!TextUtils.isEmpty(mOrderInfoEntity.getCabinetAddress())) {
            tvChargeAddress.setText("充电座位置: " + mOrderInfoEntity.getCabinetAddress());
        }
        tvChargeDoor.setText("插座号: " + (mOrderInfoEntity.getCabinetDoor()) + "号");
        int balanceType = mOrderInfoEntity.getBalanceType();
        if (balanceType == 0) {
            tvChargeStopType.setVisibility(View.GONE);
        } else if (balanceType == 1) {
            tvChargeStopType.setVisibility(View.VISIBLE);
            tvChargeStopType.setText("结束充电方式：插头已脱落或电池已充满");
        } else if (balanceType == 2) {
            tvChargeStopType.setVisibility(View.VISIBLE);
            tvChargeStopType.setText("结束充电方式：超功率自动结束充电");
        }
        tvChargePrice.setText("充电费用: " + changeMoney(mOrderInfoEntity.getMoney() / 100f));
        tvChargeManagePrice.setText("服务费用: " + changeMoney(mOrderInfoEntity.getGiveMoney() / 100f));
        tvChargeAllPrice.setText("合计: " + changeMoney((mOrderInfoEntity.getGiveMoney() + mOrderInfoEntity.getMoney()) / 100f));
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
}
