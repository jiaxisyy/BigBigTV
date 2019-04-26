package com.share_will.mobile.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.share_will.mobile.R;
import com.share_will.mobile.model.entity.StationEntity;
import com.share_will.mobile.model.entity.TempEntity;
import com.share_will.mobile.ui.adapter.ChargeStakeAdapter;
import com.ubock.library.base.BaseFragmentActivity;
import com.ubock.library.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

public class ChooseChargeStakeActivity extends BaseFragmentActivity implements View.OnClickListener {
    private RecyclerView mRv;
    private int tempPos = 0;
    private boolean isClicked = false;
    private TextView mTvStart;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_choose_charge_stake;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setTitle("选择插座");
        mRv = findViewById(R.id.rv_charge_stake);
        mTvStart = findViewById(R.id.tv_home_choose_charge_socket_start);
        mTvStart.setOnClickListener(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        mRv.setLayoutManager(gridLayoutManager);
        List<TempEntity> list = new ArrayList<>();
        list.add(new TempEntity(1, isClicked));
        list.add(new TempEntity(0, isClicked));
        list.add(new TempEntity(1, isClicked));
        list.add(new TempEntity(1, isClicked));
        list.add(new TempEntity(0, isClicked));
        list.add(new TempEntity(0, isClicked));
        ChargeStakeAdapter chargeStakeAdapter = new ChargeStakeAdapter(R.layout.item_charge_stake_pic, list);
        mRv.setAdapter(chargeStakeAdapter);
        chargeStakeAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (list.get(position).getStatus() != 0) {
                    chargeStakeAdapter.setData(tempPos, new TempEntity(list.get(tempPos).getStatus(), isClicked));
                    LogUtils.d("tempPos=" + tempPos + ",position=" + position);
                    chargeStakeAdapter.setData(position, new TempEntity(list.get(position).getStatus(), !isClicked));
//                mStationIntent.putExtra("station_entity", list.get(position));
                    LogUtils.d("当前:" + list.get(position).getStatus());
                    tempPos = position;
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_home_choose_charge_socket_start:
                Intent intent = new Intent();
                intent.putExtra("key_refresh", true);
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
    }
}
