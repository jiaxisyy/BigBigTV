package com.smartmoss.mobile.wxapi;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.share_will.mobile.App;
import com.share_will.mobile.Constant;
import com.share_will.mobile.MessageEvent;
import com.share_will.mobile.R;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.ubock.library.base.BaseFragmentActivity;
import com.ubock.library.utils.AppUtils;

import org.greenrobot.eventbus.EventBus;

public class WXPayEntryActivity extends BaseFragmentActivity implements IWXAPIEventHandler
		, DialogInterface.OnClickListener{

	private static final String TAG = "WXPayEntryActivity";

	private IWXAPI api;

	private String getAppId(){
		return Constant.WEIXIN_APP_INFO.get(AppUtils.getAppMetaData(App.getInstance(),"CHANNEL")+"_APP_ID");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, getAppId());
        api.handleIntent(getIntent(), this);
	}

    @Override
    protected void initView(Bundle savedInstanceState) {
    }

	@Override
	public void onClick(DialogInterface dialog, int which) {

	}

	@Override
    protected int getLayoutId() {
        return R.layout.pay_result;
    }

    @Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		api.handleIntent(intent, this);
	}

    @Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
		Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);
		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			MessageEvent.PayEvent payEvent;
			if(resp.errCode == 0) {
				payEvent = new MessageEvent.PayEvent(0, MessageEvent.PayEvent.PAY_TYPE_WEIXIN, "支付成功");
			}else if(resp.errCode == -2) {
				payEvent = new MessageEvent.PayEvent(1, MessageEvent.PayEvent.PAY_TYPE_WEIXIN, "您已取消支付！");
			}else{
				payEvent = new MessageEvent.PayEvent(1, MessageEvent.PayEvent.PAY_TYPE_WEIXIN, "支付错误！请重新支付！");
			}

			EventBus.getDefault().post(payEvent);
			finish();
		}
	}
}