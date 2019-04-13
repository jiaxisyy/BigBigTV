package com.share_will.mobile.ui.views;

import com.ubock.library.base.BaseEntity;
import com.ubock.library.base.BaseView;

import java.util.Map;

public interface PayView extends BaseView {

    /**
     * 获取支付宝预支付订单回调
     * @param success
     * @param message
     */
    void onCreateAlipayOrder(boolean success, String orderInfo, String message);

    /**
     * 获取微信预支付订单回调
     * @param ret
     */
    void onCreateWeiXinOrder(BaseEntity<Map<String, String>> ret);

    /**
     * 余额支付套餐订单回调
     * @param success
     * @param message
     */
    void onPayPackageResult(boolean success, String message);
}
