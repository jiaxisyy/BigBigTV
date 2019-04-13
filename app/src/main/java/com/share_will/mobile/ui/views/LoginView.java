package com.share_will.mobile.ui.views;

import com.ubock.library.base.BaseView;

public interface LoginView extends BaseView {

    /**
     * 登录回调
     * @param success 登录成功
     * @param message 消息
     */
    void onLogin(boolean success, String message);

}
