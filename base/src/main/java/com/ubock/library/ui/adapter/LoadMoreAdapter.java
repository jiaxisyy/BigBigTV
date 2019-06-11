package com.ubock.library.ui.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * 有分页功能的适配器
 * Created by ChenGD on 2017-12-21.
 */
public abstract class LoadMoreAdapter<D, H extends BaseViewHolder> extends BaseQuickAdapter<D, H> {
    Context mContext;

    //加载状态
    public final static int STATUS_LOAD = 0;//还有下一页
    public final static int STATUS_ERR = 1;//加载失败状态
    public final static int STATUS_FINISHED = 2;//加载完成
    private int mStatus = STATUS_LOAD;//当前状态
    private int mCurrentPage = 1;//当前页码
    private int mPageSize = 25;//每页数量
    private boolean mHideNoMoreView = false;//加载完成后是否隐藏无更多提示

    public interface OnLoadMoreListener {
        void onLoadMore(int currentPage);
    }

    private OnLoadMoreListener mOnLoadMoreListener;

    public LoadMoreAdapter(Context context, @LayoutRes int layoutResId) {
        super(layoutResId);
        mContext = context;

    }

    public LoadMoreAdapter(@Nullable List<D> data, Context mContext) {
        super(data);
        this.mContext = mContext;
    }

    public int getLoadMoreStatus() {
        return mStatus;
    }

    public int getCurrentPage() {
        return mCurrentPage;
    }

    public void setPageSize(int pageSize) {
        mPageSize = pageSize;
    }

    /**
     * 设置每一页的条数
     *
     * @return
     */
    public int getPageSize() {
        return mPageSize;
    }

    /**
     * 加载完成后是否显示无更多提示
     *
     * @param hide
     */
    public void hideNoMoreView(boolean hide) {
        mHideNoMoreView = hide;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        setOnLoadMoreListener(mLoadMoreListener, recyclerView);
    }

    public void setOnLoadMoreListener(OnLoadMoreListener listener) {
        mOnLoadMoreListener = listener;
    }

    RequestLoadMoreListener mLoadMoreListener = new RequestLoadMoreListener() {
        @Override
        public void onLoadMoreRequested() {
            if (mOnLoadMoreListener != null) {
                mCurrentPage++;
                mOnLoadMoreListener.onLoadMore(getCurrentPage());
            }
        }
    };

    public void setLoadMoreData(List<D> data) {
        setLoadMoreData(data, false);
    }

    public void setLoadMoreData(List<D> data, boolean reset) {
        if (data != null) {
            if (reset) {//重置
                mCurrentPage = 1;
            }
            if (mCurrentPage == 1) {
                setNewData(data);
            } else {
                addData(data);
            }
            if (data.size() < mPageSize) {
                mStatus = STATUS_FINISHED;
                loadMoreEnd(mHideNoMoreView);
            } else {
                setEnableLoadMore(true);
                loadMoreComplete();
            }
        } else {
            mStatus = STATUS_ERR;
            if (reset) {
                mCurrentPage = 1;
                setNewData(data);
            } else {
                loadMoreFail();
            }
        }
    }

}
