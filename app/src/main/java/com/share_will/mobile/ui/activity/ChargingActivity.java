package com.share_will.mobile.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.share_will.mobile.App;
import com.share_will.mobile.R;
import com.share_will.mobile.model.entity.ChargingEntity;
import com.share_will.mobile.presenter.ChargingPresenter;
import com.share_will.mobile.ui.views.ChargingView;
import com.ubock.library.base.BaseEntity;
import com.ubock.library.base.BaseFragmentActivity;
import com.ubock.library.ui.dialog.ToastExt;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class ChargingActivity extends BaseFragmentActivity<ChargingPresenter> implements ChargingView {

    private RecyclerView mRvChargingList;
    private ChargingAdapter mChargingAdapter;
    private RecyclerView mRvChannelList;
    private ChannelAdapter mChannelAdapter;
    private String mSN;
    private int mChannel;
    private List<ChargingEntity> mChargingData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSN = getIntent().getStringExtra("sn");
        mChannel = getIntent().getIntExtra("channel", 0);
        initChargingList();
        initChannel();
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mRvChargingList = findViewById(R.id.rv_charging_list);
        mRvChannelList = findViewById(R.id.rv_charging_channel);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_charging;
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        getChargingList();
    }

    private void initChargingList(){
        mChargingAdapter = new ChargingAdapter(this, mChargingData);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        mRvChargingList.setLayoutManager(layoutManager);
        mRvChargingList.setAdapter(mChargingAdapter);
        mChargingAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                mChargingAdapter.select(position);
            }
        });
    }

    private void initChannel(){
        ArrayList<Integer> data = new ArrayList<>();
        for (int i = 0; i < mChannel; i++){
            data.add(i+1);
        }
        mChannelAdapter = new ChannelAdapter(this, data);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        mRvChannelList.setLayoutManager(layoutManager);
        mRvChannelList.setAdapter(mChannelAdapter);
        mChannelAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                mChannelAdapter.select(position);
            }
        });
    }

    public void submit(View view){
        if (mChannelAdapter.getIndex() == -1 || mChargingAdapter.getIndex() == -1){
            Toast.makeText(this, "请选择充电时间和充电通道", Toast.LENGTH_LONG).show();
            return;
        }
        createOrder();
    }

    @Override
    public void onCreateChargingOrder(BaseEntity<Object> data) {
        if (data != null){
            if (data.getCode() == 0) {
                ToastExt.showExt("支付成功，请开始充电");
            } else {
                ToastExt.showExt(data.getMessage());
            }
        } else {
            ToastExt.showExt("创建订单失败");
        }
    }

    /**
     * 生成订单并支付
     */
    private void createOrder(){
        getPresenter().createChargingOrder(mSN, App.getInstance().getUserId(), mChannelAdapter.getItem(), mChargingAdapter.getItem().getId());
    }

    @Override
    public void onLoadChargingList(BaseEntity<List<ChargingEntity>> data) {
        mChargingData.clear();
        if (data != null && data.getCode() ==0 && data.getData() != null){
            mChargingData.addAll(data.getData());
        }
        mChargingAdapter.notifyDataSetChanged();
    }

    /**
     * 加载充电选项：时间、价格
     */
    private void getChargingList(){
        getPresenter().getChargingList(mSN);
    }

    static class ChargingAdapter extends BaseQuickAdapter<ChargingEntity, BaseViewHolder>{
        private int mIndex = -1;
        public void select(int position){
            mIndex = position;
            notifyDataSetChanged();
        }

        public int getIndex(){
            return mIndex;
        }

        public ChargingEntity getItem(){
            return getItem(mIndex);
        }

        public ChargingAdapter(Context context, List<ChargingEntity> data) {
            super(R.layout.charging_item, data);
        }

        private String changeTime(long time){
            StringBuilder sb = new StringBuilder();
            long h = time / 60;
            long m = time % 60;
            if (h > 0){
                sb.append(h);
                sb.append("小时");
            }
            if (m > 0){
                sb.append(m);
                sb.append("分钟");
            }
            return sb.toString();
        }

        private String changeMony(long money){
            String price = NumberFormat.getInstance().format(money/100f);
            return String.format("%s元", price);
        }

        @Override
        protected void convert(BaseViewHolder helper, ChargingEntity item) {

            helper.setText(R.id.minute, changeTime(item.getMinute()));
            helper.setText(R.id.price, changeMony(item.getMoney()));
            if (helper.getAdapterPosition() == mIndex){
                helper.setBackgroundColor(R.id.container, 0xff777777);
            } else {
                helper.setBackgroundColor(R.id.container, 0xffffffff);
            }
        }
    }

    static class ChannelAdapter extends BaseQuickAdapter<Integer, BaseViewHolder>{
        private int mIndex = -1;
        public void select(int position){
            mIndex = position;
            notifyDataSetChanged();
        }

        public int getIndex(){
            return mIndex;
        }

        public Integer getItem(){
            return getItem(mIndex);
        }

        public ChannelAdapter(Context context, List<Integer> data) {
            super(R.layout.channel_item, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, Integer item) {
            helper.setText(R.id.tv_channel, "通道" + item);
            if (helper.getAdapterPosition() == mIndex){
                helper.setBackgroundColor(R.id.tv_channel, 0xff777777);
            } else {
                helper.setBackgroundColor(R.id.tv_channel, 0xffffffff);
            }
        }
    }
}
