package com.share_will.mobile.presenter;

import com.share_will.mobile.model.HomeFragmentModel;
import com.share_will.mobile.model.entity.AlarmEntity;
import com.share_will.mobile.model.entity.BatteryEntity;
import com.share_will.mobile.model.entity.ChargeBatteryEntity;
import com.share_will.mobile.ui.views.IHomeFragmentView;
import com.ubock.library.base.BaseEntity;
import com.ubock.library.base.BaseNetSubscriber;
import com.ubock.library.base.BasePresenter;
import com.ubock.library.utils.LogUtils;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class HomeFragmentPresenter extends BasePresenter<HomeFragmentModel, IHomeFragmentView> {
    public HomeFragmentPresenter(HomeFragmentModel model, IHomeFragmentView rootView) {
        super(model, rootView);
    }

    public void getAlarmList(String userId, String token) {
        getModel().getAlarmList(userId, token)
                .compose(this.bindToLifecycle(getView()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseNetSubscriber<BaseEntity<AlarmEntity>>(HomeFragmentPresenter.this) {
                               @Override
                               public void onNext(BaseEntity<AlarmEntity> s) {
                                   if (s.getCode() == 0) {
                                       getView().onLoadAlarmResult(s);
                                   } else {

                                           getView().showMessage(s.getMessage());

                                       getView().onLoadAlarmResult(null);
                                   }
                               }

                               @Override
                               public boolean onErr(Throwable e) {
                                   getView().onLoadAlarmResult(null);
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
                .subscribe(new BaseNetSubscriber<BaseEntity<ChargeBatteryEntity>>(HomeFragmentPresenter.this) {
                               @Override
                               public void onNext(BaseEntity<ChargeBatteryEntity> s) {
                                   if (s.getCode() == 0) {
                                       getView().onLoadChargeBatteryInfoResult(s);
                                   } else {

                                           getView().showMessage(s.getMessage());

                                       getView().onLoadChargeBatteryInfoResult(null);
                                   }
                               }

                               @Override
                               public boolean onErr(Throwable e) {
                                   getView().onLoadChargeBatteryInfoResult(null);
                                   LogUtils.e(e);
                                   return true;
                               }
                           }
                );
    }

    /**
     * 已有电池信息
     *
     * @param userId
     * @param token
     */
    public void getBatteryInfo(String userId, String token) {
        getModel().getBatteryInfo(userId, token)
                .compose(this.bindToLifecycle(getView()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseNetSubscriber<BaseEntity<BatteryEntity>>(HomeFragmentPresenter.this) {
                               @Override
                               public void onNext(BaseEntity<BatteryEntity> s) {
                                   if (s.getCode() == 0) {
                                       getView().onLoadBatteryInfoResult(s);
                                   } else {

                                           getView().showMessage(s.getMessage());

                                       getView().onLoadBatteryInfoResult(null);
                                   }
                               }

                               @Override
                               public boolean onErr(Throwable e) {
                                   getView().onLoadBatteryInfoResult(null);
                                   LogUtils.e(e);
                                   return true;
                               }
                           }
                );
    }

    /**
     * 绑定电池
     *
     * @param
     * @param
     */
    public void bindBattery(String userId, String batteryId) {
        getModel().bindBattery(userId, batteryId)
                .compose(this.bindToLifecycle(getView()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseNetSubscriber<BaseEntity<Object>>(HomeFragmentPresenter.this) {
                               @Override
                               public void onNext(BaseEntity<Object> s) {
                                   if (s.getCode() == 0) {
                                       getView().onBindBatteryResult(s);
                                   } else if (s.getCode() == 10010) {
                                       getView().showMessage(s.getMessage());
                                   } else {
                                       getView().onBindBatteryResult(null);
                                   }
                               }

                               @Override
                               public boolean onErr(Throwable e) {
                                   getView().onBindBatteryResult(null);
                                   LogUtils.e(e);
                                   return true;
                               }
                           }
                );
    }

    /**
     * 扫码领取电池
     * @param cabinetId
     * @param userId
     * @param
     */
    public void scanCodeGetBattery(String cabinetId, String userId) {
        getModel().scanCodeGetBattery(cabinetId, userId)
                .compose(this.bindToLifecycle(getView()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseNetSubscriber<BaseEntity<Object>>(HomeFragmentPresenter.this) {
                               @Override
                               public void onNext(BaseEntity<Object> s) {
                                   getView().onScanCodeGetBatteryResult(s);
                               }

                               @Override
                               public boolean onErr(Throwable e) {
                                   getView().onScanCodeGetBatteryResult(null);
                                   LogUtils.e(e);
                                   return true;
                               }
                           }
                );
    }

}
