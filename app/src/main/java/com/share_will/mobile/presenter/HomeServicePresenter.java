package com.share_will.mobile.presenter;

import android.util.Log;

import com.share_will.mobile.model.HomeServiceModel;
import com.share_will.mobile.model.entity.ChargeBatteryEntity;
import com.share_will.mobile.model.entity.ChargeOrderEntity;
import com.share_will.mobile.ui.views.IHomeServiceView;
import com.ubock.library.base.BaseEntity;
import com.ubock.library.base.BaseNetSubscriber;
import com.ubock.library.base.BasePresenter;
import com.ubock.library.utils.LogUtils;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class HomeServicePresenter extends BasePresenter<HomeServiceModel, IHomeServiceView> {
    public HomeServicePresenter(HomeServiceModel model, IHomeServiceView rootView) {
        super(model, rootView);
    }



    /**
     * 扫码充电
     */
    public void chargeScan(String userId, String token,
                           String cabinetId,
                           String time,
                           int type,
                           int appType) {
        getModel().chargeScan(userId, token, cabinetId, time, type, appType)
                .compose(this.bindToLifecycle(getView()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseNetSubscriber<BaseEntity<Object>>(HomeServicePresenter.this) {
                               @Override
                               public void onNext(BaseEntity<Object> s) {
                                   if (s.getCode() == 0) {
                                       getView().showMessage("成功");
                                       getView().OnChargeScanResult(s);
                                   } else {
                                       getView().showMessage(s.getMessage());
                                       getView().OnChargeScanResult(null);
                                   }
                               }

                               @Override
                               public boolean onErr(Throwable e) {
                                   LogUtils.e(e);
                                   return true;
                               }
                           }
                );
    }

    /**
     * 扫码结束充电
     */
    public void stopChargeScan(String userId, String token,
                               String cabinetId) {
        getModel().stopChargeScan(userId, token, cabinetId)
                .compose(this.bindToLifecycle(getView()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseNetSubscriber<BaseEntity<ChargeOrderEntity>>(HomeServicePresenter.this) {
                               @Override
                               public void onNext(BaseEntity<ChargeOrderEntity> s) {
                                   if (s.getCode() == 0) {
                                       getView().OnStopChargeScanResult(s);
                                   } else {
                                       getView().OnStopChargeScanResult(null);
                                   }
                               }

                               @Override
                               public boolean onErr(Throwable e) {
                                   LogUtils.e(e);
                                   return true;
                               }
                           }
                );
    }

    /**
     * 充电电池信息
     *
     * @param userId
     * @param token
     */
    public void getChargeBatteryInfo(String userId, String token) {

        getModel().getChargeBatteryInfo(userId, token)
                .compose(this.bindToLifecycle(getView()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseNetSubscriber<BaseEntity<ChargeBatteryEntity>>(HomeServicePresenter.this) {
                               @Override
                               public void onNext(BaseEntity<ChargeBatteryEntity> s) {
                                   if (s.getCode() == 0) {
                                       getView().OnChargeResult(s);
                                   } else {
                                       getView().OnChargeResult(null);
                                   }
                               }

                               @Override
                               public boolean onErr(Throwable e) {
                                   getView().OnChargeResult(null);
                                   LogUtils.e(e);
                                   return true;
                               }
                           }
                );
    }

}
