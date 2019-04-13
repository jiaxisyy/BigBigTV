package com.ubock.library.base;

import android.support.annotation.CallSuper;

import com.ubock.library.http.HttpExceptionHandle;
import com.ubock.library.subscriptionlifecycle.SubscriptionManager;
import com.ubock.library.utils.LogUtils;

import rx.Subscriber;

/**
 * Created by ChenGD on 2017/3/13.
 */

public abstract class BaseNetSubscriber<T> extends Subscriber<T> {
    protected BasePresenter mPresenter;
    private boolean showLoading = true;

    private BaseNetSubscriber() {
    }

    public BaseNetSubscriber(BasePresenter presenter) {
        this(presenter, SubscriptionManager.onDestroy);
    }

    public BaseNetSubscriber(BasePresenter presenter, boolean showLoading) {
        this(presenter, SubscriptionManager.onDestroy);
        this.showLoading = showLoading;
    }

    public BaseNetSubscriber(BasePresenter presenter, int lifeCycle) {
        this.mPresenter = presenter;
        if (mPresenter != null && mPresenter.getView() != null && mPresenter.getView() instanceof SubscriptionManager) {
            ((SubscriptionManager) mPresenter.getView()).subscribe(this, lifeCycle);
        }
    }

    /**
     * 是否显示加载框,默认显示加载框
     *
     * @return
     */
    protected boolean showLoading() {
        return showLoading;
    }

    /**
     * 网络请求回来后是否关闭加载框,用在多个网络请求嵌套时可以一直显示加载框,默认关闭
     *
     * @return
     */
    protected boolean closeLoading(){
        return true;
    }

    @CallSuper
    @Override
    public void onStart() {
        if (showLoading() && mPresenter != null && mPresenter.getView() != null) {
            mPresenter.getView().showLoading();
        }
    }

    @CallSuper
    @Override
    public void onCompleted() {
        if (mPresenter != null && mPresenter.getView() != null) {
            if (showLoading() && closeLoading()) {
                mPresenter.getView().hideLoading();
            }
            if (mPresenter.getView() instanceof SubscriptionManager) {
                ((SubscriptionManager) mPresenter.getView()).unSubscribe(this);
            }
        }
    }

    @Override
    final public void onError(Throwable e) {
        LogUtils.e("OkHttp", e);
        if (mPresenter != null && mPresenter.getView() != null) {
            if (showLoading()) {
                mPresenter.getView().hideLoading();
            }
            if (mPresenter.getView() instanceof SubscriptionManager) {
                ((SubscriptionManager) mPresenter.getView()).unSubscribe(this);
            }
        }

        //此处理统一作默认错误处理
        if (!onErr(e)){
            HttpExceptionHandle.handleException(e);
        }
    }

    /**
     * 错误处理
     * @param e
     * @return 返回true则表示错误已经被处理掉
     */
    public boolean onErr(Throwable e){
        LogUtils.e(e);
        return false;
    }
}
