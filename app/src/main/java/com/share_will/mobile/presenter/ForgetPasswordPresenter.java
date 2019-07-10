package com.share_will.mobile.presenter;

import com.share_will.mobile.model.ForgetPasswordModel;
import com.share_will.mobile.ui.views.ForgetPasswordView;
import com.ubock.library.base.BaseEntity;
import com.ubock.library.base.BaseNetSubscriber;
import com.ubock.library.base.BasePresenter;
import com.ubock.library.utils.LogUtils;
import com.ubock.library.utils.MD5Util;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ForgetPasswordPresenter extends BasePresenter<ForgetPasswordModel, ForgetPasswordView> {
    public ForgetPasswordPresenter(ForgetPasswordModel model, ForgetPasswordView rootView) {
        super(model, rootView);
    }

    /**
     * 注册
     * @param userId 手机号
     * @param password 密码
     * @param verCode 验证码
     */
    public void updatePassword(String userId, String password, String verCode) {
        password = MD5Util.MD5(MD5Util.MD5(password));
        getModel().updatePassword(userId, password, verCode)
                .compose(this.bindToLifecycle(getView()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseNetSubscriber<BaseEntity<Object>>(ForgetPasswordPresenter.this) {
                               @Override
                               public void onNext(BaseEntity<Object> s) {
                                   if (s.getCode() == 0) {
                                       getView().onUpdatePassword(true, "密码修改成功");
                                   } else {
                                       getView().onUpdatePassword(false, s.getMessage());
                                   }
                               }

                               @Override
                               public boolean onErr(Throwable e) {
                                   getView().onUpdatePassword(false, "密码修改失败");
                                   LogUtils.e(e);
                                   return true;
                               }
                           }
                );
    }

    /**
     * 发送验证码
     * @param userId 手机号
     */
    public void sendVerifyCode(String userId,String customerCode) {
        getModel().sendVerifyCode(userId,customerCode)
                .compose(this.bindToLifecycle(getView()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseNetSubscriber<BaseEntity<Object>>(ForgetPasswordPresenter.this) {
                               @Override
                               public void onNext(BaseEntity<Object> s) {
                                   if (s.getCode() == 0) {
                                       getView().onSendVerifyCode(true, "验证码已发送，请注意查收");
                                   } else {
                                       getView().onSendVerifyCode(false, s.getMessage());
                                   }
                               }

                               @Override
                               public boolean onErr(Throwable e) {
                                   getView().onSendVerifyCode(false, "验证码发送失败");
                                   LogUtils.e(e);
                                   return true;
                               }
                           }
                );
    }
}
