package com.share_will.mobile.presenter;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.share_will.mobile.App;
import com.share_will.mobile.model.HomeModel;
import com.share_will.mobile.model.entity.CabinetEntity;
import com.share_will.mobile.model.entity.CityEntity;
import com.share_will.mobile.model.entity.NotifyMessageEntity;
import com.share_will.mobile.ui.views.HomeView;
import com.ubock.library.base.BaseEntity;
import com.ubock.library.base.BaseNetSubscriber;
import com.ubock.library.base.BasePresenter;
import com.ubock.library.ui.dialog.ToastExt;
import com.ubock.library.utils.LogUtils;

import java.util.List;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class HomePresenter extends BasePresenter<HomeModel, HomeView> {

    public HomePresenter(HomeModel model, HomeView rootView) {
        super(model, rootView);
    }

    /**
     * 获取机柜列表
     *
     * @param city 城市区号
     */
    public void getCabinetList(String city, double longitude, double latitude) {
        getModel().getCabinetList(city, longitude, latitude)
                .compose(this.bindToLifecycle(getView()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseNetSubscriber<BaseEntity<List<CabinetEntity>>>(HomePresenter.this) {
                               @Override
                               public void onNext(BaseEntity<List<CabinetEntity>> s) {
                                   if (s.getCode() == 0) {
                                       getView().onLoadCabinetList(s);
                                   } else {
                                       getView().showMessage(s.getMessage());
                                       getView().onLoadCabinetList(null);
                                   }
                               }

                               @Override
                               public boolean onErr(Throwable e) {
                                   getView().onLoadCabinetList(null);
                                   LogUtils.e(e);
                                   return true;
                               }
                           }
                );
    }

    /**
     * 获取可换电池数量
     *
     * @param sn 机柜sn
     */
    public void getFullBattery(String sn) {
        getModel().getFullBattery(sn)
                .compose(this.bindToLifecycle(getView()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseNetSubscriber<BaseEntity<Map<String, Integer>>>(HomePresenter.this) {
                               @Override
                               protected boolean showLoading() {
                                   return false;
                               }

                               @Override
                               public void onNext(BaseEntity<Map<String, Integer>> s) {
                                   if (s.getCode() == 0) {
                                       getView().onLoadFullBattery(s);
                                   } else {
                                       getView().showMessage(s.getMessage());
                                       getView().onLoadFullBattery(null);
                                   }
                               }

                               @Override
                               public boolean onErr(Throwable e) {
                                   getView().onLoadFullBattery(null);
                                   LogUtils.e(e);
                                   return true;
                               }
                           }
                );
    }

    /**
     * 扫码换电
     *
     * @param userId    账号
     * @param cabinetId 机柜sn
     * @param time      时间截
     */
    public void scanCodeLogin(String userId, String cabinetId, String time) {
        getModel().scanCodeLogin(userId, cabinetId, time, 1)
                .compose(this.bindToLifecycle(getView()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseNetSubscriber<BaseEntity<Object>>(HomePresenter.this) {

                               @Override
                               public void onNext(BaseEntity<Object> s) {
                                   getView().onScanCode(s);
                               }

                               @Override
                               public boolean onErr(Throwable e) {
                                   getView().onScanCode(null);
                                   LogUtils.e(e);
                                   return true;
                               }
                           }
                );
    }

    /**
     * 处理从浏览器跳转过来的情况
     *
     * @param intent
     */
    public void parseUri(Intent intent) {
        if (intent == null) {
            return;
        }
        Uri uri = intent.getData();
        if (uri != null) {
            String rawPath = uri.getPath();
            Log.d("cgd", "raw path = " + rawPath);
            parsePath(rawPath);
        } else {
            int page = intent.getIntExtra("page", -1);
            if (page > -1 && page < getView().getTabSize()) {
                getView().goTo(page);
            }
        }
    }

    private void parsePath(String rawPath) {
        if (!TextUtils.isEmpty(rawPath)) {
            rawPath = rawPath.substring(1);
            String[] params = rawPath.split("/");
            if (params.length < 2) {
                return;
            }

            String sn = params[0];
            String time = params[1];
            int channel = 0;
            if (params.length > 2) {
                try {
                    channel = Integer.parseInt(params[2]);
                } catch (Exception e) {
                }
            }

            Log.d("cgd", String.format("SN = %s, time = %s, channel = %d", sn, time, channel));
            goToChargingOrSwitch(sn, time, channel);
        }
    }

    /**
     * 换电
     */
    private void switchBattery(String sn, String time) {
        LogUtils.d("扫码换电测试3");
        scanCodeLogin(App.getInstance().getUserId(), sn, time);
    }

    /**
     * 扫码结果处理
     *
     * @param scanResult
     */
    public void handleScanResult(String scanResult) {
        if (!TextUtils.isEmpty(scanResult)) {
            if ("0000000000000000".equals(scanResult)) {
                ToastExt.showExt("交易进行中,请稍候");
                return;
            }
            Log.d("cgd", String.format("scan result = %s", scanResult));
            Uri uri = Uri.parse(scanResult);
            String sn = uri.getQueryParameter("sn");
            final String time = uri.getQueryParameter("time");
            int channel = 0;
            try {
                channel = Integer.parseInt(uri.getQueryParameter("channel"));
            } catch (Exception e) {
            }
            Log.d("cgd", String.format("SN = %s; time = %s; channel = %d", sn, time, channel));
            goToChargingOrSwitch(sn, time, channel);
        }
    }

    /**
     * 跳到到换电或者充电流程
     *
     * @param sn      机柜SN
     * @param time    二维码生成时间截，二维码有效时长为10分钟
     * @param channel 充电通道
     */
    private void goToChargingOrSwitch(String sn, String time, int channel) {
        LogUtils.d("扫码换电测试2");
        if (TextUtils.isEmpty(sn) || TextUtils.isEmpty(time)) {
            ToastExt.showExt("无效二维码");
            return;
        }
        if (sn.startsWith("C") || sn.startsWith("c")) {
            //充电
            getView().chargingBattery(sn, channel);
        } else {
            //换电
            switchBattery(sn, time);
        }
    }

    /**
     * 获取已开通城市列表
     */
    public void getCityList() {
        getModel().getCityList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseNetSubscriber<BaseEntity<List<CityEntity>>>(HomePresenter.this) {

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
     * 获取每日提示
     *
     * @param userId
     */
    public void getNotifyMessage(String userId) {
        getModel().getNotifyMessage(userId)
                .compose(this.bindToLifecycle(getView()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseNetSubscriber<BaseEntity<NotifyMessageEntity>>(HomePresenter.this) {
                               @Override
                               public void onNext(BaseEntity<NotifyMessageEntity> s) {
                                   if (s.getCode() == 0) {
                                       getView().showNotifyMessage(true, s.getData());
                                   } else {

                                       getView().showNotifyMessage(false, null);
                                   }
                               }

                               @Override
                               public boolean onErr(Throwable e) {
                                   getView().showNotifyMessage(false, null);
                                   LogUtils.e(e);
                                   return true;
                               }
                           }
                );
    }

}
