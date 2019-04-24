package com.share_will.mobile.ui.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.share_will.mobile.App;
import com.share_will.mobile.MessageEvent;
import com.share_will.mobile.R;
import com.share_will.mobile.model.entity.PackageEntity;
import com.share_will.mobile.presenter.ShopPresenter;
import com.share_will.mobile.ui.activity.OrderFormActivity;
import com.share_will.mobile.ui.adapter.PackageAdapter;
import com.share_will.mobile.ui.views.ShopView;
import com.share_will.mobile.ui.widget.RecyclerViewItemDecoration;
import com.ubock.library.base.BaseEntity;
import com.ubock.library.base.BaseFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class MallFragment extends BaseFragment<ShopPresenter> implements BaseQuickAdapter.OnItemClickListener
        , ShopView {

    private RecyclerView mRecyclerView;
    private PackageAdapter mPackageAdapter;
    private List<PackageEntity> mDataList = new ArrayList<>();
    private PackageEntity mSelectedPackageEntity;
    private int mPreice;
    private SwipeRefreshLayout mRefreshLayout;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mall;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        setTitle("网上商城");
        showBackMenu(false);
        mRecyclerView = view.findViewById(R.id.rv_mall_charge);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        mRecyclerView.addItemDecoration(new RecyclerViewItemDecoration(2, 0xFFd8d8d8));
        mRecyclerView.setLayoutManager(manager);
        mPackageAdapter = new PackageAdapter(getActivity());
        mPackageAdapter.hideNoMoreView(true);
        mPackageAdapter.setEnableLoadMore(false);
        mRecyclerView.setAdapter(mPackageAdapter);
        mPackageAdapter.setEmptyView(R.layout.empty_view);
        mPackageAdapter.setOnItemClickListener(this);
        mRefreshLayout = view.findViewById(R.id.refresh_mall);
        mRefreshLayout.setOnRefreshListener(this::getPackageList);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        getPackageList();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        mSelectedPackageEntity = (PackageEntity) adapter.getItem(position);
        mPreice = mSelectedPackageEntity.getPackagePrice();
        if (mSelectedPackageEntity.getActivityId() != 0) {
            mPreice = mSelectedPackageEntity.getActivityPrice();
        }
        createPackage(mSelectedPackageEntity.getPackageId(),
                mSelectedPackageEntity.getActivityId(),
                mPreice,
                mSelectedPackageEntity.getPackageType(),
                mSelectedPackageEntity.getPackageName());
    }

    @Override
    public void onLoadPackageList(BaseEntity<List<PackageEntity>> data) {
        mDataList.clear();
        if (data != null && data.getCode() == 0) {
            mDataList.addAll(data.getData());
        }else {
            showMessage(data.getMessage());
        }
        mPackageAdapter.setLoadMoreData(mDataList);
        mRefreshLayout.setRefreshing(false);
    }

    /**
     * 获取套餐列表
     */
    private void getPackageList() {
        getPresenter().getPackageList(App.getInstance().getUserId());
    }

    /**
     * 显示错误
     *
     * @param msg
     */
    public void showMessage(String msg) {
        new AlertDialog.Builder(getActivity())
                .setTitle("")
                .setMessage(msg)
                .setIcon(null)
                .setCancelable(true)
                .setNegativeButton(R.string.alert_yes_button, null).create().show();
    }

    @Override
    public void onCreateOrder(boolean success, String orderId, String message) {
        if (success) {
            Intent intent = new Intent(getActivity(), OrderFormActivity.class);
            intent.putExtra("price", mPreice);
            intent.putExtra("orderId", orderId);
            intent.putExtra("orderType", 1);
            intent.putExtra("isPackage", true);
            intent.putExtra("body", mSelectedPackageEntity.getPackageName());
            startActivity(intent);
        } else {
            showMessage(message);
        }
    }

    /**
     * 生成套餐订单
     *
     * @param packageId
     * @param activityId
     * @param money
     * @return
     */
    private void createPackage(long packageId, long activityId, int money, int type, String packageName) {
        getPresenter().createPackageOrder(App.getInstance().getUserId(), packageId, activityId, money, type, packageName);
    }

    @Override
    public void onPayPackageResult(boolean success, String message) {
        showMessage(message);
        if (success) {
            //支付成功刷新界面
            getPackageList();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPayEvent(MessageEvent.PayEvent event) {
        if (event.code == 0) {
            //支付成功刷新界面
            getPackageList();
        }
    }
}
