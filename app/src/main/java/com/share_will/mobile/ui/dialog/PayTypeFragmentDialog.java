package com.share_will.mobile.ui.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.share_will.mobile.App;
import com.share_will.mobile.Constant;
import com.share_will.mobile.MessageEvent;
import com.share_will.mobile.R;
import com.share_will.mobile.alipay.AlipayUtils;
import com.share_will.mobile.listener.DetachViewClickListener;
import com.share_will.mobile.presenter.PayPresenter;
import com.share_will.mobile.ui.views.PayView;
import com.share_will.mobile.wxapi.WXUtils;
import com.ubock.library.base.BaseDialogFragment;
import com.ubock.library.base.BaseEntity;
import com.ubock.library.utils.AppUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PayTypeFragmentDialog.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PayTypeFragmentDialog#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PayTypeFragmentDialog extends BaseDialogFragment<PayPresenter> implements View.OnClickListener, PayView
    {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_SHOW_BALANCE_PAY = "param1";
    private static final String ARG_ORDER_TYPE = "param2";
    private static final String ARG_ORDER_ID = "param3";
    private static final String ARG_ORDER_BODY = "param4";
    private TextView mCancelBtn;
    private LinearLayout mBalancePay;
    private LinearLayout mWeixinPay;
    private LinearLayout mZhifubaoPay;

    // TODO: Rename and change types of parameters
    private boolean mShowBalancePay = true;
    private int mOrderType;
    private String mOrderId;
    private String mOrderBody;

    private OnFragmentInteractionListener mListener;
    private DetachViewClickListener mDetachViewClickListener;

    public PayTypeFragmentDialog() {
        // Required empty public constructor
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_pay_type_fragment_dialog;
    }

    @Override
    protected void initView(View view, @Nullable Bundle savedInstanceState) {
        mCancelBtn = view.findViewById(R.id.cancel);
        mCancelBtn.setOnClickListener(mDetachViewClickListener);
        mBalancePay = view.findViewById(R.id.ll_pay_balance);
        mBalancePay.setOnClickListener(mDetachViewClickListener);
        if (!mShowBalancePay){
            mBalancePay.setVisibility(View.GONE);
        } else {
            mBalancePay.setVisibility(View.VISIBLE);
        }
        mWeixinPay = view.findViewById(R.id.ll_pay_weixin);
        mWeixinPay.setOnClickListener(mDetachViewClickListener);
        mZhifubaoPay = view.findViewById(R.id.ll_pay_zhifubao);
        mZhifubaoPay.setOnClickListener(mDetachViewClickListener);
    }

    @Override
    protected int initWidth() {
        return LinearLayout.LayoutParams.MATCH_PARENT;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param showBalancePay Parameter 1.
     * @return A new instance of fragment PayTypeFragmentDialog.
     */
    // TODO: Rename and change types and number of parameters
    public static PayTypeFragmentDialog newInstance(boolean showBalancePay, int orderType, String orderId, String orderBody) {
        PayTypeFragmentDialog fragment = new PayTypeFragmentDialog();
        Bundle args = new Bundle();
        args.putBoolean(ARG_SHOW_BALANCE_PAY, showBalancePay);
        args.putInt(ARG_ORDER_TYPE, orderType);
        args.putString(ARG_ORDER_ID, orderId);
        args.putString(ARG_ORDER_BODY, orderBody);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mShowBalancePay = getArguments().getBoolean(ARG_SHOW_BALANCE_PAY);
            mOrderType = getArguments().getInt(ARG_ORDER_TYPE);
            mOrderId = getArguments().getString(ARG_ORDER_ID);
            mOrderBody = getArguments().getString(ARG_ORDER_BODY);
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(int payType) {
        if (mListener != null) {
            mListener.onFragmentInteraction(payType);
        }
        dismiss();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        EventBus.getDefault().register(this);
        mDetachViewClickListener = DetachViewClickListener.wrap(this);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        EventBus.getDefault().unregister(this);
        mListener = null;
        if (mDetachViewClickListener != null) {
            mDetachViewClickListener.release();
            mDetachViewClickListener = null;
        }
        super.onDetach();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPayResult(MessageEvent.PayEvent event){
        showError(event.message);
        if (event.code == 0) {
            dismiss();
        }
    }

    /**
     * 微信appId
     * @return
     */
    private String getWeiXinAppId(){
        return Constant.WEIXIN_APP_INFO.get(AppUtils.getAppMetaData(App.getInstance(),"CHANNEL")+"_APP_ID");
    }

    @Override
    public void onCreateAlipayOrder(boolean success, String orderInfo, String message) {
        if (success){
            AlipayUtils alipayUtils = new AlipayUtils(getActivity());
            alipayUtils.pay(orderInfo);
        } else {
            showError(message);
        }
    }

    @Override
    public void onCreateWeiXinOrder(BaseEntity<Map<String, String>> ret) {
        if (ret != null){
             if (ret.getCode() == 0) {
                 WXUtils wxUtils = new WXUtils(getContext());
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
        if (success){
            showError("支付成功");
            dismiss();
        } else {
            showError(message);
        }
    }

    /**
     * 显示错误提示
     *
     * @param message
     */
    private void showError(String message) {
        new AlertDialog.Builder(getContext())//设置对话框标题
                .setTitle("")
                .setMessage(message)//设置显示的内容
                .setPositiveButton("知道了", null).show();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(int payType);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancel:
                dismiss();
                break;
            case R.id.ll_pay_balance:
                getPresenter().payPackageOrder(App.getInstance().getUserId(), mOrderId);
                break;
            case R.id.ll_pay_weixin:
                getPresenter().getWeiXinOrder(getWeiXinAppId(), mOrderType, mOrderId, mOrderBody);
                break;
            case R.id.ll_pay_zhifubao:
                getPresenter().getAliPayOrder(0, AlipayUtils.APP_ID, mOrderType, mOrderId, mOrderBody);
                break;
            default:
                break;
        }
    }
}
