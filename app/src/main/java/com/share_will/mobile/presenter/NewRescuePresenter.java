package com.share_will.mobile.presenter;

import com.share_will.mobile.model.NewRescueModel;
import com.share_will.mobile.model.RegisterModel;
import com.share_will.mobile.model.entity.CityEntity;
import com.share_will.mobile.model.entity.StationEntity;
import com.share_will.mobile.ui.views.NewRescueView;
import com.share_will.mobile.ui.views.RegisterView;
import com.ubock.library.base.BaseEntity;
import com.ubock.library.base.BaseNetSubscriber;
import com.ubock.library.base.BasePresenter;
import com.ubock.library.utils.LogUtils;
import com.ubock.library.utils.MD5Util;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class NewRescuePresenter extends BasePresenter<NewRescueModel, NewRescueView> {
    public NewRescuePresenter(NewRescueModel model, NewRescueView rootView) {
        super(model, rootView);
    }

    /**
     * 获取已开通城市列表
     */
    public void getCityList() {
        getModel().getCityList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseNetSubscriber<BaseEntity<List<CityEntity>>>(NewRescuePresenter.this) {

                               @Override
                               public void onNext(BaseEntity<List<CityEntity>> ret) {

                                   getView().onLoadCityList(ret);
                               }

                               @Override
                               public boolean onErr(Throwable e) {
                                   getView().onLoadCityList(null);
                                   LogUtils.e(e);
                                   return true;
                               }
                           }
                );
    }

    /**
     * 获取站点列表
     *
     * @param areaCode 手机号
     */
    public void getStationList(String areaCode) {
        getModel().getStationList(areaCode)
                .compose(this.bindToLifecycle(getView()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseNetSubscriber<BaseEntity<List<StationEntity>>>(NewRescuePresenter.this) {
                               @Override
                               public void onNext(BaseEntity<List<StationEntity>> s) {
                                   if (s.getCode() == 0) {
                                       getView().onLoadStationList(s);
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

    /**
     * 申请救援
     *
     * @param stationId   站点ID
     * @param userId      用户手机
     * @param rescueCause 救援原因
     */
    public void applyRescue(long stationId, String userId, String rescueCause) {
        getModel().applyRescue(stationId, userId, rescueCause)
                .compose(this.bindToLifecycle(getView()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseNetSubscriber<BaseEntity<Object>>(NewRescuePresenter.this) {
                               @Override
                               public void onNext(BaseEntity<Object> s) {
                                   getView().onApplyRescue(s);
                               }

                               @Override
                               public boolean onErr(Throwable e) {
                                   LogUtils.e(e);
                                   return false;
                               }
                           }
                );
    }



}
