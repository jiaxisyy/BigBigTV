package com.ubock.library.http;

import java.io.IOException;

import com.ubock.library.ui.dialog.ToastExt;
import com.ubock.library.utils.NetworkUtil;
import okhttp3.Interceptor;
import okhttp3.Response;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by ChenGD on 2017/5/19.
 */
public class NoNetworkInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        if (!NetworkUtil.isNetworkEnable()) {
            Observable.just(null).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Object>() {
                @Override
                public void call(Object o) {
                    ToastExt.showExt("未连接网络");
                }
            });
        }
        return chain.proceed(chain.request());
    }
}
