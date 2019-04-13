package com.share_will.mobile.presenter;

import com.share_will.mobile.model.ChargingModel;
import com.share_will.mobile.model.entity.ChargingEntity;
import com.share_will.mobile.ui.views.ChargingView;
import com.ubock.library.base.BaseEntity;
import com.ubock.library.base.BaseNetSubscriber;
import com.ubock.library.base.BasePresenter;
import com.ubock.library.utils.LogUtils;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ChargingPresenter extends BasePresenter<ChargingModel, ChargingView> {
    public ChargingPresenter(ChargingModel model, ChargingView rootView) {
        super(model, rootView);
    }

    /**
     * 获取充电选项:时间、价格
     * @param sn 机柜SN
     */
    public void getChargingList(String sn) {

        getModel().getChargingList(sn)
                .compose(this.bindToLifecycle(getView()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseNetSubscriber<BaseEntity<List<ChargingEntity>>>(ChargingPresenter.this) {
                    @Override
                       public void onNext(BaseEntity<List<ChargingEntity>> s) {
                           getView().onLoadChargingList(s);
                    }
                       @Override
                       public boolean onErr(Throwable e) {
                           getView().onLoadChargingList(null);
                           LogUtils.e(e);
                           return true;


                       }
                   }
                );
    }

    /**
     * 生成充电订单
     * @param sn 机柜SN
     */
    public void createChargingOrder(String sn, String userid, int channel, String chargeid) {

        getModel().createChargingOrder(sn, userid ,channel, chargeid)
                .compose(this.bindToLifecycle(getView()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseNetSubscriber<BaseEntity<Object>>(ChargingPresenter.this) {
                               @Override
                               public void onNext(BaseEntity<Object> s) {
                                   getView().onCreateChargingOrder(s);
                               }
                               @Override
                               public boolean onErr(Throwable e) {
                                   getView().onCreateChargingOrder(null);
                                   LogUtils.e(e);
                                   return true;
                               }
                           }
                );
    }

}
