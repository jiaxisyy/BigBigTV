package com.share_will.mobile.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.share_will.mobile.App;
import com.share_will.mobile.MessageEvent;
import com.share_will.mobile.R;
import com.share_will.mobile.model.entity.UserInfo;
import com.share_will.mobile.presenter.UserCenterPresenter;
import com.share_will.mobile.services.BatteryService;
import com.share_will.mobile.ui.activity.CaptureActivity;
import com.share_will.mobile.ui.activity.ConsumeActivity;
import com.share_will.mobile.ui.activity.MyBatteryActivity;
import com.share_will.mobile.ui.activity.MyDepositActivity;
import com.share_will.mobile.ui.activity.OrderActivity;
import com.share_will.mobile.ui.activity.RechargeActivity;
import com.share_will.mobile.ui.activity.RescueActivity;
import com.share_will.mobile.ui.activity.SettingActivity;
import com.share_will.mobile.ui.activity.ShopActivity;
import com.share_will.mobile.ui.views.UserCenterView;
import com.share_will.mobile.ui.widget.RowItemView;
import com.ubock.library.annotation.PresenterInjector;
import com.ubock.library.base.BaseEntity;
import com.ubock.library.base.BaseFragment;
import com.ubock.library.ui.dialog.ToastExt;
import com.ubock.library.utils.LogUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.NumberFormat;

import static android.app.Activity.RESULT_OK;

public class MyFragment extends BaseFragment implements View.OnClickListener, UserCenterView {
    /**
     * 机柜后台扫码
     */
    private static final int REQUEST_CODE_SCANLOGIN_CODE = 10011;

    @PresenterInjector
    UserCenterPresenter userCenterPresenter;

    private RowItemView mRowScanLogin;
    private TextView mTvPhoneNum;
    private TextView mTvBalance;
    private TextView mTvBatteryPP;
    private TextView mTvBind;
    private int mDeposit;
    private int mCauseStatus;
    private ImageButton mBtnTopRightMenu;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        setTitle("个人中心");
        showBackMenu(false);
        showTopRightMenu(true);
        view.findViewById(R.id.row_my_deposit).setOnClickListener(this);
        view.findViewById(R.id.row_my_money).setOnClickListener(this);
        view.findViewById(R.id.row_my_order).setOnClickListener(this);
        view.findViewById(R.id.row_my_consume).setOnClickListener(this);
        view.findViewById(R.id.row_my_rescue).setOnClickListener(this);
        view.findViewById(R.id.row_my_vehicle).setOnClickListener(this);
        view.findViewById(R.id.row_my_battery).setOnClickListener(this);
        view.findViewById(R.id.row_my_scan).setOnClickListener(this);
        mRowScanLogin = view.findViewById(R.id.row_my_scan);
        mRowScanLogin.setOnClickListener(this);
        mTvPhoneNum = view.findViewById(R.id.tv_my_top_phone_num);
        mTvBalance = view.findViewById(R.id.tv_my_top_balance);
        mTvBatteryPP = view.findViewById(R.id.tv_my_top_batteryPP);
        mTvBind = view.findViewById(R.id.tv_my_top_bind);
        mBtnTopRightMenu = view.findViewById(R.id.btn_top_right_menu);
        mBtnTopRightMenu.setOnClickListener(this);
        mTvPhoneNum.setText(App.getInstance().getUserId());
        getBalance(true);
        showTopRightMenu(true);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.row_my_scan:
                scanLogin();
                break;
            case R.id.row_my_deposit:
                Intent intent = new Intent(getActivity(), MyDepositActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("deposit", mDeposit);
                bundle.putInt("cause_status", mCauseStatus);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.row_my_money:
                startActivity(new Intent(getActivity(), RechargeActivity.class));
                break;
            case R.id.row_my_order:
                startActivity(new Intent(getActivity(), OrderActivity.class));
                break;
            case R.id.row_my_consume:
                startActivity(new Intent(getActivity(), ConsumeActivity.class));
                break;
            case R.id.row_my_rescue:
                startActivity(new Intent(getActivity(), RescueActivity.class));
                break;
            case R.id.row_my_vehicle:
                startActivity(new Intent(getActivity(), ShopActivity.class));
                break;
            case R.id.row_my_battery:
                startActivity(new Intent(getActivity(), MyBatteryActivity.class));
                break;
            case R.id.btn_top_right_menu:
                startActivity(new Intent(getActivity(), SettingActivity.class));
                break;
        }

    }

    /**
     * 打开扫码界面
     */
    public void scanLogin() {
        Intent intent = new Intent(getActivity(), CaptureActivity.class);
        startActivityForResult(intent, REQUEST_CODE_SCANLOGIN_CODE);
    }

    /**
     * 获取用户账号余额、押金、套餐
     */
    private void getBalance(boolean showLoading) {
        final String username = App.getInstance().getUserId();
        userCenterPresenter.getBalance(username, showLoading);
    }

    @Override
    public void onLoadBalance(BaseEntity<UserInfo> entity) {
        if (entity != null && entity.getCode() == 0) {
            if (entity.getData() != null) {
                if (entity.getData().isAdminStatus()) {
                    mRowScanLogin.setVisibility(View.VISIBLE);
                }
                String balance = String.format("￥%s", NumberFormat.getInstance().format(entity.getData().getAccount() / 100f));
                mTvBalance.setText(balance);
                if (entity.getData().getDeposit() > 0) {
                    mDeposit = entity.getData().getDeposit();
                    mCauseStatus = entity.getData().getCauseStatus();
                }
            }
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SCANLOGIN_CODE && resultCode == RESULT_OK) {
            String result = data.getStringExtra("scan_result");
            LogUtils.d(result + "=====");
            //result  =   http://www.ep-ai.com/4GAgreement/qr/scanQR.html?customerCode=null&sn=E201805301000001&time=1547014958567
            if (!TextUtils.isEmpty(result)) {
                if (result.contains("sn=") && result.contains("&time")) {
                    String sn = result.substring(result.indexOf("sn=") + 3, result.indexOf("&time"));
                    userCenterPresenter.loginCMS(App.getInstance().getUserId(), sn, 3);
                }
            }
        }
    }

    @Override
    public void onLoginCMS(boolean success, String message) {
        ToastExt.showExt(message);
    }

    @Override
    public void onResume() {
        super.onResume();
        getBalance(false);
        getActivity().startService(new Intent(getActivity(), BatteryService.class));
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onMessageEvent(MessageEvent.BatteryInfo info) {
        Log.d("cgd", "update battery info");
        if (info.sop > 0) {
            mTvBatteryPP.setText(String.format("%d%%", info.sop));
        }
        mTvBind.setText(info.sn);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

}

