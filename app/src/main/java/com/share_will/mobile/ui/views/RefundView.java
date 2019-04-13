package com.share_will.mobile.ui.views;

import com.share_will.mobile.model.entity.DepositRefundEntity;
import com.ubock.library.base.BaseEntity;
import com.ubock.library.base.BaseView;

public interface RefundView extends BaseView {

    /**
     * 申请退款回调
     * @param ret 返回数据
     */
    void onApplyRefund(BaseEntity<Object> ret);

    /**
     * 取消申请退款回调
     * @param ret 返回数据
     */
    void onCancel(BaseEntity<Object> ret);

    /**
     * 申请退款详情回调
     * @param ret 返回数据
     */
    void onLoadRefund(BaseEntity<DepositRefundEntity> ret);

}
