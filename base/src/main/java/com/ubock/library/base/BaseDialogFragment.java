package com.ubock.library.base;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.trello.rxlifecycle.components.support.RxAppCompatDialogFragment;
import com.ubock.library.R;
import com.ubock.library.annotation.Presenter;
import com.ubock.library.subscriptionlifecycle.ActivitySubscriptionManager;
import com.ubock.library.subscriptionlifecycle.SubscriptionHelper;
import com.ubock.library.subscriptionlifecycle.SubscriptionManager;
import com.ubock.library.ui.dialog.Loading;
import com.ubock.library.ui.dialog.ToastExt;
import com.ubock.library.utils.PresenterUtils;

import rx.Subscription;

/**
 * Created by ChenGD on 2017-3-30.
 */

public abstract class BaseDialogFragment<P extends BasePresenter> extends RxAppCompatDialogFragment implements BaseView, ActivitySubscriptionManager {
    final String TAG = this.getClass().getSimpleName();
    private Loading mLoading;
    private P mPresenter;
    private int mGravity = 0;
    private SubscriptionHelper mSubscriptionHelper;
    private PresenterUtils mPresenterUtils;

    public interface OnCreatedViewListener{
        void onCreatedView(View view, @Nullable Bundle savedInstanceState);
    }
    private OnCreatedViewListener mOnCreatedViewListener;
    public void setOnCreatedViewListener(OnCreatedViewListener listener){
        mOnCreatedViewListener = listener;
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

    /**
     * 使用databinding时要覆盖此方法,并且返回true,并且覆盖initBinding方法
     *
     * @return
     */
    protected boolean useDatabinding() {
        return false;
    }

    protected void initBinding(ViewDataBinding binding) {
    }

    protected abstract int getLayoutId();

    protected abstract void initView(View view, @Nullable Bundle savedInstanceState);

    public void show(@NonNull BaseFragmentActivity ownerActivity) {
        show(ownerActivity.getSupportFragmentManager(), TAG);
    }

    public void show(@NonNull BaseFragment fragment) {
        show(fragment.getChildFragmentManager(), TAG);
    }

    public void show(@NonNull BaseDialogFragment fragment) {
        show(fragment.getChildFragmentManager(), TAG);
    }

    public void show(FragmentManager manager) {
        show(manager, TAG);
    }

    /**
     * 覆盖此方法设置弹出框的宽度
     *
     * @return
     */
    protected int initWidth() {
        return 0;
    }

    /**
     * 覆盖此方法设置弹出框的高度
     *
     * @return
     */
    protected int initHeight() {
        return 0;
    }

    /**
     * 设置style
     * @return
     */
    protected int initStyle(){
        return 0;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        //=====================解决内存泄漏问题========================
        boolean isShow = this.getShowsDialog();
        this.setShowsDialog(false);
        super.onActivityCreated(savedInstanceState);
        this.setShowsDialog(isShow);

        View view = getView();
        if (view != null){
            if (view.getParent() != null){
                throw new IllegalStateException(
                        "DialogFragment can not be attached to a container view");
            }
            this.getDialog().setContentView(view);
        }
        final Activity activity = getActivity();
        if (activity != null){
            this.getDialog().setOwnerActivity(activity);
        }
        this.getDialog().setCancelable(this.isCancelable());
        if (savedInstanceState != null){
            Bundle dialogState = savedInstanceState.getBundle("android:savedDialogState");
            if (dialogState != null){
                this.getDialog().onRestoreInstanceState(dialogState);
            }
        }

        //==========================解决内存泄漏问题结束===========================

        boolean change = false;
        WindowManager.LayoutParams attributes = getDialog().getWindow().getAttributes();
        mGravity = initGravity();
        if (initWidth() != 0) {
            attributes.width = initWidth();
            change = true;
        }
        if (initHeight() != 0) {
            attributes.height = initHeight();
            change = true;
        }
        if (mGravity != 0) {
            attributes.gravity = mGravity != -1 ? mGravity : Gravity.CENTER;
        }
        if (change) {
            getDialog().getWindow().setAttributes(attributes);
        }
    }

    protected int initGravity() {
        return 0;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view;
        if (useDatabinding()) {
            ViewDataBinding binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
            initBinding(binding);
            view = binding.getRoot();
        } else {
            view = inflater.inflate(getLayoutId(), container, false);
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view, savedInstanceState);
        if (mOnCreatedViewListener != null){
            mOnCreatedViewListener.onCreatedView(view, savedInstanceState);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void initPresenter(){
        mPresenterUtils = new PresenterUtils(this, this);
        mPresenter = mPresenterUtils.getPresenter(0);
        P p = (P) mPresenterUtils.createPresenterByGenericSuperclass(this.getClass(), this);
        if (p != null && p != mPresenter){
            mPresenter = p;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        mSubscriptionHelper = new SubscriptionHelper();
        initPresenter();
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, initStyle()==0? R.style.BaseDialogStyle:initStyle());
    }

    @Override
    public void showLoading() {
        showLoading(null);
    }

    public void showLoading(int message) {
        showLoading(getResources().getString(message));
    }

    @Override
    public void showLoading(String message) {
        if (mLoading == null) {
            mLoading = new Loading(getActivity());
        }
        mLoading.show();
        mLoading.setMessage(message);
    }

    @Override
    public void hideLoading() {
        if (mLoading != null && mLoading.isShowing()) {
            mLoading.dismiss();
        }
    }

    @Override
    public void showMessage(String message) {
        ToastExt.showExt(message);
    }

    @Override
    public void onStart() {
        super.onStart();
        mSubscriptionHelper.unSubscribe(ActivitySubscriptionManager.onStart);
        if (mPresenter != null) {
            mPresenter.onStart();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mSubscriptionHelper.unSubscribe(ActivitySubscriptionManager.onResume);
        if (mPresenter != null) {
            mPresenter.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mPresenter != null) {
            mPresenter.onPause();
        }
        mSubscriptionHelper.unSubscribe(ActivitySubscriptionManager.onPause);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mPresenter != null) {
            mPresenter.onStop();
        }
        mSubscriptionHelper.unSubscribe(ActivitySubscriptionManager.onStop);
    }

    @Override
    public void onDestroy() {
        if (mSubscriptionHelper != null) {
            mSubscriptionHelper.unSubscribe(SubscriptionManager.onDestroy);
            mSubscriptionHelper = null;
        }
        if (mPresenter != null) {
            mPresenter.onDestroy();//释放资源
            mPresenter = null;
        }
        if (mLoading != null && mLoading.isShowing()){
            mLoading.dismiss();
            mLoading = null;
        }
        if (mPresenterUtils != null){
            mPresenterUtils.release();
            mPresenterUtils = null;
        }
        super.onDestroy();
    }

    @Override
    final public void subscribe(Subscription subscription) {
        mSubscriptionHelper.subscribe(subscription);
    }

    @Override
    final public void subscribe(Subscription subscription, int lifeCycle) {
        if (lifeCycle == ActivitySubscriptionManager.onRestart){
            throw new IllegalStateException("Fragment can't use onRestart lifecycle");
        }
        mSubscriptionHelper.subscribe(subscription, lifeCycle);
    }

    @Override
    final public void unSubscribe(Subscription subscription) {
        mSubscriptionHelper.unSubscribe(subscription);
    }
}
