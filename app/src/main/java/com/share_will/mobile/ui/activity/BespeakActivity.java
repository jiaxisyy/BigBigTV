package com.share_will.mobile.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.share_will.mobile.App;
import com.share_will.mobile.R;
import com.share_will.mobile.listener.DetachDialogClickListener;
import com.share_will.mobile.model.entity.BespeakEntity;
import com.share_will.mobile.presenter.BespeakPresenter;
import com.share_will.mobile.ui.adapter.BespeakAdapter;
import com.share_will.mobile.ui.fragment.ExchangeFragment;
import com.share_will.mobile.ui.views.BespeakView;
import com.ubock.library.base.BaseEntity;
import com.ubock.library.base.BaseFragmentActivity;
import com.ubock.library.ui.dialog.ToastExt;
import com.ubock.library.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

public class BespeakActivity extends BaseFragmentActivity<BespeakPresenter> implements BespeakView, View.OnClickListener, DialogInterface.OnClickListener {
    private RecyclerView mRv;
    private String[] catalogs = new String[]{"预约状态:", "站点名称:", "机柜SN:", "舱门号:", "电池SN:", "站点地址:", "预约时间:", "描述:"};

    private LinearLayout mSubmitRespeak;
    /**
     * 预约状态描述
     */
    private TextView mBespeakDesc;
    /**
     * 预约状态
     */
    private String mStatus;
    /**
     * 判断是否预约中,默认false
     */
    private boolean mBespeakFlag = false;
    private List<BespeakEntity> entities;
    private BespeakAdapter bespeakAdapter;
    private String cabinetSn;
    /**
     * 添加预约后返回的id
     */
    private int id;
    private DetachDialogClickListener mDetachClickListener = DetachDialogClickListener.wrap(this);
    private AlertDialog mAlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void getParameterForMap() {
        String cabinetTitle = getIntent().getStringExtra(ExchangeFragment.CABINETTITLE);
        cabinetSn = getIntent().getStringExtra(ExchangeFragment.CABINETSN);
        String cabinetAddress = getIntent().getStringExtra(ExchangeFragment.CABINETADDRESS);
        int fullNum = getIntent().getIntExtra(ExchangeFragment.FULLNUM, 0);
        entities.add(new BespeakEntity("站点名称:", cabinetTitle));
        entities.add(new BespeakEntity("机柜SN:", cabinetSn));
        entities.add(new BespeakEntity("可预约数量:", String.valueOf(fullNum)));
        entities.add(new BespeakEntity("站点地址:", cabinetAddress));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_bespeak;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {


        setTitle("预约换电");
        mRv = findViewById(R.id.rv_bespeak);
        mSubmitRespeak = findViewById(R.id.ll_submitBespeak);
        mBespeakDesc = findViewById(R.id.tv_bespeakDesc);
        mSubmitRespeak.setOnClickListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRv.setLayoutManager(layoutManager);
        String activityStr = getIntent().getStringExtra("fromActivity");
        entities = new ArrayList<>();
        if (activityStr != null) {
            bespeakAdapter = new BespeakAdapter(R.layout.item_bespeak, entities);
            mRv.setAdapter(bespeakAdapter);
            showBespeakInfo();
        } else {
            getParameterForMap();
            mSubmitRespeak.setVisibility(View.VISIBLE);
            bespeakAdapter = new BespeakAdapter(R.layout.item_bespeak, entities);
            mRv.setAdapter(bespeakAdapter);
        }
    }

    @Override
    public void onBespeakInfoResult(BaseEntity<BespeakEntity.DataBean> dataBean) {

        //status：0预约中1预约成功待取电2已取电3取消预约
        mBespeakDesc.setText("取消预约");
        mBespeakFlag = true;
        BespeakEntity.DataBean data = dataBean.getData();
        id = data.getId();
        switch (data.getStatus()) {
            case 0:
                mStatus = "预约中";
                mSubmitRespeak.setVisibility(View.VISIBLE);
                break;
            case 1:
                mStatus = "预约成功待取电";
                mSubmitRespeak.setVisibility(View.VISIBLE);
                break;
            case 2:
                mStatus = "已取电";
                mSubmitRespeak.setVisibility(View.GONE);
                break;
            case 3:
                mStatus = "取消预约";
                mSubmitRespeak.setVisibility(View.GONE);
                break;
        }

        int length = catalogs.length;
        String[] results = new String[]{mStatus,
                data.getStationName(),
                data.getCabinetId(),
                String.valueOf(data.getDoor()),
                data.getBatteryId(),
                data.getAddress(),
                DateUtils.unixToLocalTime(String.valueOf(data.getCreateTime())),
                data.getDesc()
        };

        List<BespeakEntity> bespeakEntities = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            bespeakEntities.add(new BespeakEntity(catalogs[i], results[i]));
        }
        bespeakAdapter.setNewData(bespeakEntities);
    }

    @Override
    public void onAddBespeakResult(BaseEntity<BespeakEntity.AddDataBean> addDataBean) {
        showBespeakInfo();
    }

    private void showBespeakInfo() {
        getPresenter().getBespeakInfo(App.getInstance().getUserId());
    }

    @Override
    public void onFailedMessage(String message) {
        ToastExt.showExt(message);
    }

    /**
     * 取消预约成功
     */
    @Override
    public void onUpdateBespeakResult() {

        //刷新界面
        showBespeakInfo();
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.ll_submitBespeak) {
            if (!mBespeakFlag) {
                //提交预约
                getPresenter().addBespeak(App.getInstance().getUserId(), cabinetSn);
            } else {
                //取消预约
                cancel();
            }

        }
    }




    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        if (i == DialogInterface.BUTTON_POSITIVE) {
            getPresenter().updateBespeak(String.valueOf(id), App.getInstance().getUserId(), String.valueOf(3), "用户取消预约");
        }
        mAlertDialog.dismiss();
    }


    private void cancel() {
        mAlertDialog = new AlertDialog.Builder(this)
                .setTitle("取消操作")
                .setIcon(null)
                .setCancelable(true)
                .setPositiveButton(R.string.alert_yes_button, mDetachClickListener)
                .setNegativeButton(R.string.alert_no_button, mDetachClickListener).create();
        mAlertDialog.setCanceledOnTouchOutside(false);
        mAlertDialog.setMessage("确认要取消预约吗?");
        mAlertDialog.show();

    }

}
