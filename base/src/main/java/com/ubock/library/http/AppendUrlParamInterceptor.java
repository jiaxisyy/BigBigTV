package com.ubock.library.http;

import android.text.TextUtils;

import com.ubock.library.base.BaseApp;
import com.ubock.library.utils.AppUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 介绍： url统一追加参数
 * Created by ChenGD on 2017/6/2.
 */
public class AppendUrlParamInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();
        Request.Builder newRequest = request.newBuilder();

        if ("GET".equals(request.method())) {
            newRequest.url(newUrlBuilder(request).build());
        } else if ("POST".equals(request.method())) {
            RequestBody body = request.body();
            if (body != null) {
                if (body instanceof FormBody) {
                    // POST Param x-www-form-urlencoded
                    FormBody formBody = (FormBody) body;
                    Map<String, String> formBodyParamMap = new HashMap<>();
                    int bodySize = formBody.size();
                    boolean hasToken = false;
                    for (int i = 0; i < bodySize; i++) {
                        formBodyParamMap.put(formBody.name(i), formBody.value(i));
                        if (tokenKey.equals(formBody.name(i))) {
                            hasToken = true;
                        }
                    }
                    if (!hasToken) {
                        addToken(formBodyParamMap);
                    }

                    Map<String, String> newFormBodyParamMap = getParamMap();
                    if (newFormBodyParamMap != null) {
                        formBodyParamMap.putAll(newFormBodyParamMap);
                    }
                    FormBody.Builder bodyBuilder = new FormBody.Builder();
                    for (Map.Entry<String, String> entry : formBodyParamMap.entrySet()) {
                        bodyBuilder.add(entry.getKey(), entry.getValue());
                    }
                    newRequest.method(request.method(), bodyBuilder.build());
                } else if (body instanceof MultipartBody) {
                    // POST Param form-data
                    MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
                    MultipartBody multipartBody = (MultipartBody) body;
                    List<MultipartBody.Part> parts = multipartBody.parts();
                    boolean hasToken = false;
                    MultipartBody.Part tokenPart = MultipartBody.Part.createFormData(tokenKey, "null");
                    String tokenPartName = tokenPart.headers().value(0);
                    for (MultipartBody.Part part : parts) {
                        builder.addPart(part);
                        if (part.headers() != null && tokenPartName.equals(part.headers().value(0))) {
                            hasToken = true;
                        }
                    }
                    Map<String, String> extraFormBodyParamMap = getParamMap();
                    if (!hasToken && extraFormBodyParamMap != null) {
                        addToken(extraFormBodyParamMap);
                    }
                    for (Map.Entry<String, String> entry : extraFormBodyParamMap.entrySet()) {
                        builder.addFormDataPart(entry.getKey(), entry.getValue());
                    }
                    newRequest.post(builder.build());
                } else {
                    okio.Buffer buffer = new okio.Buffer();
                    body.writeTo(buffer);
                    String content = buffer.readString(Charset.forName("UTF-8"));
                    if (TextUtils.isEmpty(content)){//body没有内容,说明是没有参数，这里创建一个FormBody，然后向里添加参数
                        FormBody.Builder bodyBuilder = new FormBody.Builder();
                        Map<String, String> formBodyParamMap = new HashMap<>();
                        addToken(formBodyParamMap);
                        Map<String, String> newFormBodyParamMap = getParamMap();
                        if (newFormBodyParamMap != null) {
                            formBodyParamMap.putAll(newFormBodyParamMap);
                        }
                        for (Map.Entry<String, String> entry : formBodyParamMap.entrySet()) {
                            bodyBuilder.add(entry.getKey(), entry.getValue());
                        }
                        newRequest.method(request.method(), bodyBuilder.build());
                    } else {
                        HttpUrl.Builder builder = newUrlBuilder(request);//添加Url参数
                        if (builder != null) {
                            newRequest.url(builder.build());
                        }
//                        try {
//                            JSONObject json = new JSONObject(content);
//                            Map<String, String> extraFormBodyParamMap = getParamMap();
//                            for (Map.Entry<String, String> entry : extraFormBodyParamMap.entrySet()) {
//                                json.put(entry.getKey(), entry.getValue());
//                            }
//                            if (json.isNull(tokenKey)) {
//                                json.put(tokenKey, BaseApp.getInstance().getToken());
//                            }
//                            content = json.toString();
//                        } catch (JSONException e){//不是JSON格式的body，只添加Url参数
//                            LogUtils.e("OkHttp", e);
//                        }
                        newRequest.method(request.method(), RequestBody.create(body.contentType(), content));
                    }
                }
            }
        }
        return chain.proceed(newRequest.build());
    }

    public Map<String, String> getParamMap() {
        Map<String, String> queryParamMap =  new HashMap<>();
//        queryParamMap.put("app_version_code", AppUtils.getVersionCode(BaseApp.getContext())+"");
//        queryParamMap.put("app_version_name", AppUtils.getVersionName(BaseApp.getContext()));
        return queryParamMap;
    }

    private HttpUrl.Builder newUrlBuilder(Request request){
        HttpUrl.Builder newUrlBuilder = request.url().newBuilder();
        Map<String, String> queryParamMap = getParamMap();
        if (request.url().queryParameter(tokenKey) == null){
            if (queryParamMap == null){
                queryParamMap = new HashMap<>();
            }
            addToken(queryParamMap);
        }
        if (queryParamMap != null && !queryParamMap.isEmpty()) {
            for (Map.Entry<String, String> entry : queryParamMap.entrySet()) {
                newUrlBuilder.addQueryParameter(entry.getKey(), entry.getValue());
            }
        }
        return newUrlBuilder;
    }

    private void addToken(Map<String, String> params){
        if (!TextUtils.isEmpty(BaseApp.getInstance().getToken())) {
            params.put(tokenKey, BaseApp.getInstance().getToken());
        }
    }
    private final String tokenKey = "token";
}
