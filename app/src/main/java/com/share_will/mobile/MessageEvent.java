package com.share_will.mobile;

import android.location.Location;

/**
 * Created by ChenGD on 2018/3/5.
 *
 * @author chenguandu
 */

public class MessageEvent {
    /**
     * 电池信息事件
     */
    public static class BatteryInfo {
        public BatteryInfo(int num, String sn, int use, int sop, boolean online, long time) {
            this.num = num;
            this.sn = sn;
            this.use = use;
            this.sop = sop;
            this.online = online;
            this.time = time;
        }

        public int num;
        public int use;
        public int sop;
        public String sn;
        public boolean online;
        public long time;
    }

    /**
     * 弹出框回调事件
     */
    public static class DialogActivityEvent {
        public DialogActivityEvent(boolean ok) {
            this.ok = ok;
        }

        public boolean ok;
    }

    /**
     * 定位事件
     */
    public static class LocationEvent {
        public LocationEvent(Location location) {
            this.location = location;
        }

        public Location location;
    }

    /**
     * 模拟导航，用于测试
     */
    public static class EmulatorNavi {

    }

    /**
     * 支付结果回调事件
     */
    public static class PayEvent {
        public static int PAY_TYPE_BALANCE = 0;
        public static int PAY_TYPE_WEIXIN = 1;
        public static int PAY_TYPE_ALIPAY = 2;
        public int code;
        //支付类型：0：余额支付; 1:微信支付; 2支付宝支付
        public int payType;
        public String message;

        public PayEvent(int code, int payType, String message) {
            this.code = code;
            this.payType = payType;
            this.message = message;
        }
    }
}
