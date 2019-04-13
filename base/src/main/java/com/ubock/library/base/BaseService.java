package com.ubock.library.base;

import android.app.Service;

import com.ubock.library.annotation.Presenter;
import com.ubock.library.subscriptionlifecycle.SubscriptionHelper;
import com.ubock.library.subscriptionlifecycle.SubscriptionManager;
import com.ubock.library.ui.dialog.ToastExt;
import com.ubock.library.utils.PresenterUtils;

import rx.Subscription;

/**
 * Created by ChenGD on 2018/6/8.
 *
 * @author chenguandu
 */

public abstract class BaseService<P extends BasePresenter> extends Service
        implements BaseView, SubscriptionManager {

    protected final String TAG = this.getClass().getSimpleName();
    private P mPresenter;

    private SubscriptionHelper mSubscriptionHelper = new SubscriptionHelper();
    private PresenterUtils mPresenterUtils;

    private void initPresenter(){
        mPresenterUtils = new PresenterUtils(this, this);
        mPresenter = mPresenterUtils.getPresenter(0);
        P p = (P) mPresenterUtils.createPresenterByGenericSuperclass(this.getClass(), this);
        if (p != null && p != mPresenter){
            mPresenter = p;
        }
    }

    @Override
    public void onCreate() {
        initPresenter();
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        mSubscriptionHelper.unSubscribe(SubscriptionManager.onDestroy);
        if (mPresenter != null) {
            mPresenter.onDestroy();//释放资源
            mPresenter = null;
        }
        if (mPresenterUtils != null){
            mPresenterUtils.release();
            mPresenterUtils = null;
        }
        super.onDestroy();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void showLoading(String message) {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(String message) {
        ToastExt.showExt(message);
    }

    public P getPresenter() {
        return mPresenter;
    }

    public <T extends BasePresenter> T getPresenter(Class<T> clazz) {
        return mPresenterUtils.getPresenter(clazz);
    }

    /**
     * 根据类注解中的索引来获取presenter
     * <br/>
     * 注意：此方法只能获取@{@link Presenter}注解中的presenter
     * @param index 索引从0开始
     * @param <T> 返回的presenter类型
     * @return
     */
    public <T extends BasePresenter> T getPresenter(int index) {
        return mPresenterUtils.getPresenter(index);
    }

    @Override
    final public void subscribe(Subscription subscription) {
        mSubscriptionHelper.subscribe(subscription);
    }

    @Override
    final public void subscribe(Subscription subscription, int lifeCycle) {
        mSubscriptionHelper.subscribe(subscription, lifeCycle);
    }

    @Override
    final public void unSubscribe(Subscription subscription) {
        mSubscriptionHelper.unSubscribe(subscription);
    }
}
