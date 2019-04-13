package com.ubock.library.base;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.leakcanary.RefWatcher;
import com.trello.rxlifecycle.components.support.RxFragment;
import com.ubock.library.R;
import com.ubock.library.annotation.Presenter;
import com.ubock.library.subscriptionlifecycle.FragmentSubscriptionManager;
import com.ubock.library.subscriptionlifecycle.SubscriptionHelper;
import com.ubock.library.ui.dialog.Loading;
import com.ubock.library.ui.dialog.ToastExt;
import com.ubock.library.utils.PresenterUtils;

import rx.Subscription;

public abstract class BaseFragment<P extends BasePresenter> extends RxFragment implements BaseView, FragmentSubscriptionManager {
    private Loading mLoading;

    private P mPresenter;
    private SubscriptionHelper mSubscriptionHelper = new SubscriptionHelper();
    private PresenterUtils mPresenterUtils;
    private TextView mTitle;
    private ImageView mBack;
    protected ImageButton mTopRightMenu;
    private RelativeLayout rlTopbar;

    private void initPresenter() {
        mPresenterUtils = new PresenterUtils(this, this);
        mPresenter = mPresenterUtils.getPresenter(0);
        P p = (P) mPresenterUtils.createPresenterByGenericSuperclass(this.getClass(), this);
        if (p != null && p != mPresenter) {
            mPresenter = p;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        initPresenter();
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view, savedInstanceState);
    }

    public void setTitle(String title) {
        if (mTitle != null) {
            mTitle.setText(title);
        }
    }

    public void setTitleTextColor(int color) {
        if (mTitle != null) {
            mTitle.setTextColor(color);
        }
    }

    public void setTopBackground(int color) {
        if (mTitle != null) {

        }
    }


    /**
     * 显示返回按钮
     *
     * @param show
     */
    public void showBackMenu(boolean show) {
        if (mBack != null) {
            mBack.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * 显示右上角菜单按钮
     *
     * @param show
     */
    public void showTopRightMenu(boolean show) {
        if (mTopRightMenu != null) {
            mTopRightMenu.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * 使用databinding时要覆盖此方法,并且返回true,并且覆盖initBinding方法
     *
     * @return
     */
    protected boolean useDatabinding() {
        return false;
    }

    protected void initBinding(ViewDataBinding binding, Bundle savedInstanceState) {
    }

    protected abstract int getLayoutId();

    protected abstract void initView(View view, Bundle savedInstanceState);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view;
        if (useDatabinding()) {
            ViewDataBinding binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
            initBinding(binding, savedInstanceState);
            view = binding.getRoot();
        } else {
            view = inflater.inflate(getLayoutId(), container, false);
        }
        mTitle = view.findViewById(R.id.toolbar_title);
        mBack = view.findViewById(R.id.top_back);
        mTopRightMenu = view.findViewById(R.id.btn_top_right_menu);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onStart() {
        super.onStart();
        mSubscriptionHelper.unSubscribe(FragmentSubscriptionManager.onStart);
        if (mPresenter != null) {
            mPresenter.onStart();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mSubscriptionHelper.unSubscribe(FragmentSubscriptionManager.onResume);
        if (mPresenter != null) {
            mPresenter.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mSubscriptionHelper.unSubscribe(FragmentSubscriptionManager.onPause);
        if (mPresenter != null) {
            mPresenter.onPause();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        mSubscriptionHelper.unSubscribe(FragmentSubscriptionManager.onStop);
        if (mPresenter != null) {
            mPresenter.onStop();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSubscriptionHelper.unSubscribe(FragmentSubscriptionManager.onDestroy);
        if (mPresenter != null) {
            mPresenter.onDestroy();//释放资源
            mPresenter = null;
        }
        if (mLoading != null && mLoading.isShowing()) {
            mLoading.dismiss();
            mLoading = null;
        }
        if (mPresenterUtils != null) {
            mPresenterUtils.release();
            mPresenterUtils = null;
        }
        RefWatcher refWatcher = BaseApp.getInstance().getRefWatcher(getActivity());
        refWatcher.watch(this);
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            mSubscriptionHelper.unSubscribe(FragmentSubscriptionManager.onUserVisibleHint);
        } else {
            hideLoading();
            mSubscriptionHelper.unSubscribe(FragmentSubscriptionManager.onUserUnVisibleHint);
        }
    }


    @Override
    final public void subscribe(Subscription subscription) {
        mSubscriptionHelper.subscribe(subscription);
    }

    @Override
    final public void subscribe(Subscription subscription, int lifeCycle) {
        if (lifeCycle == FragmentSubscriptionManager.onRestart) {
            throw new IllegalStateException("Fragment can't use onRestart lifecycle");
        }
        mSubscriptionHelper.subscribe(subscription, lifeCycle);
    }

    @Override
    final public void unSubscribe(Subscription subscription) {
        mSubscriptionHelper.unSubscribe(subscription);
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

    public <T extends BasePresenter> T getPresenter(Class<T> clazz) {
        return mPresenterUtils.getPresenter(clazz);
    }

    /**
     * 根据类注解中的索引来获取presenter
     * <br/>
     * 注意：此方法只能获取@{@link Presenter}注解中的presenter
     *
     * @param index 索引从0开始
     * @param <T>   返回的presenter类型
     * @return
     */
    public <T extends BasePresenter> T getPresenter(int index) {
        return mPresenterUtils.getPresenter(index);
    }

    protected P getPresenter() {
        return mPresenter;
    }


}
