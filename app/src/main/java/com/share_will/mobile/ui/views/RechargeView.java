package com.share_will.mobile.ui.views;

import com.ubock.library.base.BaseView;

public interface RechargeView extends BaseView {

    /**
     * 生成充值订单回调
     * @param success
     * @param message
     */
    void onCreateOrder(boolean success, String orderId, String message);

}
