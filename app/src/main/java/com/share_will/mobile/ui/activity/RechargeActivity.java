package com.share_will.mobile.ui.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.share_will.mobile.App;
import com.share_will.mobile.R;
import com.share_will.mobile.listener.DetachViewClickListener;
import com.share_will.mobile.presenter.RechargePresenter;
import com.share_will.mobile.ui.dialog.PayTypeFragmentDialog;
import com.share_will.mobile.ui.views.RechargeView;
import com.ubock.library.base.BaseFragmentActivity;
import com.ubock.library.utils.LogUtils;

import java.text.NumberFormat;


public class RechargeActivity extends BaseFragmentActivity<RechargePresenter> implements RechargeView,
        PayTypeFragmentDialog.OnFragmentInteractionListener, View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private static final String TAG = "RechargeActivity";

    private EditText edit_payMoney;

    private PayTypeFragmentDialog mPayTypeFragmentDialog;

    private String mPrice;

    private DetachViewClickListener mDetachViewClickListener = DetachViewClickListener.wrap(this);
    private RadioGroup mRgMoney1;
    private RadioGroup mRgMoney2;
    private RadioGroup mRgMoneyType;

    @Override
    protected int getLayoutId() {
        return R.layout.pay;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOrder(boolean success, String orderId, String message) {
        if (success) {


            mPayTypeFragmentDialog = PayTypeFragmentDialog.newInstance(false, 0, orderId, "充值");
            mPayTypeFragmentDialog.show(RechargeActivity.this);
        } else {
            showError(message);
        }
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setTitle("我的钱包");
        edit_payMoney = findViewById(R.id.edit_payNum);
        mRgMoney1 = findViewById(R.id.rg_my_money1);
        mRgMoney2 = findViewById(R.id.rg_my_money2);
        mRgMoneyType = findViewById(R.id.rg_my_money_pay_type);
        mRgMoney1.setOnCheckedChangeListener(this);
        mRgMoney2.setOnCheckedChangeListener(this);
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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_paysure:
                String money = edit_payMoney.getText().toString().trim();
                if (TextUtils.isEmpty(money)) {
                    showError("请输入充值金额");
                    return;
                }
                try {
//                    mPrice = NumberFormat.getInstance().format(Float.parseFloat(money) * 100);
//                    if (mPrice != null) {
//                        mPrice = mPrice.replaceAll(",", "");
//                    }
                    if (Integer.parseInt(money) > 0) {
                        mPrice = money;
                        crateRechargeOrder(Integer.valueOf(mPrice));
                    }
                } catch (Exception e) {
                    LogUtils.e(e);
                    showError("请输入正确的金额");
                }
                break;
            case R.id.edit_payNum:
                mRgMoney1.clearCheck();
                mRgMoney2.clearCheck();
                break;
            default:
                break;
        }
    }

    /**
     * 生成充值订单
     */
    private void crateRechargeOrder(int money) {
        getPresenter().crateRechargeOrder(App.getInstance().getUserId(), money);
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

        if (radioGroup == mRgMoney1) {
            mRgMoney2.clearCheck();
        } else if (radioGroup == mRgMoney2) {
            mRgMoney1.clearCheck();
        } else if (radioGroup == mRgMoneyType) {
            //TODO
        }
        switch (i) {
            case R.id.rb_money_100:
                mPrice = String.valueOf(100);
                break;
            case R.id.rb_money_200:
                mPrice = String.valueOf(200);
                break;
            case R.id.rb_money_300:
                mPrice = String.valueOf(300);
                break;
            case R.id.rb_money_500:
                mPrice = String.valueOf(500);
                break;
            case R.id.rb_money_1000:
                mPrice = String.valueOf(1000);
                break;
        }
    }
}

