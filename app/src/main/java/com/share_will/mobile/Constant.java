package com.share_will.mobile;

import java.util.HashMap;
import java.util.Map;

public class Constant {

    //渠道
    public final static String CHANNEL = "CHANNEL";

    //用户信息
    public final static String USER_INFO = "UserInfo";

    //服务器协议
    public final static String KEY_SERVER_PROTOCOL = "server_protocol";

    //服务器地址
    public final static String KEY_SERVER_ADDRESS = "server_address";

    //服务器端口号
    public final static String KEY_SERVER_PORT = "server_port";

    //服务器项目名称
    public final static String KEY_SERVER_PROJECT = "server_project";

    //蚁电渠道
    public final static String KEY_CHANNEL_ANT = "zhili";

    //春藤
    public final static String KEY_CHANNEL_MOSS = "shenzhen-chunteng";

    //智锂渠道
    public final static String KEY_CHANNEL_SMARTBMS = "smartbms";

    //微信支付信息
    public static Map<String, String> WEIXIN_APP_INFO = new HashMap<>();
    static {
        //蚁电
        //appid
        WEIXIN_APP_INFO.put("zhili_APP_ID", "wx6065fed506b579c6");

        //智锂
        WEIXIN_APP_INFO.put("smartbms_APP_ID", "wx6065fed506b579c6");

        //春藤
        WEIXIN_APP_INFO.put("shenzhen-chunteng_APP_ID", "wx6065fed506b579c6");
    }

    //log级别
    public final static String KEY_LOG_LEVEL = "log_level";

    //已读用户协议
    public final static String KEY_USER_PROTOCOL_READ = "user_protocol_read";

    //救援审核状态
    public static Map<Integer, String> RescueStatus = new HashMap<>();
    static {
        RescueStatus.put(0, "审批中");
        RescueStatus.put(1, "审核通过、待取电");
        RescueStatus.put(2, "已取电、待归还");
        RescueStatus.put(3, "已归还");
        RescueStatus.put(4, "已取消救援");
        RescueStatus.put(5, "审核拒绝");
    }

    //押金退款审核状态
    //-1:正常,没有申请退款 0:正在退款中 1:退款成功 2:拒绝退款 3:取消退款
    public static Map<Integer, String> RefundStatus = new HashMap<>();
    static {
        RefundStatus.put(-1, "正常");
        RefundStatus.put(0, "正在退款中");
        RefundStatus.put(1, "退款成功");
        RefundStatus.put(2, "拒绝退款");
        RefundStatus.put(3, "取消退款");
    }


}
