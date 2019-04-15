package com.share_will.mobile.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.share_will.mobile.R;
import com.share_will.mobile.model.entity.OrderDetailsEntity;
import com.share_will.mobile.ui.adapter.HomeAlarmAdapter;
import com.share_will.mobile.ui.adapter.OrderAdapter;
import com.share_will.mobile.ui.widget.RecyclerViewItemDecoration;
import com.ubock.library.base.BaseFragmentActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的订单
 */

public class OrderActivity extends BaseFragmentActivity {

    private RecyclerView mRv;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_order;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setTitle("我的订单");
        mRv = findViewById(R.id.rv_my_order);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mRv.setLayoutManager(manager);
        List<OrderDetailsEntity> list = new ArrayList<>();
        list.add(new OrderDetailsEntity("张三"));
        list.add(new OrderDetailsEntity("李四"));
        list.add(new OrderDetailsEntity("王麻子"));
        OrderAdapter mAdapter = new OrderAdapter(R.layout.item_my_order, list);
        mRv.setAdapter(mAdapter);
    }
}
