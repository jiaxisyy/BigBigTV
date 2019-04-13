package com.ubock.library.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

import com.ubock.library.base.BaseApp;


/**
 * 网络工具类
 * Created by ChenGD on 2017/3/13.
 */
public class NetworkUtil {

    public final static int NETWORK_NO_CONNECT = -1;            //没有网络
    public final static int NETWORK_WIFI = 1;                //Wifi网络
    public final static int NETWORK_3G = 2;                    //3G网络
    public final static int NETWORK_OTHER = 3;                //其他网络

    public static int getNetworkType(Context context) {
        if (null != context) {
            ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo == null || !networkInfo.isConnected()) {
                return -1;
            }
            return networkInfo.getType();
        }
        return NETWORK_NO_CONNECT;
    }


    /**
     * 是否联网
     *
     * @return
     */
    public static boolean isNetworkEnable() {
        ConnectivityManager cm = (ConnectivityManager) BaseApp.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (null == cm || (null != cm && null == cm.getActiveNetworkInfo())) {
            return false;
        }
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info == null) {
            return false;
        }
        return info.isAvailable();
    }

    /**
     * ping server是否可用
     * @param server
     * @return
     */
    public static boolean isNetworkEnable(String server) {
        try {
            Process process = Runtime.getRuntime().exec("/system/bin/ping -c 2 -w 10 " + server);
            int status = process.waitFor();
            if (status == 0) {
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 用来获取手机拨号上网（包括CTWAP和CTNET）时由PDSN分配给手机终端的源IP地址。
     *
     * @return
     * @author SHANHY
     */
    public static String getPsdnIp() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (Exception e) {
        }
        return "";
    }

}
