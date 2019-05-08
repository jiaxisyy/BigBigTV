package com.share_will.mobile.presenter;

import com.share_will.mobile.model.ChargeStakeModel;
import com.share_will.mobile.model.entity.ChargeStakeEntity;
import com.share_will.mobile.model.entity.ChargeStakeOrderInfoEntity;
import com.share_will.mobile.model.entity.ChargingEntity;
import com.share_will.mobile.ui.views.ChargeStakeView;
import com.ubock.library.base.BaseEntity;
import com.ubock.library.base.BaseNetSubscriber;
import com.ubock.library.base.BasePresenter;
import com.ubock.library.utils.LogUtils;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ChargeStakePresenter extends BasePresenter<ChargeStakeModel, ChargeStakeView> {
    public ChargeStakePresenter(ChargeStakeModel model, ChargeStakeView rootView) {
        super(model, rootView);
    }

    /**
     * 获取用户充电信息
     */
    public void getChargingInfo(final boolean isFinishing) {

        getModel().getChargingInfo()
                .compose(this.bindToLifecycle(getView()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseNetSubscriber<BaseEntity<ChargeStakeOrderInfoEntity>>(ChargeStakePresenter.this) {
                               @Override
                               protected boolean showLoading() {
                                   return isFinishing;
                               }

                               @Override
                               public void onNext(BaseEntity<ChargeStakeOrderInfoEntity> s) {

                                   if (s.getCode() == 0) {
                                       getView().onLoadChargingInfo(s);
                                   } else {
                                       getView().onLoadChargingInfo(null);
                                   }

                               }

                               @Override
                               public boolean onErr(Throwable e) {
                                   getView().onLoadChargingInfo(null);
                                   LogUtils.e(e);
                                   return true;
                               }
                           }
                );
    }

    /**
     * 获取充电桩状态
     *
     * @param cabinetId 机柜SN
     */
    public void getStakeStatus(String cabinetId) {

        getModel().getStakeStatus(cabinetId)
                .compose(this.bindToLifecycle(getView()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseNetSubscriber<BaseEntity<List<ChargeStakeEntity>>>(ChargeStakePresenter.this) {
                               @Override
                               public void onNext(BaseEntity<List<ChargeStakeEntity>> s) {
                                   if (s.getCode() == 0) {
                                       getView().onLoadStakeStatus(s);
                                   } else {
                                       getView().onLoadStakeStatus(null);
                                   }

                               }

                               @Override
                               public boolean onErr(Throwable e) {
                                   LogUtils.e(e);
                                   getView().onLoadStakeStatus(null);
                                   return true;
                               }
                           }
                );
    }

    /**
     * 充电桩充电或结束充电结果
     *
     * @param cabinetId 机柜SN
     * @param userId    用户手机
     * @param index     充电桩号
     * @param status    设置的状态，0关(断电)，1开(通电)
     */
    public void stakeCharging(String cabinetId, String userId, int index, int status) {

        getModel().stakeCharging(cabinetId, userId, index, status)
                .compose(this.bindToLifecycle(getView()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseNetSubscriber<BaseEntity<Object>>(ChargeStakePresenter.this) {
                               @Override
                               public void onNext(BaseEntity<Object> s) {
                                   if (s.getCode() == 0) {
                                       getView().onChargingResult(s);
                                   } else {
                                       getView().onChargingResult(null);
                                   }
                               }

                               @Override
                               public boolean onErr(Throwable e) {
                                   LogUtils.e(e);
                                   getView().onChargingResult(null);
                                   return true;
                               }
                           }
                );
    }

}
