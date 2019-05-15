package com.share_will.mobile.ui.activity;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.share_will.mobile.App;
import com.share_will.mobile.R;
import com.share_will.mobile.model.entity.AlarmEntity;
import com.share_will.mobile.model.entity.BatteryEntity;
import com.share_will.mobile.model.entity.ChargeBatteryEntity;
import com.share_will.mobile.presenter.AlarmFragmentPresenter;
import com.share_will.mobile.presenter.HomeFragmentPresenter;
import com.share_will.mobile.ui.adapter.HomeAlarmAdapter;
import com.share_will.mobile.ui.adapter.HomeAlarmRfidAdapter;
import com.share_will.mobile.ui.views.IAlarmFragmentView;
import com.share_will.mobile.ui.views.IHomeFragmentView;
import com.share_will.mobile.ui.widget.RecyclerViewItemDecoration;
import com.ubock.library.annotation.PresenterInjector;
import com.ubock.library.base.BaseEntity;
import com.ubock.library.base.BaseFragmentActivity;
import com.ubock.library.ui.adapter.LoadMoreAdapter;
import com.ubock.library.ui.dialog.ToastExt;
import com.ubock.library.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

public class AlarmListActivity extends BaseFragmentActivity<HomeFragmentPresenter> implements IHomeFragmentView
        , IAlarmFragmentView, LoadMoreAdapter.OnLoadMoreListener {


    /**
     * 每次加载条目数量
     */
    private static final int LOADMOREITEMNUM = 15;
    private RecyclerView mRv;
    private RecyclerView mRvRfid;
    private SwipeRefreshLayout mRefreshLayout;
    private HomeAlarmAdapter mAdapter;
    private HomeAlarmRfidAdapter mAdapterRfid;

    @PresenterInjector
    AlarmFragmentPresenter fragmentPresenter;
    private TextView mTvNoAlarm;
    private List<AlarmEntity.RfidBean> mRfidBeanList;

    private int mCurrentCounter;
    private int mTotalCounter = 0;
    private boolean isErr = false;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_alarm_list;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setTitle("告警信息");
        mRv = findViewById(R.id.rv_home_alarm_list);
        mRvRfid = findViewById(R.id.rv_home_alarm_list_ufid);
        mRefreshLayout = findViewById(R.id.refresh_alarm_list);
        mTvNoAlarm = findViewById(R.id.tv_home_no_alarm);
        mRefreshLayout.setOnRefreshListener(this::initData);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mRv.addItemDecoration(new RecyclerViewItemDecoration(20, Color.parseColor("#F5F5F5")));
        mRv.setLayoutManager(manager);
        mAdapter = new HomeAlarmAdapter(R.layout.item_home_alarm, null);
        mRv.setAdapter(mAdapter);

        LinearLayoutManager managerRfid = new LinearLayoutManager(this);
        mRvRfid.addItemDecoration(new RecyclerViewItemDecoration(20, Color.parseColor("#F5F5F5")));
        mRvRfid.setLayoutManager(managerRfid);
        mAdapterRfid = new HomeAlarmRfidAdapter(this,R.layout.item_home_alarm);
        mRvRfid.setAdapter(mAdapterRfid);
        //TODO 合并两个RecyclerView
        mRvRfid.setNestedScrollingEnabled(false);
        mRv.setNestedScrollingEnabled(false);
        mAdapterRfid.setEnableLoadMore(true);
        mAdapterRfid.setEmptyView(R.layout.empty_view);
        mAdapterRfid.setOnLoadMoreListener(this);
        initData();
    }

    private void initData() {
        mAdapterRfid.loadMoreEnd(false);
        getPresenter().getAlarmList(App.getInstance().getUserId(), App.getInstance().getToken());
    }

    /**
     * 关闭告警对话框
     */
    private void closeDialog(AlarmEntity.SmokeBean data) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.alerm_title)
                .setIcon(null)
                .setCancelable(true)
                .setMessage(R.string.alert_quit_close_alarm)
                .setPositiveButton(R.string.alert_yes_button, (dialog, which) -> {
                    closeAlarm(data);
                })
                .setNegativeButton(R.string.alert_no_button, (dialog, which) -> dialog.dismiss()).create().show();
    }


    private void closeAlarm(AlarmEntity.SmokeBean data) {
        fragmentPresenter.closeAlarm(App.getInstance().getUserId(), data.getDevtype(), data.getDeveui());
    }

    @Override
    public void onLoadAlarmResult(BaseEntity<AlarmEntity> data) {
        if (data != null) {
            mAdapterRfid.loadMoreEnd(true);
            int size = data.getData().getSmoke().size();
            if (size > 0 || data.getData().getRfid().size() > 0) {
                mTvNoAlarm.setVisibility(View.INVISIBLE);
            }
            if (size > 0) {
                List<AlarmEntity.SmokeBean> smokeAlarms = new ArrayList<>();//烟感告警
                List<AlarmEntity.SmokeBean> smokeBeanList = data.getData().getSmoke();//所有设备
                for (AlarmEntity.SmokeBean smokeBean : smokeBeanList) {
                    if (!TextUtils.isEmpty(smokeBean.getAlarmcode())) {
                        smokeAlarms.add(smokeBean);
                    }
                }
                mAdapter.setNewData(smokeAlarms);
                mAdapter.setOnItemChildClickListener((baseQuickAdapter, view, i) -> {
                    if (view.getId() == R.id.item_tv_home_alarm_close && data.getData().getSmoke().size() > 0) {
                        closeDialog(data.getData().getSmoke().get(i));
                    }
                });

            }
            if (data.getData().getRfid() != null && data.getData().getRfid().size() > 0) {
                //TODO 可能会改动显示条件,待定
                if (mRfidBeanList != null) {
                    mRfidBeanList.clear();
                }
                mRfidBeanList = data.getData().getRfid();
                mTotalCounter = mRfidBeanList.size();
                LogUtils.d("RfidBeanList.size=" + mRfidBeanList.size());
                if (mRfidBeanList.size() > mAdapterRfid.getPageSize()) {
                    mAdapterRfid.setLoadMoreData(subData(mRfidBeanList, 0, mAdapterRfid.getPageSize()), true);
                    mCurrentCounter = mAdapterRfid.getPageSize();
                } else {
                    mAdapterRfid.setLoadMoreData(mRfidBeanList);
                }
                mAdapterRfid.disableLoadMoreIfNotFullPage();
            }

        } else {
            mTvNoAlarm.setVisibility(View.VISIBLE);
            mAdapter.setNewData(null);
            mAdapterRfid.setNewData(null);
        }
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onCloseAlarmResult(BaseEntity<Object> s) {
        if (s != null) {
            ToastExt.showExt("关闭告警成功");
        } else {
            ToastExt.showExt("关闭告警失败,请重试");
        }
        initData();
    }

    @Override
    public void onLoadChargeBatteryInfoResult(BaseEntity<ChargeBatteryEntity> data) {

    }

    @Override
    public void onLoadBatteryInfoResult(BaseEntity<BatteryEntity> data) {

    }

    @Override
    public void onLoadMore(int currentPage) {
        LogUtils.d("onLoadMore is executed");
        mAdapterRfid.loadMoreEnd();
        if (mCurrentCounter >= mTotalCounter) {
            //数据全部加载完毕
            mAdapterRfid.loadMoreEnd();
        } else {
            //成功获取更多数据
            LogUtils.d("mCurrentCounter=" + mCurrentCounter);
            List<AlarmEntity.RfidBean> rfidBeans = subData(mRfidBeanList, mCurrentCounter, mCurrentCounter + LOADMOREITEMNUM);
            mAdapterRfid.setLoadMoreData(rfidBeans);
            mCurrentCounter = mAdapterRfid.getData().size();
            mAdapterRfid.loadMoreComplete();
        }
    }


    public List<AlarmEntity.RfidBean> subData(List<AlarmEntity.RfidBean> list, int start, int end) {
        List<AlarmEntity.RfidBean> dataList = new ArrayList<>();
        for (int i = start; i < end; i++) {
            dataList.add(list.get(i));
        }
        return dataList;
    }
}
