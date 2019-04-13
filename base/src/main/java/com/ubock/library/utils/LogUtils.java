package com.ubock.library.utils;

import android.text.TextUtils;
import android.util.Log;

import java.io.PrintWriter;
import java.io.StringWriter;

import com.ubock.library.BuildConfig;
import com.ubock.library.base.BaseConfig;


public class LogUtils {

    public static final String DEFAULT_TAG = "LogUtils";

    public static void e(String tag, String msg) {
        if ((BaseConfig.LOG_LEVEL > Log.ERROR) || TextUtils.isEmpty(msg)) {
            return;
        }
        Log.e(tag, msg);

    }

    public static void e(String msg) {
        e(DEFAULT_TAG, msg);
    }

    public static void e(Throwable throwable) {
        e(DEFAULT_TAG, throwable);
    }

    public static void e(String tag, Throwable throwable) {
        StringWriter mStringWriter = new StringWriter();
        PrintWriter mPrintWriter = new PrintWriter(mStringWriter);
        throwable.printStackTrace(mPrintWriter);
        mPrintWriter.close();
        e(tag, mStringWriter.toString());
    }

    public static void d(Throwable throwable) {
        d(DEFAULT_TAG, throwable);
    }

    public static void d(String tag, Throwable throwable) {
        StringWriter mStringWriter = new StringWriter();
        PrintWriter mPrintWriter = new PrintWriter(mStringWriter);
        throwable.printStackTrace(mPrintWriter);
        mPrintWriter.close();
        d(tag, mStringWriter.toString());
    }

    public static void d(String tag, String msg) {
        if ((BaseConfig.LOG_LEVEL > Log.DEBUG) || TextUtils.isEmpty(msg)) {
            return;
        }
        Log.d(tag, msg);

    }

    public static void d(String msg) {
        d(DEFAULT_TAG, msg);
    }

    public static void w(String tag, String msg) {
        if ((BaseConfig.LOG_LEVEL > Log.WARN) || TextUtils.isEmpty(msg)) {
            return;
        }
        Log.w(tag, msg);

    }

    public static void w(String msg) {
        w(DEFAULT_TAG, msg);
    }

    /**
     * TODO 使用Log来显示调试信息,因为log在实现上每个message有4k字符长度限制
     * 所以这里使用自己分节的方式来输出足够长度的message
     *
     * @param tag
     * @param msg void
     */

    public static void debugLongInfo(String tag, String msg) {
        if ((BaseConfig.LOG_LEVEL > Log.DEBUG) || TextUtils.isEmpty(msg)) {
            return;
        }
        msg = msg.trim();
        int index = 0;
        int maxLength = 3500;
        String sub;
        while (index < msg.length()) {
            // java的字符不允许指定超过总的长度end  
            if (msg.length() <= index + maxLength) {
                sub = msg.substring(index);
            } else {
                sub = msg.substring(index, index + maxLength);
            }

            index += maxLength;
            Log.d(tag, sub.trim());

        }

    }

    /**
     * @param str void
     */
    public static void debugLongInfo(String str) {
        debugLongInfo(DEFAULT_TAG, str);
    }

}
