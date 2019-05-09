package com.share_will.mobile.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.share_will.mobile.App;
import com.share_will.mobile.R;
import com.share_will.mobile.model.entity.ChargeStakeEntity;
import com.share_will.mobile.model.entity.ChargeStakeOrderInfoEntity;
import com.share_will.mobile.presenter.ChargeStakePresenter;
import com.share_will.mobile.ui.adapter.ChargeStakeAdapter;
import com.share_will.mobile.ui.views.ChargeStakeView;
import com.ubock.library.base.BaseEntity;
import com.ubock.library.base.BaseFragmentActivity;
import com.ubock.library.ui.dialog.ToastExt;
import com.ubock.library.utils.LogUtils;

import java.util.List;

public class ChooseChargeStakeActivity extends BaseFragmentActivity<ChargeStakePresenter> implements View.OnClickListener
        , ChargeStakeView {
    private static final int MSG_CREATE_OEDER = 1;
    private RecyclerView mRv;
    private int mSelectedIndex = -1;
    private TextView mTvStart;
    private String mCabinetId;
    private ChargeStakeAdapter mChargeStakeAdapter;
    private ProgressDialog progressDialog;
    /**
     * 轮询次数
     */
    private int MAXSECOND = 5;
    /**
     * 已轮询次数
     */
    private int NOWSECOND = 0;
    /**
     * 是否关闭加载框
     */
    private boolean showLoading = true;
    private boolean closeLoading = false;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_choose_charge_stake;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCabinetId = getIntent().getStringExtra("sn");
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        getPresenter().getStakeStatus(mCabinetId);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setTitle("选择插座");
        mRv = findViewById(R.id.rv_charge_stake);
        mTvStart = findViewById(R.id.tv_home_choose_charge_socket_start);
        mTvStart.setOnClickListener(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        mRv.setLayoutManager(gridLayoutManager);
        mChargeStakeAdapter = new ChargeStakeAdapter(null);
        mRv.setAdapter(mChargeStakeAdapter);
        mChargeStakeAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ChargeStakeEntity entity = mChargeStakeAdapter.getItem(position);
                if (entity.getStatus() == 0 && mSelectedIndex != position) {
                    entity.setSelected(true);
                    mChargeStakeAdapter.setData(position, entity);
                    if (mSelectedIndex >= 0) {
                        ChargeStakeEntity pre = mChargeStakeAdapter.getItem(mSelectedIndex);
                        pre.setSelected(false);
                        mChargeStakeAdapter.setData(mSelectedIndex, pre);
                        LogUtils.d("mSelectedIndex=" + mSelectedIndex + ",position=" + position);
                    }
                    mSelectedIndex = position;
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_home_choose_charge_socket_start:
                startCharge();
                break;
        }
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            case MSG_CREATE_OEDER:
                getPresenter().getChargingInfo(showLoading, closeLoading);
                sendEmptyMessageDelayed(MSG_CREATE_OEDER, 3000);
                break;
            default:
                break;
        }
    }

    private void startCharge() {
        if (mSelectedIndex < 0) {
            ToastExt.showExt("请选择充电桩");
        } else {
            getPresenter().stakeCharging(mCabinetId, App.getInstance().getUserId(), mSelectedIndex + 1, 1);
        }
    }

    @Override
    public void onLoadStakeStatus(BaseEntity<List<ChargeStakeEntity>> data) {
        if (data != null) {
            if (data.getCode() == 0 && data.getData() != null) {
                mChargeStakeAdapter.setNewData(data.getData());
            } else {
                ToastExt.showExt(data.getMessage());
            }
        } else {
            ToastExt.showExt("当前机柜不支持充电桩充电");
        }
    }

    @Override
    public void onChargingResult(BaseEntity<Object> data) {
        if (data != null) {
            if (data.getCode() == 0) {
                Intent intent = new Intent();
                intent.putExtra("key_refresh", true);
                setResult(RESULT_OK, intent);
                sendEmptyMessageDelayed(MSG_CREATE_OEDER, 3000);
            } else {
                ToastExt.showExt(data.getMessage());
            }
        } else {
            ToastExt.showExt("充电失败,请重新尝试");
        }
    }

    @Override
    public void onLoadChargingInfo(BaseEntity<ChargeStakeOrderInfoEntity> data) {
        NOWSECOND++;
        if (NOWSECOND == MAXSECOND) {
            ToastExt.showExt("请求超时,请更换插座再试");
            NOWSECOND = 0;
        }
        if (data != null) {
            ChargeStakeOrderInfoEntity mOrderInfoEntity = data.getData();
            if (mOrderInfoEntity != null) {
                String orderId = mOrderInfoEntity.getOrderId();
                if (!TextUtils.isEmpty(orderId)) {
                    getBaseHandler().removeMessages(MSG_CREATE_OEDER);
                    finish();
                }
            }
        }

    }
}
