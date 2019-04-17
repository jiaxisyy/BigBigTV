package com.share_will.mobile.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.share_will.mobile.listener.DetachDialogClickListener;
import com.share_will.mobile.presenter.PayPresenter;
import com.share_will.mobile.ui.views.PayView;
import com.share_will.mobile.wxapi.WXUtils;
import com.ubock.library.base.BaseEntity;
import com.ubock.library.base.BaseFragmentActivity;
import com.ubock.library.utils.AppUtils;
import com.ubock.library.utils.LogUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.NumberFormat;
import java.util.Map;

public class OrderFormActivity extends BaseFragmentActivity<PayPresenter> implements View.OnClickListener, PayView
    ,DialogInterface.OnClickListener{
    private RadioButton mRbBalance;
    private RadioButton mRbZfb;
    private RadioButton mRbWz;
    private RadioGroup mRgType;
    private TextView mSubmit;
    private int submitType = -1;
    private boolean mIsPackage;
    private String mOrderId;
    private int mPrice;
    private int mOrderType;
    private String mBody;
    private TextView mOrderName;
    private TextView mOrderPrice;

    private AlertDialog mAlertDialog;
    private DetachDialogClickListener mDetachClickListener = DetachDialogClickListener.wrap(this);

    @Override
    protected int getLayoutId() {
        return R.layout.activity_order_form;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setTitle("确认支付");
        mOrderName = findViewById(R.id.tv_order_form_name);
        mOrderPrice = findViewById(R.id.tv_order_form_price);
        mRbBalance = findViewById(R.id.rb_order_type_balance);
        mRbZfb = findViewById(R.id.rb_order_type_zfb);
        mRbWz = findViewById(R.id.rb_order_type_wx);
        mRgType = findViewById(R.id.rg_order_type);
        mSubmit = findViewById(R.id.tv_order_form_submit);
        mSubmit.setOnClickListener(this);

        mIsPackage = getIntent().getBooleanExtra("isPackage", false);
        mOrderId = getIntent().getStringExtra("orderId");
        mOrderType = getIntent().getIntExtra("orderType", -1);
        mPrice = getIntent().getIntExtra("price", 0);
        mBody = getIntent().getStringExtra("body");

        mOrderName.setText(mBody);
        mOrderPrice.setText(String.format("%s元", NumberFormat.getInstance().format(mPrice/100f)));

        mRgType.setOnCheckedChangeListener((radioGroup, i) -> {
            switch (i) {
                case R.id.rb_order_type_balance:
                    submitType = MessageEvent.PayEvent.PAY_TYPE_BALANCE;
                    break;
                case R.id.rb_order_type_zfb:
                    submitType = MessageEvent.PayEvent.PAY_TYPE_ALIPAY;
                    break;
                case R.id.rb_order_type_wx:
                    submitType = MessageEvent.PayEvent.PAY_TYPE_WEIXIN;
                    break;

            }

        });

        mAlertDialog = new AlertDialog.Builder(this)
                .setTitle("支付提示")
                .setIcon(null)
                .setCancelable(false)
                .setPositiveButton(R.string.alert_yes_button, mDetachClickListener)
                .create();
        mAlertDialog.setCanceledOnTouchOutside(false);
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

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (which == DialogInterface.BUTTON_POSITIVE) {
            finish();
        }
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
            } else if (submitType == MessageEvent.PayEvent.PAY_TYPE_BALANCE) {
                if (!TextUtils.isEmpty(mOrderId)) {
                    if (mIsPackage) {
                        getPresenter().payPackageOrder(App.getInstance().getUserId(), mOrderId);
                    } else {
                        getPresenter().payMoneyOrder(App.getInstance().getUserId(), mOrderId);
                    }
                }
            } else if (submitType == MessageEvent.PayEvent.PAY_TYPE_WEIXIN) {
                if (!TextUtils.isEmpty(mOrderId)) {
                    getPresenter().getWeiXinOrder(getWeiXinAppId(), mOrderType, mOrderId, mBody);
                }
            } else if (submitType == MessageEvent.PayEvent.PAY_TYPE_ALIPAY) {
                if (!TextUtils.isEmpty(mOrderId)) {
                    getPresenter().getAliPayOrder(0, AlipayUtils.APP_ID, mOrderType, mOrderId, mBody);
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
            mAlertDialog.setMessage("支付成功");
            mAlertDialog.show();
        } else {
            showError(message);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPayEvent(MessageEvent.PayEvent event) {
        if (event.code == 0) {
            //支付成功刷新界面
            mAlertDialog.setMessage("支付成功");
            mAlertDialog.show();
        } else {
            showError(event.message);
        }
    }
}
