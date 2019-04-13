package com.ubock.library.http;

import android.content.Context;
import android.text.TextUtils;

import com.ubock.library.base.BaseApp;
import com.ubock.library.base.BaseEntity;
import com.ubock.library.ui.dialog.ToastExt;
import com.ubock.library.utils.LogUtils;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import org.json.JSONException;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;

import okhttp3.ResponseBody;
import retrofit2.HttpException;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * @author ChenGuandu
 * @description 网络异常捕获、状态码统一处理
 * @since 2018/1/23.
 */

public class HttpExceptionHandle {
    private static final int BSH_ERROR = 400;
    private static final int UNAUTHORIZED = 401;
    private static final int FORBIDDEN = 403;
    private static final int NOT_FOUND = 404;
    private static final int REQUEST_TIMEOUT = 408;
    private static final int UNLOGIN = 426;
    private static final int INTERNAL_SERVER_ERROR = 500;
    private static final int BAD_GATEWAY = 502;
    private static final int SERVICE_UNAVAILABLE = 503;
    private static final int GATEWAY_TIMEOUT = 504;

    /**
     * 根据协议目前只有 http code = 400 时返回的json含有错误码,其他情况走标准http code
     */
    public static void handleException(Throwable e) {
        Context context = BaseApp.getInstance();
        String message = context.getString(getStringId(context, "unknown_error"));
        //http code为400时经过内部定义转换，不是标准的http code
        if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            switch (httpException.code()) {
                //400在CustomError中处理
                case UNLOGIN:
                    message = context.getString(getStringId(context, "bsh_user_visitor_erro"));
                    break;
                case BSH_ERROR:
//                    ResponseBody responseBody = ((HttpException) e).response().errorBody();
//                    if (responseBody != null){
//                        try {
//                            String body = responseBody.string();
//                            Gson gson = new Gson();
//                            BaseEntity baseEntity = gson.fromJson(body, BaseEntity.class);
//                            if (baseEntity != null) {
//                                message = getFailStr(baseEntity.getCode(), context.getString(getStringId(context, "unknown_error")));
//                            }
//                        } catch (IOException e1) {
//                            LogUtils.e(e1);
//                        }
//                    }
//                    break;
                case UNAUTHORIZED:
                case FORBIDDEN:
                case NOT_FOUND:
                case REQUEST_TIMEOUT:
                case GATEWAY_TIMEOUT:
                case INTERNAL_SERVER_ERROR:
                case BAD_GATEWAY:
                case SERVICE_UNAVAILABLE:
                default:
                    message = context.getString(getStringId(context, "net_error"));
                    break;
            }
        } else if (e instanceof SocketTimeoutException) {
            message = context.getString(getStringId(context, "connect_timeout"));
        } else if (e instanceof JsonParseException || e instanceof JSONException) {
            message = context.getString(getStringId(context, "data_parse_error"));
        } else if (e instanceof ConnectException) {
            message = context.getString(getStringId(context, "connect_server_error"));
        } else if (e instanceof javax.net.ssl.SSLHandshakeException) {
            message = context.getString(getStringId(context, "cer_verify_error"));
        } else {
            message = context.getString(getStringId(context, "unknown_error"));
        }
        if (!TextUtils.isEmpty(message)) {
            showError(message);
        }
    }

    private static void showError(final String message){
        Observable.just(null).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Object>() {
            @Override
            public void call(Object o) {
                ToastExt.showExt(message);
            }
        });
    }


    /**
     * 返回 错误码对应的提示
     * @param stateCode
     * @param def
     * @return
     */
    public static String getFailStr(int stateCode, String def){
        Context context = BaseApp.getInstance();
        def = def==null?"":def;
        if (stateCode == 0) return def;
        String failStr = null;
        int failCode = 0;
        if (TextUtils.isEmpty(failStr)) failStr = def;
        switch (stateCode){
            case BshErrorCode.BSH_MOBILE_ERRO:{
                failCode = getStringId(context, "bsh_mobile_erro");
                break;
            }
            case BshErrorCode.BSH_MOBILE_EXIT_ERRO:{
                failCode = getStringId(context, "bsh_mobile_exit_erro");
                break;
            }
            case BshErrorCode.BSH_VERCODE_ERRO:{
                failCode = getStringId(context, "bsh_vercode_erro");
                break;
            }
            case BshErrorCode.BSH_VERCODE_SEND_ERRO:{
                failCode = getStringId(context, "bsh_vercode_send_erro");
                break;
            }
            case BshErrorCode.BSH_PWD_LENG_ERRO:{
                failCode = getStringId(context, "bsh_pwd_erro_01");
                break;
            }
            case BshErrorCode.BSH_MOBILE_NO_REGIST:{
                failCode = getStringId(context, "bsh_mobile_erro02");
                break;
            }
            case BshErrorCode.ACOUNT_PWD_ERRO:{
                failCode = getStringId(context, "acount_pwd_erro");
                break;
            }
            case BshErrorCode.BSH_CONTENT_ILLEGAL:{
                failCode = getStringId(context, "bsh_content_illegal");
                break;
            }
            case BshErrorCode.BSH_PAY_PWD_ERRO:{
                failCode = getStringId(context, "bsh_pay_pwd_erro");
                break;
            }
            case BshErrorCode.UPDATE_PWD_SAME:{
                failCode = getStringId(context, "update_pwd_erro_01");
                break;
            }
            case BshErrorCode.OLD_PWD_ERRO:{
                failCode = getStringId(context, "update_pwd_erro_02");
                break;
            }
            case BshErrorCode.ACOUNT_BINDED_ERROR:
            case BshErrorCode.BIND_ACOUNT_ERRO01:{
                failCode = getStringId(context, "bind_acount_erro01");
                break;
            }

            case BshErrorCode.BIND_ACOUNT_ERRO02:{
                failCode = getStringId(context, "bind_acount_erro02");
                break;
            }
            case BshErrorCode.BSH_VISITOR_AUTH_ERRO:{
                failCode = getStringId(context, "bsh_user_visitor_erro");
                break;
            }
            case BshErrorCode.BSH_REQUEST_OFTEN:
            case BshErrorCode.CODE_OFTEN:{
                failCode = getStringId(context, "bsh_request_often");
                break;
            }
            case BshErrorCode.BSH_REQUEST_LIMIT:{
                failCode = getStringId(context, "bsh_request_limit");
                break;
            }
            case BshErrorCode.BSH_POINT_NOT_ENOUGH:{
                failCode = getStringId(context, "bsh_not_enaugh_ponit");
                break;
            }
            case BshErrorCode.HAS_FRIEND_BING:{
                failCode = getStringId(context, "has_bing_friend");
                break;
            }
            case BshErrorCode.BSH_IN_BLACK_LIST:{
                failCode = getStringId(context, "bsh_in_black_list");
                break;
            }
            case BshErrorCode.BSH_CIRCLE_NO_AUTH:{
                failCode = getStringId(context, "bsh_grant_erro");
                break;
            }
            case BshErrorCode.BSH_LOGIN_FAIL_DOOR:
            case BshErrorCode.BSH_RELATION_LOGIN_ERRO:{
                failCode = getStringId(context, "login_fail_door");
                break;
            }
            case BshErrorCode.SERVICE_ERROR:{
                failCode = getStringId(context, "service_error");
                break;
            }
            case BshErrorCode.SINGA_ERROR:{
                failCode = getStringId(context, "signa_error");
                break;
            }
            case BshErrorCode.BLE_DEVICE_EXIT:{
                failCode = getStringId(context, "signa_error");
                break;
            }
        }
        if (failCode!=0) failStr = context.getString(failCode);
        return failStr;
    }

    private static int getStringId(Context context, String name){
        return context.getResources().getIdentifier(name, "string", context.getPackageName());
    }

}
