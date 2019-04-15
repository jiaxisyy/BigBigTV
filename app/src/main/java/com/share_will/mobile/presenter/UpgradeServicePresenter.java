package com.share_will.mobile.presenter;

import com.share_will.mobile.model.UpgradeServiceModel;
import com.share_will.mobile.model.entity.UserInfo;
import com.share_will.mobile.ui.views.UpgradeServiceView;
import com.ubock.library.base.BaseEntity;
import com.ubock.library.base.BaseNetSubscriber;
import com.ubock.library.base.BasePresenter;
import com.ubock.library.utils.LogUtils;

import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class UpgradeServicePresenter extends BasePresenter<UpgradeServiceModel, UpgradeServiceView> {
    public UpgradeServicePresenter(UpgradeServiceModel model, UpgradeServiceView rootView) {
        super(model, rootView);
    }

    /**
     * 检测新版本
     * @param userId
     */
    public void checkVersion(String versionName, int versionCode, int type, String customer, String userId, final boolean showLoading) {
        getModel().checkVersion(versionName, versionCode,  type, customer, userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseNetSubscriber<BaseEntity<Map<String, String>>>(UpgradeServicePresenter.this) {
                    @Override
                    protected boolean showLoading() {
                        return showLoading;
                    }

                    @Override
                       public void onNext(BaseEntity<Map<String, String>> s) {
                           if (s.getCode() == 0) {
                               getView().onLoadVersion(s.getData());
                           }
                       }

                       @Override
                       public boolean onErr(Throwable e) {
                           LogUtils.e(e);
                           return true;
                       }
                   }
                );
    }
}
