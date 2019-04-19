package com.share_will.mobile.ui.activity;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.share_will.mobile.App;
import com.share_will.mobile.R;
import com.share_will.mobile.model.entity.AlarmEntity;
import com.share_will.mobile.model.entity.BatteryEntity;
import com.share_will.mobile.model.entity.ChargeBatteryEntity;
import com.share_will.mobile.presenter.HomeFragmentPresenter;
import com.share_will.mobile.ui.adapter.HomeAlarmAdapter;
import com.share_will.mobile.ui.adapter.HomeAlarmRfidAdapter;
import com.share_will.mobile.ui.fragment.HomeFragment;
import com.share_will.mobile.ui.views.IHomeFragmentView;
import com.share_will.mobile.ui.widget.RecyclerViewItemDecoration;
import com.ubock.library.base.BaseEntity;
import com.ubock.library.base.BaseFragmentActivity;
import com.ubock.library.utils.LogUtils;

import java.util.List;
import java.util.function.LongFunction;

public class AlarmListActivity extends BaseFragmentActivity<HomeFragmentPresenter> implements IHomeFragmentView {

    private RecyclerView mRv;
    private RecyclerView mRvRfid;
    private SwipeRefreshLayout mRefreshLayout;
    private HomeAlarmAdapter mAdapter;
    private HomeAlarmRfidAdapter mAdapterRfid;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_alarm_list;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setTitle("消防告警");
        mRv = findViewById(R.id.rv_home_alarm_list);
        mRvRfid = findViewById(R.id.rv_home_alarm_list_ufid);
        mRefreshLayout = findViewById(R.id.refresh_alarm_list);
        mRefreshLayout.setOnRefreshListener(this::initData);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mRv.addItemDecoration(new RecyclerViewItemDecoration(20, Color.parseColor("#F5F5F5")));
        mRv.setLayoutManager(manager);
        mAdapter = new HomeAlarmAdapter(R.layout.item_home_alarm, null);
        mRv.setAdapter(mAdapter);

        LinearLayoutManager managerRfid = new LinearLayoutManager(this);
        mRvRfid.addItemDecoration(new RecyclerViewItemDecoration(20, Color.parseColor("#F5F5F5")));
        mRvRfid.setLayoutManager(managerRfid);
        mAdapterRfid = new HomeAlarmRfidAdapter(R.layout.item_home_alarm, null);
        mRvRfid.setAdapter(mAdapterRfid);
        initData();
    }

    private void initData() {
        getPresenter().getAlarmList(App.getInstance().getUserId(), App.getInstance().getToken());
    }

    /**
     * 退出对话框
     */
    private void quitDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.alerm_title)
                .setIcon(null)
                .setCancelable(true)
                .setMessage(R.string.alert_quit_confirm)
                .setPositiveButton(R.string.alert_yes_button, (dialog, which) -> {
                    //TODO
                })
                .setNegativeButton(R.string.alert_no_button, (dialog, which) -> dialog.dismiss()).create().show();
    }

    @Override
    public void onLoadAlarmResult(BaseEntity<AlarmEntity> data) {
        mAdapter.setNewData(data.getData().getSmoke());
        mAdapter.setOnItemChildClickListener((baseQuickAdapter, view, i) -> {
            //TODO
        });

        if (data.getData().getRfid() != null && data.getData().getRfid().size() > 0) {
            mAdapterRfid.setNewData(data.getData().getRfid());
        }
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onLoadChargeBatteryInfoResult(BaseEntity<ChargeBatteryEntity> data) {

    }

    @Override
    public void onLoadBatteryInfoResult(BaseEntity<BatteryEntity> data) {

    }
}
