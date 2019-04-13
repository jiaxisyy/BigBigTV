package com.share_will.mobile.presenter;

import com.share_will.mobile.model.ShopModel;
import com.share_will.mobile.model.entity.PackageEntity;
import com.share_will.mobile.model.entity.PackageOrderEntity;
import com.share_will.mobile.ui.views.ShopView;
import com.ubock.library.annotation.PresenterInjector;
import com.ubock.library.base.BaseEntity;
import com.ubock.library.base.BaseNetSubscriber;
import com.ubock.library.base.BasePresenter;
import com.ubock.library.utils.LogUtils;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ShopPresenter extends BasePresenter<ShopModel, ShopView> {
    public ShopPresenter(ShopModel model, ShopView rootView) {
        super(model, rootView);
    }

    /**
     * 获取套餐列表
     * @param userId
     */
    public void getPackageList(String userId) {
        getModel().getPackageList(userId)
                .compose(this.bindToLifecycle(getView()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseNetSubscriber<BaseEntity<List<PackageEntity>>>(ShopPresenter.this) {

                    @Override
                       public void onNext(BaseEntity<List<PackageEntity>> s) {
                           getView().onLoadPackageList(s);
                       }

                       @Override
                       public boolean onErr(Throwable e) {
                           getView().onLoadPackageList(null);
                           LogUtils.e(e);
                           return true;
                       }
                   }
                );
    }

    /**
     * 生成套餐订单
     * @param userId
     */
    public void createPackageOrder(String userId, long packageId, long activityId, int money, int type, final String packageName) {
        getModel().createPackageOrder(userId, packageId, activityId, money, type)
                .compose(this.bindToLifecycle(getView()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseNetSubscriber<BaseEntity<PackageOrderEntity>>(ShopPresenter.this) {

                       @Override
                       public void onNext(BaseEntity<PackageOrderEntity> s) {
                           if (s.getCode() == 0 && s.getData() != null){
                               getView().onCreateOrder(true,s.getData().getOrderId(), s.getMessage());
                           } else {
                               getView().onCreateOrder(false, null, s.getMessage());
                           }
                       }

                       @Override
                       public boolean onErr(Throwable e) {
                           getView().onCreateOrder(false, null,"生成订单失败");
                           LogUtils.e(e);
                           return true;
                       }
                   }
                );
    }

}
