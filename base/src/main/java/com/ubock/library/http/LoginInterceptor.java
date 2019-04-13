package com.ubock.library.http;

import android.content.ComponentName;
import android.content.Intent;

import com.ubock.library.base.BaseApp;
import com.ubock.library.ui.dialog.ToastExt;
import com.ubock.library.utils.LogUtils;
import com.ubock.library.utils.SharedPreferencesUtils;
import com.ubock.library.utils.UiUtils;

import java.io.IOException;
import java.net.HttpURLConnection;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 介绍： 统一处理未登录情况
 */
public class LoginInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        Response response = chain.proceed(request);
        //10007未登录
        if (response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
            BaseApp.getInstance().getGlobalModel().logout();
            Observable.just(null)
                    .observeOn(Schedulers.io())
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Object>() {
                                   @Override
                                   public void call(Object o) {
                                       //TODO 这里弹出登录界面
//                            ToastExt.showExt("未登录");
//                                       Intent intent = new Intent("action_sharewill_login");
                                       Intent intent = new Intent();
                                       intent.setComponent(new ComponentName(UiUtils.getApplicationContext(), "com.share_will.mobile.ui.activity.LoginActivity"));
                                       intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                       intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                                       intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                       UiUtils.getApplicationContext().startActivity(intent);
                                   }
                               },
                            new Action1<Throwable>() {
                                @Override
                                public void call(Throwable throwable) {
                                    LogUtils.e(throwable);
                                }
                            });
        }
        return response;
    }

}
