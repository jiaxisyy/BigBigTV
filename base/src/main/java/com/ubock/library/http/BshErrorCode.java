package com.ubock.library.http;

/**
 * Created by twl on 2016/8/4.
 * 该类是返回错误码的集合管理类
 * (目前只处理 对用户 理解 和体验上有帮助的提示，并进行国际化，其他错误不提示具体原因)
 */
public class BshErrorCode {
    /**
     * 后台给的数据格式出问题了(解析json的时候出问题了)
     */
    public final static int BSH_DATA_FORMAT_ERRO = -1;
    /**伴生活关联账号登录失败 */
    public final static int BSH_RELATION_LOGIN_ERRO = 100;
    /**手机号检查失败*/
    public final static int BSH_MOBILE_ERRO = 11001;
    /**账号或密码错误*/
    public final static int ACOUNT_PWD_ERRO = 11002;
    /**第三方账号已经绑定*/
    public final static int ACOUNT_BINDED_ERROR = 11003;
    /** 第三方登录未绑定 错误码  */
    public final static int OTHER_LOGIN_UNBINDE = 11027;
    /**字符串中包含敏感字符*/
    public final static int BSH_CONTENT_ILLEGAL = 11028;
    /**手机未注册*/
    public final static int BSH_MOBILE_NO_REGIST = 11004;
    /**登录失败 获取多度信息失败*/
    public final static int BSH_LOGIN_FAIL_DOOR = 11006;

    /**验证码输入错误*/
    public final static int BSH_VERCODE_ERRO = 11011;
    /**新密码不能与旧密码相同*/
    public final static int UPDATE_PWD_SAME = 11023;
    /**旧密码不正确*/
    public final static int OLD_PWD_ERRO= 11005;
    /**密码长度错误*/
    public final static int BSH_PWD_LENG_ERRO = 11012;

    /** 手机号已经注册*/
    public final static int BSH_MOBILE_EXIT_ERRO = 11020;
    /**第三方账号已经绑定过用户*/
    public final static int BIND_ACOUNT_ERRO01 = 11029;
    /**该用户已绑定第三方账号*/
    public final static int BIND_ACOUNT_ERRO02 = 11033;
    /** 验证码发送失败*/
    public final static int BSH_VERCODE_SEND_ERRO = 11021;
    /**游客模式不能使用此功能,请注册或登录后再试*/
    public final static int BSH_VISITOR_AUTH_ERRO = 19000;
    /** 支付密码 写错了 */
    public final static int BSH_PAY_PWD_ERRO = 20006;
	/** 请求频率太高*/
	public final static int BSH_REQUEST_OFTEN = 17007;
	/** 请求每日已达上限*/
	public final static int BSH_REQUEST_LIMIT = 17006;
    /**积分不足 */
    public final static int BSH_POINT_NOT_ENOUGH = 20011;
	/**添加好友时 已经是好友关系*/
	public final static int HAS_FRIEND_BING = 11133;
    /**用户掉线错误码*/
    public final static int USER_IS_DIS_LINE = 60426;
    /**用户掉线错误码2*/
    public final static int USER_IS_DIS_LINE2 = 15001;
    /**在对方的黑名单中*/
    public final static int BSH_IN_BLACK_LIST = 60444;
    /**用户发布动态没有权限的bug  授权已过期或删除，请联系物业*/
    public final static int BSH_CIRCLE_NO_AUTH = 72002;

    /**豪力士开门成功*/
    public final static int BSH_HLS_OPEN_SUCCE = 20001;
    /**豪力士开门失败*/
    public final static int BSH_HLS_OPEN_FAIL = 2002;
    /**操作频繁,请稍后再试*/
    public final static int CODE_OFTEN = 15011;
    /**服务端错误*/
    public final static int SERVICE_ERROR = 500;
    /**获取验证码 手机号加密校验不通过**/
    public final static int SINGA_ERROR = 30001;
    /**蓝牙被占用**/
    public final static int BLE_DEVICE_EXIT = 100200;

}
