package com.share_will.mobile.ui.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
        PayTypeFragmentDialog.OnFragmentInteractionListener, View.OnClickListener {

    private static final String TAG = "RechargeActivity";

    private EditText edit_payMoney;

    private PayTypeFragmentDialog mPayTypeFragmentDialog;

    private String mPrice;

    private DetachViewClickListener mDetachViewClickListener = DetachViewClickListener.wrap(this);

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
        if (success){
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
        switch (v.getId()){
            case R.id.btn_paysure:
                String money = edit_payMoney.getText().toString().trim();
                if (TextUtils.isEmpty(money)) {
                    showError("请输入充值金额");
                    return;
                }
                try {
                    mPrice = NumberFormat.getInstance().format(Float.parseFloat(money) * 100);
                    if (mPrice != null) {
                        mPrice = mPrice.replaceAll(",", "");
                        crateRechargeOrder(Integer.valueOf(mPrice));
                    }
                } catch (Exception e) {
                    LogUtils.e(e);
                    showError("请输入正确的金额");
                }
                break;
            default:
                break;
        }
    }

    /**
     * 生成充值订单
     */
    private void crateRechargeOrder(int money){
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

}

