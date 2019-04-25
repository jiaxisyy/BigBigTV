package com.share_will.mobile.presenter;

import com.share_will.mobile.model.LocationServiceModel;
import com.share_will.mobile.model.UpgradeServiceModel;
import com.share_will.mobile.ui.views.LocationServiceView;
import com.share_will.mobile.ui.views.UpgradeServiceView;
import com.ubock.library.base.BaseEntity;
import com.ubock.library.base.BaseNetSubscriber;
import com.ubock.library.base.BasePresenter;
import com.ubock.library.utils.LogUtils;

import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class LocationServicePresenter extends BasePresenter<LocationServiceModel, LocationServiceView> {
    public LocationServicePresenter(LocationServiceModel model, LocationServiceView rootView) {
        super(model, rootView);
    }

    /**
     * 上传位置
     * @param userId
     * @param longitude
     * @param latitude
     * @param range
     */
    public void uploadLocation(String userId, double longitude, double latitude, float range) {
        getModel().uploadLocation(userId, longitude, latitude, range)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseNetSubscriber<BaseEntity<Object>>(LocationServicePresenter.this) {
                    @Override
                    protected boolean showLoading() {
                        return false;
                    }

                    @Override
                       public void onNext(BaseEntity<Object> s) {
                           if (s.getCode() == 0) {
                               getView().onUploadPosition(s);
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
