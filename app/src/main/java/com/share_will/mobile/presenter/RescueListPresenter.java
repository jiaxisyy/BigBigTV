package com.share_will.mobile.presenter;

import com.share_will.mobile.App;
import com.share_will.mobile.model.RescueListModel;
import com.share_will.mobile.model.entity.RescueEntity;
import com.share_will.mobile.ui.views.RescueListView;
import com.ubock.library.base.BaseEntity;
import com.ubock.library.base.BaseNetSubscriber;
import com.ubock.library.base.BasePresenter;
import com.ubock.library.utils.LogUtils;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RescueListPresenter extends BasePresenter<RescueListModel, RescueListView> {
    public RescueListPresenter(RescueListModel model, RescueListView rootView) {
        super(model, rootView);
    }

    /**
     * 获取救援列表
     * @param userId  用户手机
     */
    public void getRescueList(String userId) {
        getModel().getRescueList(userId)
                .compose(this.bindToLifecycle(getView()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseNetSubscriber<BaseEntity<List<RescueEntity>>>(RescueListPresenter.this) {
                               @Override
                               public void onNext(BaseEntity<List<RescueEntity>> s) {
                                   if(s.getCode()==0){
                                       getView().onLoadRescueList(s);
                                   }else {
                                       getView().onLoadRescueList(null);
                                   }

                               }

                               @Override
                               public boolean onErr(Throwable e) {
                                   getView().onLoadRescueList(null);
                                   return super.onErr(e);
                               }
                           }
                );
    }

    /**
     * 取消救援
     * @param id  救援记录ID
     */
    public void cancelRescue(long id) {
        getModel().cancelRescue(id, App.getInstance().getUserId())
                .compose(this.bindToLifecycle(getView()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseNetSubscriber<BaseEntity<Object>>(RescueListPresenter.this) {
                               @Override
                               public void onNext(BaseEntity<Object> s) {
                                   getView().onCancelRescue(s);
                               }
                           }
                );
    }
}
