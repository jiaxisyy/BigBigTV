package com.ubock.library.base;

import android.util.Log;

/**
 * Created by ChenGD on 2018/03/20.
 */

public class BaseConfig {

    /**
     * 默认Log级别为错误级别，控制台输出的日志级别，
     */
    public static int LOG_LEVEL = Log.ERROR;

    /**
     * 接口版本
     */
    public final static String API_VERSION = "v7";

    /**
     *  默认服务器地址
     */
//    public static String DEFAULT_SERVER_HOST = "http://services.smart-moss.com/";
    public static String DEFAULT_SERVER_HOST = "http://www.ep-ai.com/";
//    public static String DEFAULT_SERVER_HOST = "http://bigdata.share-will.com/";
    public static String SERVER_HOST = DEFAULT_SERVER_HOST;
    /**
     * 项目名称
     */
    public static String PROJECT_NAME = "4GAgreement/";
//    public static String PROJECT_NAME = "ct-moss/";

    /**
     * 服务器地址
     */
    public static String API_SERVER_URL = SERVER_HOST + PROJECT_NAME;

    /**
     * 两次返回退出应用时间间隔，单位毫秒
     */
    public static int exitAppInterval = 3000;

    /**
     * 科大讯飞应用appid
     */
    public static final String IFLYTEK_APPID = "5adfdda9";

    /**
     * 过滤输出log的接口名称，在ApiService中的接口列表中取，如：uploadError,默认没有，即全部输出
     */
    public static String LogInterface = null;

}
