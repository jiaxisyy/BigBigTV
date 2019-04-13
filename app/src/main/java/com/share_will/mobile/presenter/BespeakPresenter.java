package com.share_will.mobile.presenter;


import com.share_will.mobile.model.BespeakModel;
import com.share_will.mobile.model.entity.BespeakEntity;
import com.share_will.mobile.ui.views.BespeakView;
import com.ubock.library.base.BaseEntity;
import com.ubock.library.base.BaseNetSubscriber;
import com.ubock.library.base.BasePresenter;

import java.util.concurrent.TimeUnit;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class BespeakPresenter extends BasePresenter<BespeakModel, BespeakView> {

    private int i = 0;
    /**
     * 设置轮询次数
     */
    private static int COUNT = 5;
    /**轮询间隔时间 单位:s*/
    private static int SECONDS = 1;
    private static boolean ISCLOSELOADING = false;

    public BespeakPresenter(BespeakModel model, BespeakView view) {
        super(model, view);
    }

    /**
     * 获取预约换电状态
     *
     * @param userId
     */
    public void getBespeakInfo(String userId) {

        getModel().getBespeakInfo(userId)
                .repeatWhen(observable -> observable.delay(SECONDS, TimeUnit.SECONDS))
                .takeUntil(dataBeanBaseEntity -> {
                    if (i != COUNT && dataBeanBaseEntity.getData().getStatus() == 0) {
                        return false;
                    }
                    i = 0;
                    return true;
                })
                .compose(this.bindToLifecycle(getView()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseNetSubscriber<BaseEntity<BespeakEntity.DataBean>>(BespeakPresenter.this) {
                               @Override
                               public void onNext(BaseEntity<BespeakEntity.DataBean> s) {

                                   if (s.getCode() == 0) {
                                       if (s.getData().getStatus() != 0) {
                                           getView().onBespeakInfoResult(s);
                                           ISCLOSELOADING = true;
                                           super.onCompleted();
                                       }
                                       i++;
                                   } else {
                                       getView().onFailedMessage(s.getMessage());
                                   }
                               }

                               @Override
                               protected boolean closeLoading() {

                                   return ISCLOSELOADING;
                               }

                               @Override
                               public boolean onErr(Throwable e) {

                                   getView().onFailedMessage("获取信息失败");
                                   return super.onErr(e);
                               }
                           }
                );
    }

    /**
     * 添加预约
     *
     * @param userId
     * @param cabinetId 机柜Sn
     */
    public void addBespeak(String userId, String cabinetId) {

        getModel().addBespeak(userId, cabinetId)
                .compose(this.bindToLifecycle(getView()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseNetSubscriber<BaseEntity<BespeakEntity.AddDataBean>>(BespeakPresenter.this) {
                               @Override
                               public void onNext(BaseEntity<BespeakEntity.AddDataBean> s) {

                                   if (s.getCode() == 0) {
                                       getView().onAddBespeakResult(s);
                                   } else {
                                       getView().onFailedMessage(s.getMessage());
                                   }
                               }

                               @Override
                               public boolean onErr(Throwable e) {

                                   getView().onFailedMessage("提交预约失败");
                                   return super.onErr(e);
                               }
                           }
                );

    }

    /**
     * 修改预约换电状态，包括取消、预约成功、已取电状态
     *
     * @param id
     * @param userId
     * @param status
     * @param desc
     */
    public void updateBespeak(String id, String userId, String status, String desc) {
        getModel().updateBespeak(id, userId, status, desc)
                .compose(this.bindToLifecycle(getView()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseNetSubscriber<BaseEntity<Object>>(BespeakPresenter.this) {
                               @Override
                               public void onNext(BaseEntity<Object> s) {

                                   if (s.getCode() == 0) {
                                       getView().onUpdateBespeakResult();
                                   } else {
                                       getView().onFailedMessage(s.getMessage());
                                   }
                               }

                               @Override
                               public boolean onErr(Throwable e) {

                                   getView().onFailedMessage("取消预约失败");
                                   return super.onErr(e);
                               }
                           }
                );

    }

}
