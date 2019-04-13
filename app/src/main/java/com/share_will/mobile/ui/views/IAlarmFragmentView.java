package com.share_will.mobile.ui.views;

import com.ubock.library.base.BaseEntity;
import com.ubock.library.base.BaseView;

public interface IAlarmFragmentView extends BaseView {

    /**
     * 关闭告警回调
     */
    void onCloseAlarmResult(BaseEntity<Object> s);
}
