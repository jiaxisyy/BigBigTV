package com.share_will.mobile.presenter;

import com.share_will.mobile.model.UserCenterModel;
import com.share_will.mobile.model.entity.UserInfo;
import com.share_will.mobile.ui.views.UserCenterView;
import com.ubock.library.base.BaseEntity;
import com.ubock.library.base.BaseNetSubscriber;
import com.ubock.library.base.BasePresenter;
import com.ubock.library.common.RetryWithDelay;
import com.ubock.library.utils.LogUtils;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class UserCenterPresenter extends BasePresenter<UserCenterModel, UserCenterView> {
    public UserCenterPresenter(UserCenterModel model, UserCenterView rootView) {
        super(model, rootView);
    }

    /**
     * 获取用户账号余额、押金、套餐
     * @param userId
     */
    public void getBalance(String userId, final boolean showLoading) {
        getModel().getBalance(userId)
                .compose(this.bindToLifecycle(getView()))
                .retryWhen(new RetryWithDelay(3, 1))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseNetSubscriber<BaseEntity<UserInfo>>(UserCenterPresenter.this) {
                    @Override
                    protected boolean showLoading() {
                        return showLoading;
                    }

                    @Override
                       public void onNext(BaseEntity<UserInfo> s) {
                           getView().onLoadBalance(s);
                       }

                       @Override
                       public boolean onErr(Throwable e) {
                           getView().onLoadBalance(null);
                           LogUtils.e(e);
                           return true;
                       }
                   }
                );
    }
    /**
     * 登录机柜后台
     *
     * @param loginName phone
     * @param cabinetId sn
     * @param userType  默认3
     */
    public void loginCMS(String loginName, String cabinetId, int userType) {
        getModel().loginCMS(loginName, cabinetId, userType)
                .compose(this.bindToLifecycle(getView()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseNetSubscriber<BaseEntity<Object>>(UserCenterPresenter.this) {
                               @Override
                               public void onNext(BaseEntity<Object> s) {
                                   if (s.getCode() == 0) {
                                       getView().onLoginCMS(true, "登录成功");
                                   } else {
                                       getView().onLoginCMS(false, s.getMessage());
                                   }
                               }

                               @Override
                               public boolean onErr(Throwable e) {
                                   getView().onLoginCMS(false, "登录失败");
                                   LogUtils.e(e);
                                   return true;
                               }
                           }
                );

    }

    /**
     * 异常取电
     * @param userId
     */
    public void exceptionGetBattery(String cabinetId, String userId) {
        getModel().exceptionScanCodeGetBattery(cabinetId, userId)
                .compose(this.bindToLifecycle(getView()))
                .retryWhen(new RetryWithDelay(3, 1))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseNetSubscriber<BaseEntity<Object>>(UserCenterPresenter.this) {
                               @Override
                               public void onNext(BaseEntity<Object> s) {
                                   getView().onExceptionGetBattery(s);
                               }

                               @Override
                               public boolean onErr(Throwable e) {
                                   getView().onExceptionGetBattery(null);
                                   LogUtils.e(e);
                                   return true;
                               }
                           }
                );
    }
}
