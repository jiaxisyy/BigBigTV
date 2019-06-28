package com.share_will.mobile.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.share_will.mobile.App;
import com.share_will.mobile.R;
import com.share_will.mobile.model.entity.ChargeRecordEntity;
import com.share_will.mobile.presenter.ChargeRecordPresenter;
import com.share_will.mobile.ui.adapter.ChargeRecordAdapter;
import com.share_will.mobile.ui.views.IChargeRecordView;
import com.share_will.mobile.ui.widget.RecyclerViewItemDecoration;
import com.ubock.library.base.BaseEntity;
import com.ubock.library.base.BaseFragmentActivity;
import com.ubock.library.ui.adapter.LoadMoreAdapter;
import com.ubock.library.utils.LogUtils;

import java.util.List;

public class ChargeRecordActivity extends BaseFragmentActivity<ChargeRecordPresenter> implements IChargeRecordView, LoadMoreAdapter.OnLoadMoreListener {

    private RecyclerView mRv;
    private ChargeRecordAdapter mAdapter;
    private View mNoDataView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_charge_record;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setTitle("充电记录");
        mRv = findViewById(R.id.rv_charge_record);
        mNoDataView = findViewById(R.id.no_data);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRv.addItemDecoration(new RecyclerViewItemDecoration(1, Color.parseColor("#F6F6F6")));
        mRv.setLayoutManager(linearLayoutManager);
        mAdapter = new ChargeRecordAdapter(this, R.layout.item_charge_record);
        mAdapter.setEnableLoadMore(true);
        mRv.setAdapter(mAdapter);
        mAdapter.setEmptyView(R.layout.empty_view);
        mAdapter.setOnLoadMoreListener(this);
        loadByPage(1);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(ChargeRecordActivity.this, ChargeRecordDetailActivity.class);
                intent.putExtra("data", mAdapter.getData().get(position));
                startActivity(intent);
            }
        });

    }

    @Override
    public void onLoadChargeRecordResult(BaseEntity<List<ChargeRecordEntity>> data) {
        if (data != null) {
            mNoDataView.setVisibility(View.INVISIBLE);
            mAdapter.setLoadMoreData(data.getData());
            LogUtils.d("当前size=" + mAdapter.getData().size());
        } else {
            mNoDataView.setVisibility(View.VISIBLE);
        }
    }


    private void loadByPage(int currentPage) {
        getPresenter().getChargeRecordList(App.getInstance().getUserId(), currentPage, 25);
    }

    @Override
    public void onLoadMore(int currentPage) {
        currentPage++;
        loadByPage(currentPage);
    }
}
