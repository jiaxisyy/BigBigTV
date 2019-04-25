package com.share_will.mobile.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.share_will.mobile.R;
import com.share_will.mobile.ui.adapter.ChargeStakeAdapter;
import com.ubock.library.base.BaseFragmentActivity;

import java.util.ArrayList;
import java.util.List;

public class ChargeStakeActivity extends BaseFragmentActivity {

    private TextView mStartTime;
    private TextView mDurationTime;
    private TextView mEnergy;
    private TextView mAddress;
    private TextView mDoor;
    private TextView mPrice;
    private TextView mStop;
    private RecyclerView mRv;
    private LinearLayout mLlPic;
    private LinearLayout mLlInfo;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_charge_stake;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setTitle("充电桩充电");

        mStartTime = findViewById(R.id.tv_home_charge_stake_start_time);
        mDurationTime = findViewById(R.id.tv_home_charge_duration_time);
        mEnergy = findViewById(R.id.tv_home_charge_stake_energy);
        mAddress = findViewById(R.id.tv_home_charge_stake_address);
        mDoor = findViewById(R.id.tv_home_charge_stake_door);
        mPrice = findViewById(R.id.tv_home_charge_stake_price);
        mStop = findViewById(R.id.tv_home_charge_stake_stop);

        mRv = findViewById(R.id.rv_charge_stake);
        mLlPic = findViewById(R.id.ll_charge_stake_pic);
        mLlInfo = findViewById(R.id.tv_home_charge_stake_info);
        mLlPic.setVisibility(View.VISIBLE);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        mRv.setLayoutManager(gridLayoutManager);
        List<Integer> list = new ArrayList<>();
        list.add(0);
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);
        ChargeStakeAdapter adapter = new ChargeStakeAdapter(R.layout.item_charge_stake_pic, list);
        mRv.setAdapter(adapter);
        hasChargeInfoView(false);
    }

    /**
     * 是否显示充电状态信息
     *
     * @param isVisibility
     */
    private void hasChargeInfoView(boolean isVisibility) {

        if (isVisibility) {
            mLlPic.setVisibility(View.VISIBLE);
            mLlInfo.setVisibility(View.INVISIBLE);
            mStop.setVisibility(View.VISIBLE);
        } else {
            mLlPic.setVisibility(View.INVISIBLE);
            mLlInfo.setVisibility(View.VISIBLE);
            mStop.setVisibility(View.INVISIBLE);
        }


    }
}
