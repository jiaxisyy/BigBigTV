package com.share_will.mobile.wxapi;

import android.content.Context;
import android.text.TextUtils;

import com.share_will.mobile.App;
import com.share_will.mobile.Constant;
import com.share_will.mobile.MessageEvent;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.ubock.library.utils.AppUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

public class WXUtils {

    public final static String TAG = "WXUtils";

    private Context mContext;
    private IWXAPI mIWXAPI;
    private PayReq mPayReq;

    public WXUtils(Context context) {
        mContext = context;
        mIWXAPI = WXAPIFactory.createWXAPI(mContext, null);
        mIWXAPI.registerApp(getAppId());
        mPayReq = new PayReq();
    }

    public void pay(Map<String, String> param){
        if (TextUtils.isEmpty(param.get("prepayid"))){
            MessageEvent.PayEvent payEvent = new MessageEvent.PayEvent(1, MessageEvent.PayEvent.PAY_TYPE_WEIXIN, "生成微信预支付订单失败");
            EventBus.getDefault().post(payEvent);
            return;
        }
        mPayReq.appId = param.get("appid");
        mPayReq.partnerId = param.get("partnerid");
        mPayReq.prepayId = param.get("prepayid");
        mPayReq.packageValue = param.get("package");
        mPayReq.nonceStr = param.get("noncestr");
        mPayReq.timeStamp = param.get("timestamp");
        mPayReq.sign = param.get("sign");

        sendPayReq();
    }

    private String getAppId(){
        return Constant.WEIXIN_APP_INFO.get(AppUtils.getAppMetaData(App.getInstance(),"CHANNEL")+"_APP_ID");
    }

    private void sendPayReq() {
        mIWXAPI.sendReq(mPayReq);
    }

}
