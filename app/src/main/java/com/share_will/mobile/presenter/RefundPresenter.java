package com.share_will.mobile.presenter;

import com.share_will.mobile.model.RefundModel;
import com.share_will.mobile.model.entity.DepositRefundEntity;
import com.share_will.mobile.ui.views.RefundView;
import com.ubock.library.base.BaseEntity;
import com.ubock.library.base.BaseNetSubscriber;
import com.ubock.library.base.BasePresenter;
import com.ubock.library.utils.LogUtils;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RefundPresenter extends BasePresenter<RefundModel, RefundView> {
    public RefundPresenter(RefundModel model, RefundView rootView) {
        super(model, rootView);
    }

    /**
     * 申请退押金
     * @param userId  用户手机
     * @param bankUserName  银行卡人姓名
     * @param bankName  银行名称
     * @param bankCard  银行卡号
     * @param desc  退款备注
     */
    public void applyRefund(String userId, String bankUserName, String bankName, String bankCard, String desc) {
        getModel().applyRefund(userId, bankUserName, bankName, bankCard, desc)
                .compose(this.bindToLifecycle(getView()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseNetSubscriber<BaseEntity<Object>>(RefundPresenter.this) {
                               @Override
                               public void onNext(BaseEntity<Object> s) {
                                   getView().onApplyRefund(s);
                               }

                               @Override
                               public boolean onErr(Throwable e) {
                                   LogUtils.e(e);
                                   return false;
                               }
                           }
                );
    }

    /**
     * 取消申请退押金
     * @param id  退款id
     */
    public void cancelApplyRefund(long id) {
        getModel().cancelApplyRefund(id)
                .compose(this.bindToLifecycle(getView()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseNetSubscriber<BaseEntity<Object>>(RefundPresenter.this) {
                               @Override
                               public void onNext(BaseEntity<Object> s) {
                                   getView().onCancel(s);
                               }

                               @Override
                               public boolean onErr(Throwable e) {
                                   LogUtils.e(e);
                                   return false;
                               }
                           }
                );
    }

    /**
     * 申请退押金详情
     * @param userId  用户id
     */
    public void getRefundDetail(String userId) {
        getModel().applyRefundDetail(userId)
                .compose(this.bindToLifecycle(getView()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseNetSubscriber<BaseEntity<DepositRefundEntity>>(RefundPresenter.this) {
                               @Override
                               public void onNext(BaseEntity<DepositRefundEntity> s) {
                                   getView().onLoadRefund(s);
                               }

                               @Override
                               public boolean onErr(Throwable e) {
                                   LogUtils.e(e);
                                   return false;
                               }
                           }
                );
    }
}
