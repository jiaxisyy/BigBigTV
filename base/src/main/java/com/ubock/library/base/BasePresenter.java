package com.ubock.library.base;


import android.os.Bundle;
import android.support.annotation.Nullable;

import com.trello.rxlifecycle.LifecycleTransformer;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by chengd on 17/3/8.
 */
public class BasePresenter<M extends BaseModel, V extends BaseView> implements Presenter {
    protected final String TAG = this.getClass().getSimpleName();

    protected M mModel;
    protected V mRootView;

    //是否前台
    private boolean mIsResume = false;

    public BasePresenter() {
        onCreate();
    }

    public BasePresenter(V rootView) {
        this.mRootView = rootView;
        onCreate();

    }

    public BasePresenter(M model) {
        this.mModel = model;
        onCreate();

    }

    public BasePresenter(M model, V rootView) {
        this.mModel = model;
        this.mRootView = rootView;

        onCreate();
    }

    public M getModel() {
        return mModel;
    }

    public V getView() {
        return mRootView;
    }


    @Override
    public void onCreate() {

    }

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onResume() {
        mIsResume = true;
    }

    @Override
    public void onPause() {
        mIsResume = false;
    }

    @Override
    public void onStop() {

    }

    @Override
    public void onDestroy() {
        this.mModel = null;
        this.mRootView = null;
    }

    public boolean isResume(){
        return mIsResume;
    }

    public <T> LifecycleTransformer<T> bindToLifecycle(BaseView view) {
        if (view instanceof BaseFragmentActivity) {
            return ((BaseFragmentActivity) view).<T>bindToLifecycle();
        } else if (view instanceof BaseFragment) {
            return ((BaseFragment) view).<T>bindToLifecycle();
        } else if (view instanceof BaseDialogFragment) {
            return ((BaseDialogFragment) view).<T>bindToLifecycle();
        } else if (view instanceof BaseDialog) {
            return ((BaseFragmentActivity) ((BaseDialog) view).getActivity()).<T>bindToLifecycle();
        } else {
            throw new IllegalArgumentException("view isn't activity or fragment or dialog");
        }

    }

    public Observable.Transformer schedulersTransformer() {
        return new Observable.Transformer() {
            @Override
            public Object call(Object observable) {
                return ((Observable) observable).subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

}
