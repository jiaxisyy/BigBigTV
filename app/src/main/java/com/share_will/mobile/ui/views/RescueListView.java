package com.share_will.mobile.ui.views;

import com.share_will.mobile.model.entity.RescueEntity;
import com.ubock.library.base.BaseEntity;
import com.ubock.library.base.BaseView;

import java.util.List;

public interface RescueListView extends BaseView {

    /**
     * 获取救援列表回调
     * @param ret
     */
    void onLoadRescueList(BaseEntity<List<RescueEntity>> ret);

    /**
     * 取消救援回调
     * @param ret
     */
    void onCancelRescue(BaseEntity<Object> ret);

}
