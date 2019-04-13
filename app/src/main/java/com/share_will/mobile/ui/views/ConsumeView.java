package com.share_will.mobile.ui.views;

import com.share_will.mobile.model.entity.RecordEntity;
import com.share_will.mobile.model.entity.UserInfo;
import com.ubock.library.base.BaseEntity;
import com.ubock.library.base.BaseView;

import java.util.List;

public interface ConsumeView extends BaseView {

    /**
     * 加载消费记录列表回调
     * @param data
     */
    void onLoadConsumeList(BaseEntity<List<RecordEntity>> data);

}
