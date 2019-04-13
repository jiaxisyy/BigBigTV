package com.share_will.mobile.alipay;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;

import com.alipay.sdk.app.PayTask;
import com.share_will.mobile.MessageEvent;
import com.share_will.mobile.utils.ThreadPools;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

/**
 * Created by Chenguandu on 2018/10/25.
 */
public class AlipayUtils {
    /**
     * APPID,后台已经设置，这里不需要传
     */
    public static final String APP_ID = null;
    private Activity mActivity;

    public AlipayUtils(Activity activity){
        mActivity = activity;
    }

    public void pay(String orderInfo){
        Runnable payRunnable = () -> {
            PayTask alipay = new PayTask(mActivity);
            Map<String, String> result = alipay.payV2(orderInfo, true);
            Log.i("msp", result.toString());
            PayResult payResult = new PayResult(result);
            /**
             对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
             */
            String resultInfo = payResult.getResult();// 同步返回需要验证的信息
            String resultStatus = payResult.getResultStatus();
            MessageEvent.PayEvent payEvent;
            // 判断resultStatus 为9000则代表支付成功
            if (TextUtils.equals(resultStatus, "9000")) {
                // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                payEvent = new MessageEvent.PayEvent(0, MessageEvent.PayEvent.PAY_TYPE_ALIPAY, "支付成功");
            } else if (TextUtils.equals(resultStatus, "4000")) {
                payEvent = new MessageEvent.PayEvent(1, MessageEvent.PayEvent.PAY_TYPE_ALIPAY, "订单支付失败");
            } else if (TextUtils.equals(resultStatus, "5000")) {
                payEvent = new MessageEvent.PayEvent(1, MessageEvent.PayEvent.PAY_TYPE_ALIPAY, "重复请求");
            } else if (TextUtils.equals(resultStatus, "6001")) {
                payEvent = new MessageEvent.PayEvent(1, MessageEvent.PayEvent.PAY_TYPE_ALIPAY, "已取消支付");
            } else if (TextUtils.equals(resultStatus, "6002")) {
                payEvent = new MessageEvent.PayEvent(1, MessageEvent.PayEvent.PAY_TYPE_ALIPAY, "网络连接出错");
            } else{
                payEvent = new MessageEvent.PayEvent(1, MessageEvent.PayEvent.PAY_TYPE_ALIPAY, "订单支付失败");
            }
            EventBus.getDefault().post(payEvent);
        };

        ThreadPools.execute(payRunnable);
    }
}
