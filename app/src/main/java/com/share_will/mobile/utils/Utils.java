package com.share_will.mobile.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.text.TextUtils;

import java.util.Set;

/**
 * Created by ChenGD on 2018/3/4.
 *
 * @author chenguandu
 */

public class Utils {
    /**
     * 获取当前版号
     */
    public static int getVersionCode(Context ctx) {
        PackageManager packageManager = ctx.getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo = packageManager.getPackageInfo(ctx.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return packageInfo.versionCode;
    }

    /**
     * 获取当前版名
     */
    public static String getVersionName(Context ctx) {
        PackageManager packageManager = ctx.getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo = packageManager.getPackageInfo(ctx.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return packageInfo.versionName;
    }

    /**
     * 获得屏幕的宽度
     *
     * @return
     */
    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 获得屏幕的高度
     *
     * @return
     */
    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 判断是否开启定位, GPS或者AGPS开启一个就认为是开启的
     * @param context
     * @return true 表示开启
     */
    public static final boolean isOPenLocation(final Context context) {
        LocationManager locationManager
                = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        return gps || network;

    }

    /**
     * 判断GPS是否开启
     * @param context
     * @return true 表示开启
     */
    public static final boolean isOPenGPS(final Context context) {
        LocationManager locationManager
                = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return gps;
    }

    public static String getMetaData(Context context, String key){
        try {
            ApplicationInfo info = context.getPackageManager().
                    getApplicationInfo(context.getPackageName(),PackageManager.GET_META_DATA);
            return info.metaData.getString(key);

        }catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 1字节16进制数字直接以字符串输出,如0x1F->1F, 0x0F->0F
     * @param hexValue
     * @return
     */
    public static String hexToString(int hexValue){
        return hexToString(hexValue, 1);
    }

    /**
     * 多字节16进制数字直接以字符串输出,如0x1F->1F, 0x0F->0F,  1FF->01FF
     * @param hexValue
     * @param bytes 字节数
     * @return
     */
    public static String hexToString(int hexValue, int bytes){
        String str = String.format("%x", hexValue);
        str = str.replace("x", "");
        //计算前缀数量，一个字节两位，所以要乘于2
        int prefix = (bytes * 2) - str.length();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < prefix; i++) {
            sb.append("0");
        }

        str = sb.toString() + str;

        return str.toUpperCase();
    }
}
