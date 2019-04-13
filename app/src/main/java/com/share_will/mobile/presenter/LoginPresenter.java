package com.share_will.mobile.presenter;

import com.share_will.mobile.App;
import com.share_will.mobile.Constant;
import com.share_will.mobile.model.LoginModel;
import com.share_will.mobile.model.entity.UserInfo;
import com.share_will.mobile.ui.views.LoginView;
import com.ubock.library.base.BaseEntity;
import com.ubock.library.base.BaseNetSubscriber;
import com.ubock.library.base.BasePresenter;
import com.ubock.library.utils.LogUtils;
import com.ubock.library.utils.MD5Util;
import com.ubock.library.utils.SharedPreferencesUtils;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class LoginPresenter extends BasePresenter<LoginModel, LoginView> {
    public LoginPresenter(LoginModel model, LoginView rootView) {
        super(model, rootView);
    }

    /**
     * 登录
     *
     * @param userId
     * @param password
     */
    public void login(final String userId, String password) {
        password = MD5Util.MD5(MD5Util.MD5(password));
        getModel().login(userId, password, 0)
                .compose(this.bindToLifecycle(getView()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseNetSubscriber<BaseEntity<UserInfo>>(LoginPresenter.this) {
                               @Override
                               public void onNext(BaseEntity<UserInfo> s) {
                                   if (s.getCode() == 0) {
                                       getView().onLogin(true, "登录成功");
                                       App.getInstance().getGlobalModel().setUserInfo(s.getData());
                                       SharedPreferencesUtils.saveDeviceData(App.getContext(), Constant.USER_INFO, s.getData());
                                   } else {
                                       getView().onLogin(false, s.getMessage());
                                   }
                               }

                               @Override
                               public boolean onErr(Throwable e) {
                                   getView().onLogin(false, "登录失败");
                                   LogUtils.e(e);
                                   return true;
                               }
                           }
                );
    }



}
