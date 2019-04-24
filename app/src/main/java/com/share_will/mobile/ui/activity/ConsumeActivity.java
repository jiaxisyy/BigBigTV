package com.share_will.mobile.ui.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.share_will.mobile.App;
import com.share_will.mobile.R;
import com.share_will.mobile.model.entity.RecordEntity;
import com.share_will.mobile.presenter.ConsumePresenter;
import com.share_will.mobile.ui.adapter.RecordAdapter;
import com.share_will.mobile.ui.views.ConsumeView;
import com.ubock.library.base.BaseEntity;
import com.ubock.library.base.BaseFragmentActivity;
import com.ubock.library.ui.adapter.LoadMoreAdapter;

import java.util.List;

/**
 * @author chenguandu
 * 功能描述：消费记录
 */
public class ConsumeActivity extends BaseFragmentActivity<ConsumePresenter> implements ConsumeView, LoadMoreAdapter.OnLoadMoreListener {

    private RecordAdapter mRecordAdapter;

    private RecyclerView mRecordList;
    private SwipeRefreshLayout mRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("消费记录");
        loadByPage(1);
    }

    private void loadByPage(int currentPage) {
        getPresenter().getConsumeList(App.getInstance().getUserId(), currentPage, 20);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.consume_activity;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mRecordList = findViewById(R.id.mention_lv_atme);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mRecordList.setLayoutManager(manager);
        mRecordAdapter = new RecordAdapter(this);
        mRecordAdapter.setPageSize(20);
        mRecordAdapter.setEnableLoadMore(true);
        mRecordList.setAdapter(mRecordAdapter);
        mRecordAdapter.setEmptyView(R.layout.empty_view);
        mRecordAdapter.setOnLoadMoreListener(this);
        mRefreshLayout = findViewById(R.id.refresh_consume);
        mRefreshLayout.setOnRefreshListener(() -> loadByPage(1));

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onLoadConsumeList(BaseEntity<List<RecordEntity>> data) {
        if (data != null && data.getCode() == 0) {
            mRecordAdapter.setLoadMoreData(data.getData());
        }else {
           showMessage(data.getMessage());
        }
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onLoadMore(int currentPage) {
        loadByPage(currentPage);
    }
}
