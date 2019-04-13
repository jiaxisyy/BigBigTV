package com.share_will.mobile.ui.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.share_will.mobile.App;
import com.share_will.mobile.MessageEvent;
import com.share_will.mobile.R;
import com.share_will.mobile.listener.DetachDialogClickListener;
import com.share_will.mobile.model.entity.PackageEntity;
import com.share_will.mobile.presenter.ShopPresenter;
import com.share_will.mobile.ui.activity.OrderFormActivity;
import com.share_will.mobile.ui.activity.ShopActivity;
import com.share_will.mobile.ui.adapter.PackageAdapter;
import com.share_will.mobile.ui.dialog.PayTypeFragmentDialog;
import com.share_will.mobile.ui.views.ShopView;
import com.share_will.mobile.ui.widget.RecyclerViewItemDecoration;
import com.ubock.library.base.BaseEntity;
import com.ubock.library.base.BaseFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class MallFragment extends BaseFragment<ShopPresenter> implements BaseQuickAdapter.OnItemClickListener
        , DialogInterface.OnClickListener, ShopView {

    private RecyclerView mRecyclerView;
    private PackageAdapter mPackageAdapter;
    private List<PackageEntity> mDataList = new ArrayList<>();
    private AlertDialog mAlertDialog;
    private PackageEntity mSelectedPackageEntity;
    private DetachDialogClickListener mDetachClickListener = DetachDialogClickListener.wrap(this);
    private PayTypeFragmentDialog mPayTypeFragmentDialog;

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
        mAlertDialog = new AlertDialog.Builder(getActivity())
                .setTitle("确认购买此套餐吗?")
                .setIcon(null)
                .setCancelable(true)
                .setPositiveButton(R.string.alert_yes_button, mDetachClickListener)
                .setNegativeButton(R.string.alert_no_button, mDetachClickListener).create();
        mAlertDialog.setCanceledOnTouchOutside(false);
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
        mDetachClickListener.release();
        mDetachClickListener = null;
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        mSelectedPackageEntity = (PackageEntity) adapter.getItem(position);
        int p = mSelectedPackageEntity.getPackagePrice();
        if (mSelectedPackageEntity.getActivityId() != 0) {
            p = mSelectedPackageEntity.getActivityPrice();
        }
        String price = NumberFormat.getInstance().format(p / 100f);
        //TODO 传递价格等信息
        startActivity(new Intent(getActivity(), OrderFormActivity.class));
        mAlertDialog.setMessage(String.format("%s 需要支付:%s元", mSelectedPackageEntity.getPackageName(), price));
        mAlertDialog.show();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (which == DialogInterface.BUTTON_POSITIVE && mSelectedPackageEntity != null) {
            if (mSelectedPackageEntity.getActivityId() != 0) {
                createPackage(mSelectedPackageEntity.getPackageId(),
                        mSelectedPackageEntity.getActivityId(),
                        mSelectedPackageEntity.getActivityPrice(),
                        mSelectedPackageEntity.getPackageType(),
                        mSelectedPackageEntity.getPackageName());
            } else {
                createPackage(mSelectedPackageEntity.getPackageId(),
                        mSelectedPackageEntity.getActivityId(),
                        mSelectedPackageEntity.getPackagePrice(),
                        mSelectedPackageEntity.getPackageType(),
                        mSelectedPackageEntity.getPackageName());
            }
        }
        mAlertDialog.dismiss();
    }

    @Override
    public void onLoadPackageList(BaseEntity<List<PackageEntity>> data) {
        mDataList.clear();
        if (data != null && data.getCode() == 0) {
            mDataList.addAll(data.getData());
        }
        mPackageAdapter.setLoadMoreData(mDataList);
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
            mPayTypeFragmentDialog = PayTypeFragmentDialog.newInstance(true, 1, orderId, "购买套餐");
            mPayTypeFragmentDialog.show(this);
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
