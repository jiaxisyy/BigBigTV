package com.share_will.mobile.ui.views;

import com.share_will.mobile.model.entity.BespeakEntity;
import com.ubock.library.base.BaseEntity;
import com.ubock.library.base.BaseView;

public interface BespeakView extends BaseView {

    /**
     * 查询预约换电信息回调
     *
     * @param dataBean 预约换电信息
     */
    void onBespeakInfoResult(BaseEntity<BespeakEntity.DataBean> dataBean);

    /**
     * 添加预约换电
     *
     * @param addDataBean
     */
    void onAddBespeakResult(BaseEntity<BespeakEntity.AddDataBean> addDataBean);



    /**
     * 修改预约换电状态，包括取消、预约成功、已取电状态
     */
    void onUpdateBespeakResult();


    /**
     * 请求失败错误信息反馈
     * @param msg
     */
    void onFailedMessage(String msg);


}
