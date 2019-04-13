package com.share_will.mobile.ui.views;

import com.share_will.mobile.model.entity.PackageEntity;
import com.share_will.mobile.model.entity.PackageOrderEntity;
import com.share_will.mobile.model.entity.UserInfo;
import com.ubock.library.base.BaseEntity;
import com.ubock.library.base.BaseView;

import java.util.List;

public interface ShopView extends BaseView {

    /**
     * 获取套餐列表回调
     *
     * @param data
     */
    void onLoadPackageList(BaseEntity<List<PackageEntity>> data);

    /**
     * 生成套餐订单回调
     *
     * @param success
     * @param orderId
     * @param message
     */
    void onCreateOrder(boolean success, String orderId, String message);

    /**
     * 支付订单回调
     *
     * @param success
     * @param message
     */
    void onPayPackageResult(boolean success, String message);

}
