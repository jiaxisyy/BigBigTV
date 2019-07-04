package com.share_will.mobile.presenter;

import com.share_will.mobile.model.AlarmFragmentModel;
import com.share_will.mobile.model.FixMapModel;
import com.share_will.mobile.model.entity.FixStationEnity;
import com.share_will.mobile.ui.views.IAlarmFragmentView;
import com.share_will.mobile.ui.views.IFixMapView;
import com.ubock.library.base.BaseEntity;
import com.ubock.library.base.BaseNetSubscriber;
import com.ubock.library.base.BasePresenter;
import com.ubock.library.utils.LogUtils;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class FixMapPresenter extends BasePresenter<FixMapModel, IFixMapView> {
    public FixMapPresenter(FixMapModel model, IFixMapView rootView) {
        super(model, rootView);
    }

    public void loadFixStation() {
        getModel().loadFixStation()
                .compose(this.bindToLifecycle(getView()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseNetSubscriber<BaseEntity<List<FixStationEnity>>>(FixMapPresenter.this) {
                               @Override
                               public void onNext(BaseEntity<List<FixStationEnity>> s) {
                                   if (s.getCode() == 0) {
                                       getView().onLoadStationResult(s);
                                   } else {
                                       getView().onLoadStationResult(null);
                                   }
                               }

                               @Override
                               public boolean onErr(Throwable e) {
                                   getView().onLoadStationResult(null);
                                   LogUtils.e(e);
                                   return true;
                               }
                           }
                );

    }


}
