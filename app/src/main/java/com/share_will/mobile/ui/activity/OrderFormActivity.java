package com.share_will.mobile.ui.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.share_will.mobile.App;
import com.share_will.mobile.Constant;
import com.share_will.mobile.MessageEvent;
import com.share_will.mobile.R;
import com.share_will.mobile.alipay.AlipayUtils;
import com.share_will.mobile.presenter.PayPresenter;
import com.share_will.mobile.ui.views.PayView;
import com.share_will.mobile.wxapi.WXUtils;
import com.ubock.library.annotation.PresenterInjector;
import com.ubock.library.base.BaseEntity;
import com.ubock.library.base.BaseFragmentActivity;
import com.ubock.library.utils.AppUtils;
import com.ubock.library.utils.LogUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Map;

public class OrderFormActivity extends BaseFragmentActivity implements View.OnClickListener, PayView {
    private RadioButton mRbBalance;
    private RadioButton mRbZfb;
    private RadioButton mRbWz;
    private RadioGroup mRgType;
    private TextView mSubmit;
    private int submitType = -1;
    private String mOrderId;
    @PresenterInjector
    PayPresenter payPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_order_form;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setTitle("确认支付");
        EventBus.getDefault().register(this);
        mRbBalance = findViewById(R.id.rb_order_type_balance);
        mRbZfb = findViewById(R.id.rb_order_type_zfb);
        mRbWz = findViewById(R.id.rb_order_type_wx);
        mRgType = findViewById(R.id.rg_order_type);
        mSubmit = findViewById(R.id.tv_order_form_submit);
        mSubmit.setOnClickListener(this);
        mOrderId = getIntent().getStringExtra("orderId");

        mRgType.setOnCheckedChangeListener((radioGroup, i) -> {
            switch (i) {
                case R.id.rb_order_type_balance:
                    submitType = 0;
                    break;
                case R.id.rb_order_type_zfb:
                    submitType = 2;
                    break;
                case R.id.rb_order_type_wx:
                    submitType = 1;
                    break;

            }

        });
    }

    /**
     * 微信appId
     *
     * @return
     */
    private String getWeiXinAppId() {
        return Constant.WEIXIN_APP_INFO.get(AppUtils.getAppMetaData(App.getInstance(), "CHANNEL") + "_APP_ID");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 显示错误提示
     *
     * @param message
     */
    private void showError(String message) {
        new AlertDialog.Builder(this)//设置对话框标题
                .setTitle("")
                .setMessage(message)//设置显示的内容
                .setPositiveButton("知道了", null).show();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.tv_order_form_submit) {
            if (submitType == -1) {
                showMessage("请选择支付类型");
            } else if (submitType == 0) {
                if (!TextUtils.isEmpty(mOrderId)) {
                    payPresenter.payMoneyOrder(App.getInstance().getUserId(), mOrderId);
                }
            } else if (submitType == 1) {
                if (!TextUtils.isEmpty(mOrderId)) {
                    payPresenter.getWeiXinOrder(getWeiXinAppId(), 0, mOrderId, "充值");
                }
            } else if (submitType == 2) {
                if (!TextUtils.isEmpty(mOrderId)) {
                    payPresenter.getAliPayOrder(0, AlipayUtils.APP_ID, 0, mOrderId, "充值");
                }
            }
        }
        LogUtils.d(submitType + "===========");
    }

    @Override
    public void onCreateAlipayOrder(boolean success, String orderInfo, String message) {
        if (success) {
            AlipayUtils alipayUtils = new AlipayUtils(this);
            alipayUtils.pay(orderInfo);
        } else {
            showError(message);
        }
    }

    @Override
    public void onCreateWeiXinOrder(BaseEntity<Map<String, String>> ret) {
        if (ret != null) {
            if (ret.getCode() == 0) {
                WXUtils wxUtils = new WXUtils(this);
                wxUtils.pay(ret.getData());
            } else {
                showError(ret.getMessage());
            }
        } else {
            showError("生成订单失败");
        }
    }

    @Override
    public void onPayPackageResult(boolean success, String message) {
        if (success) {
            finish();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPayEvent(MessageEvent.PayEvent event) {
        if (event.code == 0) {
            //支付成功刷新界面
            finish();
        }
    }
}
