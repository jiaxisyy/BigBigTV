package com.share_will.mobile.presenter;

import com.share_will.mobile.model.AlarmFragmentModel;
import com.share_will.mobile.model.entity.AlarmEntity;
import com.share_will.mobile.ui.views.IAlarmFragmentView;
import com.ubock.library.base.BaseEntity;
import com.ubock.library.base.BaseNetSubscriber;
import com.ubock.library.base.BasePresenter;
import com.ubock.library.utils.LogUtils;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AlarmFragmentPresenter extends BasePresenter<AlarmFragmentModel, IAlarmFragmentView> {
    public AlarmFragmentPresenter(AlarmFragmentModel model, IAlarmFragmentView rootView) {
        super(model, rootView);
    }

    public void closeAlarm(String userId, String devtype, String deveui) {
        getModel().closeAlarm(userId, devtype, deveui)
                .compose(this.bindToLifecycle(getView()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseNetSubscriber<BaseEntity<Object>>(AlarmFragmentPresenter.this) {
                               @Override
                               public void onNext(BaseEntity<Object> s) {
                                   if (s.getCode() == 0) {
                                       getView().onCloseAlarmResult(s);
                                   } else {
                                       getView().onCloseAlarmResult(null);
                                   }
                               }

                               @Override
                               public boolean onErr(Throwable e) {
                                   getView().onCloseAlarmResult(null);
                                   LogUtils.e(e);
                                   return true;
                               }
                           }
                );
    }


}
