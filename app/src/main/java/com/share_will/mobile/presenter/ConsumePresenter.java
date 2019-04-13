package com.share_will.mobile.presenter;

import com.share_will.mobile.model.ConsumeModel;
import com.share_will.mobile.model.UserCenterModel;
import com.share_will.mobile.model.entity.RecordEntity;
import com.share_will.mobile.model.entity.UserInfo;
import com.share_will.mobile.ui.views.ConsumeView;
import com.share_will.mobile.ui.views.UserCenterView;
import com.ubock.library.base.BaseEntity;
import com.ubock.library.base.BaseNetSubscriber;
import com.ubock.library.base.BasePresenter;
import com.ubock.library.utils.LogUtils;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ConsumePresenter extends BasePresenter<ConsumeModel, ConsumeView> {
    public ConsumePresenter(ConsumeModel model, ConsumeView rootView) {
        super(model, rootView);
    }

    /**
     * 获取消费记录
     * @param userId
     */
    public void getConsumeList(String userId, final int page, int size) {
        getModel().getConsumeList(userId, page, size)
                .compose(this.bindToLifecycle(getView()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseNetSubscriber<BaseEntity<List<RecordEntity>>>(ConsumePresenter.this) {
                    @Override
                    protected boolean showLoading() {
                        return page == 1;
                    }

                    @Override
                       public void onNext(BaseEntity<List<RecordEntity>> s) {
                           getView().onLoadConsumeList(s);
                       }

                       @Override
                       public boolean onErr(Throwable e) {
                           getView().onLoadConsumeList(null);
                           LogUtils.e(e);
                           return true;
                       }
                   }
                );
    }
}
