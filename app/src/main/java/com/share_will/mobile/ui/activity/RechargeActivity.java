package com.share_will.mobile.ui.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.share_will.mobile.App;
import com.share_will.mobile.Constant;
import com.share_will.mobile.MessageEvent;
import com.share_will.mobile.R;
import com.share_will.mobile.alipay.AlipayUtils;
import com.share_will.mobile.listener.DetachViewClickListener;
import com.share_will.mobile.model.entity.UserInfo;
import com.share_will.mobile.presenter.PayPresenter;
import com.share_will.mobile.presenter.RechargePresenter;
import com.share_will.mobile.presenter.UserCenterPresenter;
import com.share_will.mobile.ui.dialog.PayTypeFragmentDialog;
import com.share_will.mobile.ui.views.PayView;
import com.share_will.mobile.ui.views.RechargeView;
import com.share_will.mobile.ui.views.UserCenterView;
import com.share_will.mobile.wxapi.WXUtils;
import com.ubock.library.annotation.PresenterInjector;
import com.ubock.library.base.BaseEntity;
import com.ubock.library.base.BaseFragmentActivity;
import com.ubock.library.utils.AppUtils;
import com.ubock.library.utils.LogUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.NumberFormat;
import java.util.Map;


public class RechargeActivity extends BaseFragmentActivity<RechargePresenter> implements RechargeView,
        PayTypeFragmentDialog.OnFragmentInteractionListener, View.OnClickListener, RadioGroup.OnCheckedChangeListener
        ,PayView, UserCenterView {

    private static final String TAG = "RechargeActivity";

    private EditText mCustomMoney;
    private TextView mAccount;

    private PayTypeFragmentDialog mPayTypeFragmentDialog;

    private int mPrice;
    private int mPayType = -1;

    private DetachViewClickListener mDetachViewClickListener = DetachViewClickListener.wrap(this);
    private RadioGroup mRgMoneyType;
    private RadioButton[] mRadioButtons = new RadioButton[5];

    @PresenterInjector
    private PayPresenter mPayPresenter;
    @PresenterInjector
    UserCenterPresenter mUserCenterPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.pay;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mUserCenterPresenter.getBalance(App.getInstance().getUserId(), false);
    }

    @Override
    public void onCreateOrder(boolean success, String orderId, String message) {
        if (success) {
            if (mPayType == MessageEvent.PayEvent.PAY_TYPE_WEIXIN) {
                mPayPresenter.getWeiXinOrder(getWeiXinAppId(), 0, orderId, "充值");
            } else {
                mPayPresenter.getAliPayOrder(0, AlipayUtils.APP_ID, 0, orderId, "充值");
            }
        } else {
            showError(message);
        }
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
    protected void initView(Bundle savedInstanceState) {
        setTitle("我的钱包");
        mAccount = findViewById(R.id.tv_account);
        mCustomMoney = findViewById(R.id.edit_payNum);
        mRadioButtons[0] = findViewById(R.id.rb_money_100);
        mRadioButtons[1] = findViewById(R.id.rb_money_200);
        mRadioButtons[2] = findViewById(R.id.rb_money_300);
        mRadioButtons[3] = findViewById(R.id.rb_money_500);
        mRadioButtons[4] = findViewById(R.id.rb_money_1000);
        mRgMoneyType = findViewById(R.id.rg_my_money_pay_type);
        mRgMoneyType.setOnCheckedChangeListener(this);
        TextView payBtn = findViewById(R.id.btn_paysure);
        payBtn.setOnClickListener(mDetachViewClickListener);
    }

    @Override
    protected void onDestroy() {
        if (mDetachViewClickListener != null) {
            mDetachViewClickListener.release();
            mDetachViewClickListener = null;
        }
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onLoadBalance(BaseEntity<UserInfo> data) {
        if (data != null && data.getCode() == 0) {
            if (data.getData() != null) {
                String balance = String.format("￥%s", NumberFormat.getInstance().format(data.getData().getAccount() / 100f));
                mAccount.setText(balance);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_paysure:
                String money = mCustomMoney.getText().toString().trim();
                if (!TextUtils.isEmpty(money)){
                    try {
                        mPrice = Integer.parseInt(money);
                    } catch (Exception e) {
                        LogUtils.e(e);
                        showError("请输入正确的金额");
                        return;
                    }
                }
                if (mPrice <= 0) {
                    showError("请输入充值金额");
                    return;
                }

                if (mPayType <= 0) {
                    showError("请选择支付方式");
                    return;
                }
                crateRechargeOrder(mPrice);
                break;
            case R.id.edit_payNum:
                mPrice = 0;
                clearRadioButton();
                break;
            case R.id.rb_money_100:
                mPrice = 100;
                setRadioButtonStyle(0);
                break;
            case R.id.rb_money_200:
                mPrice = 200;
                setRadioButtonStyle(1);
                break;
            case R.id.rb_money_300:
                mPrice = 300;
                setRadioButtonStyle(2);
                break;
            case R.id.rb_money_500:
                mPrice = 500;
                setRadioButtonStyle(3);
                break;
            case R.id.rb_money_1000:
                mPrice = 1000;
                setRadioButtonStyle(4);
                break;
            default:
                break;
        }
    }

    private void setRadioButtonStyle(int index){
        for (int i = 0; i < 5; i++){
            if (i == index){
                mRadioButtons[i].setChecked(true);
            } else {
                mRadioButtons[i].setChecked(false);
            }
        }
        mCustomMoney.setText("");
        mCustomMoney.clearFocus();
    }

    private void clearRadioButton(){
        for (int i = 0; i < 5; i++){
            mRadioButtons[i].setChecked(false);
        }
    }

    /**
     * 生成充值订单
     */
    private void crateRechargeOrder(int money) {
        getPresenter().crateRechargeOrder(App.getInstance().getUserId(), money * 100);
    }

    /**
     * 显示错误提示
     *
     * @param message
     */
    private void showError(String message) {
        new AlertDialog.Builder(RechargeActivity.this)//设置对话框标题
                .setTitle("")
                .setMessage(message)//设置显示的内容
                .setPositiveButton("知道了", null).show();
    }

    @Override
    public void onFragmentInteraction(int payType) {

    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (i) {
            case R.id.rb_zfb:
                mPayType = MessageEvent.PayEvent.PAY_TYPE_ALIPAY;
                break;
            case R.id.rb_wx:
                mPayType = MessageEvent.PayEvent.PAY_TYPE_WEIXIN;
                break;
            default:
                break;
        }
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

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPayEvent(MessageEvent.PayEvent event) {
        if (event.code == 0) {
            //支付成功刷新界面
            showError("支付成功");
        } else {
            showError(event.message);
        }
    }
}

