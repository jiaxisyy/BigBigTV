package com.share_will.mobile.presenter;

import com.share_will.mobile.model.RegisterModel;
import com.share_will.mobile.model.entity.CityEntity;
import com.share_will.mobile.model.entity.StationEntity;
import com.share_will.mobile.ui.views.RegisterView;
import com.ubock.library.base.BaseEntity;
import com.ubock.library.base.BaseNetSubscriber;
import com.ubock.library.base.BasePresenter;
import com.ubock.library.utils.LogUtils;
import com.ubock.library.utils.MD5Util;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RegisterPresenter extends BasePresenter<RegisterModel, RegisterView> {
    public RegisterPresenter(RegisterModel model, RegisterView rootView) {
        super(model, rootView);
    }

    /**
     * 注册
     *
     * @param userId   账号/手机号
     * @param userName 姓名
     * @param password 密码
     * @param verCode  验证码
     */
    public void register(String userId, String userName, String password, String verCode, String customer, String stationId) {
        password = MD5Util.MD5(MD5Util.MD5(password));
        getModel().register(userId, userName, password, verCode, customer, stationId)
                .compose(this.bindToLifecycle(getView()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseNetSubscriber<BaseEntity<Object>>(RegisterPresenter.this) {
                               @Override
                               public void onNext(BaseEntity<Object> s) {
                                   if (s.getCode() == 0) {
                                       getView().onRegister(true, "注册成功");
                                   } else {
                                       getView().onRegister(false, s.getMessage());
                                   }
                               }

                               @Override
                               public boolean onErr(Throwable e) {
                                   getView().onRegister(false, "注册失败");
                                   LogUtils.e(e);
                                   return true;
                               }
                           }
                );
    }

    /**
     * 发送验证码
     *
     * @param userId 手机号
     */
    public void sendVerifyCode(String userId) {
        getModel().sendVerifyCode(userId)
                .compose(this.bindToLifecycle(getView()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseNetSubscriber<BaseEntity<Object>>(RegisterPresenter.this) {
                               @Override
                               public void onNext(BaseEntity<Object> s) {
                                   if (s.getCode() == 0) {
                                       getView().onSendVerifyCode(true, "验证码已发送，请注意查收");
                                   } else {
                                       getView().onSendVerifyCode(false, s.getMessage());
                                   }
                               }

                               @Override
                               public boolean onErr(Throwable e) {
                                   getView().onSendVerifyCode(false, "验证码发送失败");
                                   LogUtils.e(e);
                                   return true;
                               }
                           }
                );
    }

    /**
     * 获取已开通城市列表
     */
    public void getCityList() {
        getModel().getCityList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseNetSubscriber<BaseEntity<List<CityEntity>>>(RegisterPresenter.this) {

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
     * @param type 用户类型 type :0.B端骑手，1.C端个人
     */
    public void getStationList(String areaCode,int type) {
        getModel().getStationList(areaCode,type)
                .compose(this.bindToLifecycle(getView()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseNetSubscriber<BaseEntity<List<StationEntity>>>(RegisterPresenter.this) {
                               @Override
                               public void onNext(BaseEntity<List<StationEntity>> s) {
                                   if (s.getCode() == 0) {
                                       getView().onLoadStationList(s);
                                   } else {
                                       getView().onLoadStationList(null);
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
     * 通过站长手机号获取站点
     *
     * @param phone 站长手机号
     */
    public void getStationForPhone(String phone) {

        getModel().getStationForPhone(phone)
                .compose(this.bindToLifecycle(getView()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseNetSubscriber<BaseEntity<List<StationEntity>>>(RegisterPresenter.this) {
                    @Override
                    public void onNext(BaseEntity<List<StationEntity>> listBaseEntity) {
                        if (listBaseEntity.getCode() == 0 && listBaseEntity.getData() != null) {
                            getView().onLoadStationForPhone(listBaseEntity);
                        } else {
                            getView().showMessage("查询失败");
                        }
                    }

                    @Override
                    public boolean onErr(Throwable e) {
                        getView().showMessage("查询失败");
                        return super.onErr(e);
                    }
                });


    }

}
