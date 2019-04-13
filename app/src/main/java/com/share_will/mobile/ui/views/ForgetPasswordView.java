package com.share_will.mobile.ui.views;

import com.ubock.library.base.BaseView;

public interface ForgetPasswordView extends BaseView {

    /**
     * 修改密码回调
     * @param success 成功
     * @param message 消息
     */
    void onUpdatePassword(boolean success, String message);

    /**
     * 发送验证码回调
     * @param success 发送验证码成功
     * @param message 消息
     */
    void onSendVerifyCode(boolean success, String message);

}
