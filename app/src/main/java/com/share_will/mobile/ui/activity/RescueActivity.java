package com.share_will.mobile.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.share_will.mobile.App;
import com.share_will.mobile.R;
import com.share_will.mobile.model.entity.RescueEntity;
import com.share_will.mobile.presenter.RescueListPresenter;
import com.share_will.mobile.ui.adapter.RescueListAdapter;
import com.share_will.mobile.ui.views.RescueListView;
import com.ubock.library.base.BaseEntity;
import com.ubock.library.base.BaseFragmentActivity;

import java.util.List;

public class RescueActivity extends BaseFragmentActivity<RescueListPresenter> implements RescueListView,
        BaseQuickAdapter.OnItemClickListener {

    private RecyclerView mRescueListView;
    private RescueListAdapter mRescueListAdapter;
    private SwipeRefreshLayout mRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("救援记录");
        initData();

    }

    private void initData() {
        getPresenter().getRescueList(App.getInstance().getUserId());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_rescue;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setTopText("申请救援");
        mRescueListView = findViewById(R.id.rv_rescue_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRescueListView.setLayoutManager(layoutManager);
        mRescueListAdapter = new RescueListAdapter(this);
        mRescueListView.setAdapter(mRescueListAdapter);
        mRescueListAdapter.setOnItemClickListener(this);
        mRefreshLayout = findViewById(R.id.refresh_rescue);
        mRefreshLayout.setOnRefreshListener(this::initData);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getPresenter().getRescueList(App.getInstance().getUserId());
    }


    @Override
    public void onClickTopRightMenuText(View view) {
        super.onClickTopRightMenu(view);
        startActivity(new Intent(this, NewRescueActivity.class));
    }

    @Override
    public void onLoadRescueList(BaseEntity<List<RescueEntity>> ret) {
        if (ret != null && ret.getData() != null) {
            mRescueListAdapter.setLoadMoreData(getPresenter().getModel().getRescue(), true);
        }
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onCancelRescue(BaseEntity<Object> ret) {

    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Intent intent = new Intent(this, RescueDetailActivity.class);
        RescueEntity rescueEntity = getPresenter().getModel().getRescue(position);
        intent.putExtra("entity", rescueEntity);
        startActivity(intent);
    }
}
