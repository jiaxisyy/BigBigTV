package com.ubock.library.http;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ubock.library.base.BaseApp;
import com.ubock.library.base.BaseConfig;
import com.ubock.library.common.ListTConverterFactory;
import com.ubock.library.common.gson.DoubleTypeAdapter;
import com.ubock.library.common.gson.FloatTypeAdapter;
import com.ubock.library.common.gson.IntegerTypeAdapter;
import com.ubock.library.common.gson.LongTypeAdapter;
import com.ubock.library.common.gson.StringTypeAdapter;
import com.ubock.library.event.Events;
import com.ubock.library.utils.LogUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by ChenGD on 2017/4/17.
 */

public class ApiClient {

    private static OkHttpClient mOkHttpClient = null;
    private static Retrofit mRetrofit;
    private ConcurrentHashMap<Class<?>, Object> mApiServiceList = new ConcurrentHashMap<>();

    public static ApiClient getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final ApiClient INSTANCE = new ApiClient();
    }

    private ApiClient() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(BaseConfig.API_SERVER_URL)
                //字符串转换器
                .addConverterFactory(ScalarsConverterFactory.create())
                //设置 Json 转换器
                .addConverterFactory(GsonConverterFactory.create(buildGson()))
                //RxJava 适配器
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(ListTConverterFactory.create())
                .client(getOkhttpClient())
                .build();
    }

    public <T> T getApiService(Class<T> service) {
        T apiService = (T) mApiServiceList.get(service);
        if (apiService == null) {
            synchronized (this) {
                apiService = mRetrofit.create(service);
                mApiServiceList.put(service, apiService);
            }
        }
        return apiService;
    }

    private Gson buildGson() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Integer.class, new IntegerTypeAdapter())
                .registerTypeAdapter(int.class, new IntegerTypeAdapter())
                .registerTypeAdapter(Double.class, new DoubleTypeAdapter())
                .registerTypeAdapter(double.class, new DoubleTypeAdapter())
                .registerTypeAdapter(Long.class, new LongTypeAdapter())
                .registerTypeAdapter(long.class, new LongTypeAdapter())
                .registerTypeAdapter(Float.class, new FloatTypeAdapter())
                .registerTypeAdapter(float.class, new FloatTypeAdapter())
                .create();
        return gson;
    }

    private OkHttpClient getOkhttpClient() {
        if (mOkHttpClient == null) {
            initOkHttpClient();
        }
        return mOkHttpClient;
    }

    private UbockLoggingInterceptor.Logger logger = new UbockLoggingInterceptor.Logger() {
        @Override
        public void log(String message) {
            LogUtils.debugLongInfo("OkHttp", message);
        }
    };

    private void initOkHttpClient() {

        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        builder.addInterceptor(new NoNetworkInterceptor());
        builder.addInterceptor(new AppendUrlParamInterceptor());
        builder.addInterceptor(new LoginInterceptor());

        if (BaseConfig.LOG_LEVEL <= Log.DEBUG) {
            // Log信息拦截器
            UbockLoggingInterceptor loggingInterceptor = new UbockLoggingInterceptor(logger);
            loggingInterceptor.setLevel(UbockLoggingInterceptor.Level.BODY);
            //设置 Debug Log 模式
            builder.addInterceptor(loggingInterceptor);
        }
        //设置超时
        builder.connectTimeout(15, TimeUnit.SECONDS);
        builder.readTimeout(15, TimeUnit.SECONDS);
        builder.writeTimeout(60, TimeUnit.SECONDS);
        //错误重连
        builder.retryOnConnectionFailure(false);
        //https支持
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(getCerts(), null, null);
        builder.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);

        builder.cookieJar(new CookieJarImpl());

        mOkHttpClient = builder.build();
    }

    /**
     * Cookie管理类
     */
    private static class CookieJarImpl implements CookieJar {
        private final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();

        @Override
        public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
            cookieStore.put(url.host(), cookies);

        }

        @Override
        public List<Cookie> loadForRequest(HttpUrl url) {
            List<Cookie> cookies = cookieStore.get(url.host());
            return cookies != null ? cookies : new ArrayList<Cookie>();
        }
    }

    private InputStream[] getCerts() {
        String rootDir = "certs";
        ArrayList<InputStream> certs = new ArrayList<>();
        try {
            String[] files = BaseApp.getInstance().getAssets().list(rootDir);
            if (files != null) {
                int num = files.length;
                for (int i = 0; i < num; i++) {
                    InputStream certificates = BaseApp.getInstance().getAssets().open(String.format("%s/%s", rootDir, files[i]));
                    if (certificates != null) {
                        certs.add(certificates);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return certs.toArray(new InputStream[0]);
    }

}
