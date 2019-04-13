package com.share_will.mobile.ui.views;

import com.share_will.mobile.model.entity.UserInfo;
import com.ubock.library.base.BaseEntity;
import com.ubock.library.base.BaseView;

public interface UserCenterView extends BaseView {

    /**
     * 登录回调
     * @param data
     */
    void onLoadBalance(BaseEntity<UserInfo> data);

    /**
     * 登录机柜回调
     * @param success 登录成功
     * @param message 消息
     */
    void onLoginCMS(boolean success, String message);

}
