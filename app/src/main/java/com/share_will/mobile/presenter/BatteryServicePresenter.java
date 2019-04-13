package com.share_will.mobile.presenter;

import com.share_will.mobile.model.BatteryServiceModel;
import com.share_will.mobile.ui.views.BatteryServiceView;
import com.ubock.library.base.BaseEntity;
import com.ubock.library.base.BaseNetSubscriber;
import com.ubock.library.base.BasePresenter;
import com.ubock.library.common.RetryWithDelay;
import com.ubock.library.utils.LogUtils;

import java.util.Map;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class BatteryServicePresenter extends BasePresenter<BatteryServiceModel, BatteryServiceView> {
    private Subscriber mSubscriber;
    public BatteryServicePresenter(BatteryServiceModel model, BatteryServiceView view) {
        super(model, view);
    }

    /**
     * 获取电量信息
     *
     * @param userId
     */
    public void getBattery(String userId) {
        if (mSubscriber != null && !mSubscriber.isUnsubscribed()){
            return;
        }
        getModel().getBattery(userId)
                .retryWhen(new RetryWithDelay(3, 1))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mSubscriber = new BaseNetSubscriber<BaseEntity<Map<String, String>>>(BatteryServicePresenter.this) {
                       @Override
                       public void onNext(BaseEntity<Map<String, String>> s) {
                           getView().onLoadBattery(s);
                       }

                       @Override
                       public boolean onErr(Throwable e) {
                           getView().onLoadBattery(null);
                           LogUtils.e(e);
                           return true;
                       }
                   }
                );
    }
}
