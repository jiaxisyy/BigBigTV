package com.share_will.mobile.presenter;

import com.share_will.mobile.model.ChargeRecordModel;
import com.share_will.mobile.model.entity.ChargeRecordEntity;
import com.share_will.mobile.ui.views.IChargeRecordView;
import com.ubock.library.base.BaseEntity;
import com.ubock.library.base.BaseNetSubscriber;
import com.ubock.library.base.BasePresenter;
import com.ubock.library.utils.LogUtils;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ChargeRecordPresenter extends BasePresenter<ChargeRecordModel, IChargeRecordView> {
    public ChargeRecordPresenter(ChargeRecordModel model, IChargeRecordView rootView) {
        super(model, rootView);
    }

    public void getChargeRecordList(String userId, int pn, int ps) {
        getModel().getChargeRecordList(userId, pn, ps)
                .compose(this.bindToLifecycle(getView()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseNetSubscriber<BaseEntity<List<ChargeRecordEntity>>>(ChargeRecordPresenter.this) {
                               @Override
                               public void onNext(BaseEntity<List<ChargeRecordEntity>> s) {
                                   if (s.getCode() == 0) {
                                       getView().onLoadChargeRecordResult(s);
                                   } else {
                                       getView().onLoadChargeRecordResult(null);
                                   }
                               }

                               @Override
                               public boolean onErr(Throwable e) {
                                   getView().onLoadChargeRecordResult(null);
                                   LogUtils.e(e);
                                   return true;
                               }
                           }
                );
    }


}
