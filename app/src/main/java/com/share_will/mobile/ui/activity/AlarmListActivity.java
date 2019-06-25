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

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.share_will.mobile.App;
import com.share_will.mobile.R;
import com.share_will.mobile.model.entity.AlarmEntity;
import com.share_will.mobile.model.entity.AlarmMultiEntity;
import com.share_will.mobile.model.entity.BatteryEntity;
import com.share_will.mobile.model.entity.ChargeBatteryEntity;
import com.share_will.mobile.presenter.AlarmFragmentPresenter;
import com.share_will.mobile.presenter.HomeFragmentPresenter;
import com.share_will.mobile.ui.adapter.MultiAdapter;
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
        , IAlarmFragmentView, BaseQuickAdapter.RequestLoadMoreListener {
    /**
     * 每次加载条目数量
     */
    private static final int LOADMOREITEMCOUNT = 15;
    private RecyclerView mRv;
    private SwipeRefreshLayout mRefreshLayout;
    @PresenterInjector
    AlarmFragmentPresenter fragmentPresenter;
    private TextView mTvNoAlarm;
    private List<AlarmEntity.RfidBean> mRfidBeanList;

    private int mCurrentCounter = 0;
    private int mTotalCounter = 0;
    private List<AlarmEntity.SmokeBean> smokeBeanList;
    private List<AlarmEntity.SmokeBean> mSmokeAlarms;
    private MultiAdapter mMultiAdapter;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_alarm_list;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setTitle("告警信息");
        mRv = findViewById(R.id.rv_home_alarm_list);
        mRefreshLayout = findViewById(R.id.refresh_alarm_list);
        mTvNoAlarm = findViewById(R.id.tv_home_no_alarm);
        mRefreshLayout.setOnRefreshListener(this::initData);

        LinearLayoutManager managerRfid = new LinearLayoutManager(this);
        mRv.addItemDecoration(new RecyclerViewItemDecoration(20, Color.parseColor("#F5F5F5")));
        mRv.setLayoutManager(managerRfid);
        mMultiAdapter = new MultiAdapter(null);
        mRv.setAdapter(mMultiAdapter);
        mMultiAdapter.setEnableLoadMore(false);
        mMultiAdapter.bindToRecyclerView(mRv);
        mMultiAdapter.setEmptyView(R.layout.empty_view);
        mMultiAdapter.setOnLoadMoreListener(this, mRv);
        initData();
    }


    private void initData() {
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
            mMultiAdapter.loadMoreEnd(true);
            int size = data.getData().getSmoke().size();
            if (size > 0 || data.getData().getRfid().size() > 0) {
                mTvNoAlarm.setVisibility(View.INVISIBLE);
            }
            if (size > 0) {
                //烟感告警
                mSmokeAlarms = new ArrayList<>();
                List<AlarmEntity.SmokeBean> smokeBeanList = data.getData().getSmoke();//所有设备
                for (AlarmEntity.SmokeBean smokeBean : smokeBeanList) {
                    if (!TextUtils.isEmpty(smokeBean.getAlarmcode())) {
                        mSmokeAlarms.add(smokeBean);
                    }
                }
                mMultiAdapter.setOnItemChildClickListener((baseQuickAdapter, view, i) -> {
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
                setData();
            }

        } else {
            mTvNoAlarm.setVisibility(View.VISIBLE);
            mMultiAdapter.setNewData(null);
        }
        mRefreshLayout.setRefreshing(false);
    }

    private void setData() {
        List<AlarmMultiEntity> list = new ArrayList<>();
        if (mSmokeAlarms != null) {
            int smokeSize = mSmokeAlarms.size();
            if (smokeSize > 0) {
                for (int i = 0; i < smokeSize; i++) {
                    AlarmMultiEntity alarmMultiEntity = new AlarmMultiEntity(AlarmMultiEntity.TYPE_SMOKE, mSmokeAlarms.get(i));
                    list.add(alarmMultiEntity);
                }
            }

        }
        if (mRfidBeanList != null) {
            int rfidSize = mRfidBeanList.size();
            if (rfidSize > 0) {
                if (rfidSize > LOADMOREITEMCOUNT) {
                    //初始化数据
                    for (int i = 0; i < LOADMOREITEMCOUNT; i++) {
                        AlarmMultiEntity alarmMultiEntity = new AlarmMultiEntity(AlarmMultiEntity.TYPE_RFID, mRfidBeanList.get(i));
                        list.add(alarmMultiEntity);
                    }
                    mCurrentCounter = LOADMOREITEMCOUNT;
                } else {
                    for (int i = 0; i < rfidSize; i++) {
                        AlarmMultiEntity alarmMultiEntity = new AlarmMultiEntity(AlarmMultiEntity.TYPE_RFID, mRfidBeanList.get(i));
                        list.add(alarmMultiEntity);
                    }
                    mCurrentCounter = rfidSize;
                }
            }
        }
        mMultiAdapter.setNewData(list);
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


    public List<AlarmEntity.RfidBean> subData(List<AlarmEntity.RfidBean> list, int start, int end) {
        LogUtils.d("start=" + start + "/end=" + end);
        List<AlarmEntity.RfidBean> dataList = new ArrayList<>();
        try {
            for (int i = start; i < end; i++) {
                dataList.add(list.get(i));
            }
            return dataList;
        } catch (Exception e) {
            for (int i = start; i < dataList.size(); i++) {
                dataList.add(list.get(i));
            }
            return dataList;
        }
    }

    @Override
    public void onLoadMoreRequested() {
        LogUtils.d("onLoadMore is executed");
        mMultiAdapter.loadMoreEnd();
        if (mCurrentCounter >= mTotalCounter) {
            //数据全部加载完毕
            mMultiAdapter.loadMoreEnd();
        } else {
            //成功获取更多数据
            LogUtils.d("mCurrentCounter=" + mCurrentCounter);
            List<AlarmEntity.RfidBean> rfidBeans = subData(mRfidBeanList, mCurrentCounter, mCurrentCounter + LOADMOREITEMCOUNT);
            List<AlarmMultiEntity> list = new ArrayList<>();
            for (int i = mCurrentCounter; i < mCurrentCounter + rfidBeans.size(); i++) {
                AlarmMultiEntity alarmMultiEntity = new AlarmMultiEntity(AlarmMultiEntity.TYPE_RFID, mRfidBeanList.get(i));
                LogUtils.d("testNum=" + mRfidBeanList.get(i).getTestNum());
                list.add(alarmMultiEntity);
            }
            mMultiAdapter.addData(list);
            mCurrentCounter = mMultiAdapter.getData().size() - mSmokeAlarms.size();
            mMultiAdapter.loadMoreComplete();
        }
    }
}
