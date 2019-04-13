package com.share_will.mobile.presenter;

import com.share_will.mobile.model.RechargeModel;
import com.share_will.mobile.ui.views.RechargeView;
import com.ubock.library.base.BaseEntity;
import com.ubock.library.base.BaseNetSubscriber;
import com.ubock.library.base.BasePresenter;
import com.ubock.library.utils.LogUtils;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RechargePresenter extends BasePresenter<RechargeModel, RechargeView> {
    public RechargePresenter(RechargeModel model, RechargeView rootView) {
        super(model, rootView);
    }

    /**
     * 生成充值订单
     * @param userId
     */
    public void crateRechargeOrder(String userId, int money) {
        getModel().crateRechargeOrder(userId, money)
                .compose(this.bindToLifecycle(getView()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseNetSubscriber<BaseEntity<String>>(RechargePresenter.this) {

                    @Override
                       public void onNext(BaseEntity<String> s) {
                           if (s.getCode() == 0) {
                               getView().onCreateOrder(true, s.getData(), "已生成充值订单");
                           } else {
                               getView().onCreateOrder(false, null, s.getMessage());
                           }
                       }

                       @Override
                       public boolean onErr(Throwable e) {
                           getView().onCreateOrder(false, null, "生成充值订单失败");
                           LogUtils.e(e);
                           return false;
                       }
                   }
                );
    }
}
