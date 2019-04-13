package com.share_will.mobile.presenter;

import android.text.TextUtils;

import com.share_will.mobile.model.PayModel;
import com.share_will.mobile.ui.views.PayView;
import com.ubock.library.base.BaseEntity;
import com.ubock.library.base.BaseNetSubscriber;
import com.ubock.library.base.BasePresenter;
import com.ubock.library.utils.LogUtils;

import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class PayPresenter extends BasePresenter<PayModel, PayView> {
    public PayPresenter(PayModel model, PayView rootView) {
        super(model, rootView);
    }

    /**
     * 获取支付宝预支付订单
     */
    public void getAliPayOrder(int payType, String appId, int orderType, String orderId, String body) {
        getModel().getAliPayOrder(payType, appId,orderType,orderId,body)
                .compose(this.bindToLifecycle(getView()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseNetSubscriber<BaseEntity<String>>(PayPresenter.this) {

                               @Override
                               public void onNext(BaseEntity<String> s) {
                                   if (s.getCode() == 0 && !TextUtils.isEmpty(s.getData())) {
                                       getView().onCreateAlipayOrder(true, s.getData(), "已生成订单");
                                   } else {
                                       getView().onCreateAlipayOrder(false, null, s.getMessage());
                                   }
                               }

                               @Override
                               public boolean onErr(Throwable e) {
                                   getView().onCreateAlipayOrder(false, null, "生成订单失败");
                                   return false;
                               }
                           }
                );
    }

    /**
     * 获取微信预支付订单
     */
    public void getWeiXinOrder(String appId, int orderType, String orderId, String body) {
        getModel().getWeiXinOrder(appId, orderType,orderId,body)
                .compose(this.bindToLifecycle(getView()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseNetSubscriber<BaseEntity<Map<String, String>>>(PayPresenter.this) {

                               @Override
                               public void onNext(BaseEntity<Map<String, String>> s) {
                                   getView().onCreateWeiXinOrder(s);
                               }

                               @Override
                               public boolean onErr(Throwable e) {
                                   getView().onCreateWeiXinOrder(null);
                                   return false;
                               }
                           }
                );
    }

    /**
     * 支付套餐
     * @param userId
     */
    public void payPackageOrder(String userId, final String orderId) {
        getModel().payPackageOrder(userId, orderId)
                .compose(this.bindToLifecycle(getView()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseNetSubscriber<BaseEntity<Object>>(PayPresenter.this) {
                               @Override
                               public void onNext(BaseEntity<Object> s) {
                                   if (s.getCode() == 0){
                                       getView().onPayPackageResult(true, "套餐购买成功");
                                   } else if (s.getCode() == 10011){
                                       //余额不足
                                       getView().onPayPackageResult(false, s.getMessage());
                                   }else {
                                       getView().onPayPackageResult(false, s.getMessage());
                                   }
                               }

                               @Override
                               public boolean onErr(Throwable e) {
                                   getView().onPayPackageResult(false, "订单支付失败");
                                   LogUtils.e(e);
                                   return true;
                               }
                           }
                );
    }
    public void payMoneyOrder(String userId, final String orderId) {
        getModel().payMoneyOrder(userId, orderId)
                .compose(this.bindToLifecycle(getView()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseNetSubscriber<BaseEntity<Object>>(PayPresenter.this) {
                               @Override
                               public void onNext(BaseEntity<Object> s) {
                                   if (s.getCode() == 0){
                                       getView().onPayPackageResult(true, "套餐购买成功");
                                   } else if (s.getCode() == 10011){
                                       //余额不足
                                       getView().onPayPackageResult(false, s.getMessage());
                                   }else {
                                       getView().onPayPackageResult(false, s.getMessage());
                                   }
                               }

                               @Override
                               public boolean onErr(Throwable e) {
                                   getView().onPayPackageResult(false, "订单支付失败");
                                   LogUtils.e(e);
                                   return true;
                               }
                           }
                );
    }
}
