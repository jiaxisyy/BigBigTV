package com.share_will.mobile.ui.views;

import com.ubock.library.base.BaseView;

import java.util.Map;

/**
 * Created by ChenGD on 2018/6/9.
 *
 * @author chenguandu
 */

public interface UpgradeServiceView extends BaseView {
    void onLoadVersion(Map<String, String> data);
}
