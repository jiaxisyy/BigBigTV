package com.share_will.mobile.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.share_will.mobile.ui.activity.HomeActivity;
import com.share_will.mobile.ui.activity.MyBatteryActivity;
import com.share_will.mobile.ui.activity.MyDepositActivity;
import com.share_will.mobile.ui.activity.MyInformationActivity;
import com.share_will.mobile.ui.activity.OrderActivity;
import com.share_will.mobile.ui.activity.RechargeActivity;
import com.share_will.mobile.ui.activity.RefundActivity;
import com.share_will.mobile.ui.activity.RefundDetailActivity;
import com.share_will.mobile.ui.activity.RescueActivity;
import com.share_will.mobile.ui.activity.SettingActivity;
import com.share_will.mobile.ui.activity.ShopActivity;
import com.share_will.mobile.ui.views.UserCenterView;
import com.share_will.mobile.ui.widget.CircleImageView;
import com.share_will.mobile.ui.widget.RowItemView;
import com.ubock.library.annotation.PresenterInjector;
import com.ubock.library.base.BaseEntity;
import com.ubock.library.base.BaseFragment;
import com.ubock.library.ui.dialog.ToastExt;
import com.ubock.library.utils.DateUtils;
import com.ubock.library.utils.LogUtils;
import com.ubock.library.utils.SharedPreferencesUtils;

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
    //异常扫码取电池
    public final static int REQUEST_CODE_GET_BATTERY = 101;
    private static final int REQUEST_CODE_USERINFO = 10012;
    //头像图片选择
    private static final int RESULTCODE_HEADPATH = 10020;

    @PresenterInjector
    UserCenterPresenter userCenterPresenter;

    private RowItemView mRowScanLogin;
    private TextView mTvPhoneNum;
    private TextView mTvBalance;
    private TextView mTvBatteryPP;
    private TextView mTvBind;
    private int mDeposit;
    private int mCauseStatus = -1;
    private ImageButton mBtnTopRightMenu;
    private SwipeRefreshLayout mRefreshLayout;
    private TextView mValidity;
    private TextView mShopping;
    private UserInfo mUserInfo;
    private CircleImageView mHeadView;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        setTitle("个人中心");
        showBackMenu(false);
        showTopRightMenu(true);
        view.findViewById(R.id.row_exception_get_battery).setOnClickListener(this);
        mRefreshLayout = view.findViewById(R.id.refresh_my_center);
        mRefreshLayout.setOnRefreshListener(() -> update(true));
        mRowScanLogin = view.findViewById(R.id.row_my_scan);
        mTvPhoneNum = view.findViewById(R.id.tv_my_top_phone_num);
        mTvBalance = view.findViewById(R.id.tv_my_top_balance);
        mTvBatteryPP = view.findViewById(R.id.tv_my_top_batteryPP);
        mTvBind = view.findViewById(R.id.tv_my_top_bind);
        mBtnTopRightMenu = view.findViewById(R.id.btn_top_right_menu);
        mValidity = view.findViewById(R.id.tv_my_top_validity);
        mShopping = view.findViewById(R.id.tv_my_top_shopping);

        mRowScanLogin.setOnClickListener(this);
        mShopping.setOnClickListener(this);
        view.findViewById(R.id.row_my_deposit).setOnClickListener(this);
        view.findViewById(R.id.row_my_money).setOnClickListener(this);
        view.findViewById(R.id.row_my_order).setOnClickListener(this);
        view.findViewById(R.id.row_my_consume).setOnClickListener(this);
        view.findViewById(R.id.row_my_rescue).setOnClickListener(this);
        view.findViewById(R.id.row_my_vehicle).setOnClickListener(this);
        view.findViewById(R.id.row_my_battery).setOnClickListener(this);
        view.findViewById(R.id.row_my_scan).setOnClickListener(this);
        mHeadView = view.findViewById(R.id.civ_my_head);
        UserInfo mUserInfo = SharedPreferencesUtils.getDeviceData(getActivity(), App.getInstance().getUserId());
        if (mUserInfo != null && !TextUtils.isEmpty(mUserInfo.getHeadPicPath())) {
            mHeadView.setImageURI(Uri.parse(mUserInfo.getHeadPicPath()));
        }
        mHeadView.setOnClickListener(this);
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
                //-1:正常 0:正在退款中 1:退款成功 2:拒绝退款 3:取消退款
                if (mCauseStatus == -1) {
                    Intent intent = new Intent(getActivity(), MyDepositActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("deposit", mDeposit);
                    bundle.putInt("cause_status", mCauseStatus);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else if (mCauseStatus == 2 || mCauseStatus == 0) {
                    startActivity(new Intent(getActivity(), RefundDetailActivity.class));
                } else if (mCauseStatus == 3) {
                    startActivity(new Intent(getActivity(), RefundActivity.class));
                } else {
                    ToastExt.showExt("状态错误");
                }
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
                Intent inten = new Intent(getActivity(), MyBatteryActivity.class);
                inten.putExtra("isShowBindView", false);
                startActivity(inten);
                break;
            case R.id.btn_top_right_menu:
                startActivity(new Intent(getActivity(), SettingActivity.class));
                break;
            case R.id.row_exception_get_battery:
                Intent inte = new Intent(this.getContext(), CaptureActivity.class);
                startActivityForResult(inte, REQUEST_CODE_GET_BATTERY);
                break;
            case R.id.tv_my_top_shopping:
                goToShopping();
                break;
            case R.id.civ_my_head:
                startActivityForResult(new Intent(getActivity(), MyInformationActivity.class), REQUEST_CODE_USERINFO);
                break;
            default:
                break;
        }

    }

    private void goToShopping() {
        Intent intent = new Intent(this.getContext(), HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("page", HomeActivity.PAGE_SHOP);
        startActivity(intent);
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
                mUserInfo = entity.getData();
                if (entity.getData().isAdminStatus()) {
                    mRowScanLogin.setVisibility(View.VISIBLE);
                }
                long expirationTime = entity.getData().getExpirationTime();
                if (expirationTime != 0) {
                    mValidity.setText("套餐有效期:  " + DateUtils.timeStampToString(expirationTime, DateUtils.YYYYMMDD));
                    mShopping.setText("点击续费");
                } else {
                    mShopping.setText("点击购买");
                }
                String balance = String.format("￥%s", NumberFormat.getInstance().format(entity.getData().getAccount() / 100f));
                mTvBalance.setText(balance);
                if (entity.getData().getDeposit() > 0) {
                    mDeposit = entity.getData().getDeposit();
                    mCauseStatus = Integer.valueOf(entity.getData().getCauseStatus());
                }
            }
        }
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            String result = data.getStringExtra("scan_result");
            LogUtils.d(result + "=====");
            if (TextUtils.isEmpty(result)) {
                ToastExt.showExt("无效二维码");
                return;
            }
            String sn;
            String time;
            try {
                Uri uri = Uri.parse(result);
                sn = uri.getQueryParameter("sn");
                time = uri.getQueryParameter("time");
            } catch (Exception e) {
                LogUtils.e(e);
                ToastExt.showExt("无效二维码");
                return;
            }
            if (TextUtils.isEmpty(sn) || TextUtils.isEmpty(time)) {
                ToastExt.showExt("无效二维码");
                return;
            }
            if (requestCode == REQUEST_CODE_SCANLOGIN_CODE) {
                //result  =   http://www.ep-ai.com/4GAgreement/qr/scanQR.html?customerCode=null&sn=E201805301000001&time=1547014958567
                userCenterPresenter.loginCMS(App.getInstance().getUserId(), sn, 3);
            } else if (requestCode == REQUEST_CODE_GET_BATTERY) {
                userCenterPresenter.exceptionGetBattery(sn, App.getInstance().getUserId());
            }
        } else if (resultCode == RESULTCODE_HEADPATH) {
            if (requestCode == REQUEST_CODE_USERINFO) {
                String path = data.getStringExtra("head_pic_path");
                if (path != null) {
                    mHeadView.setImageURI(Uri.parse(path));
                }
            }
        }


    }

    @Override
    public void onExceptionGetBattery(BaseEntity<Object> data) {
        if (data != null) {
            if (data.getCode() == 0) {
                ToastExt.showExt("认证成功,请及时取出电池");
            } else {
                ToastExt.showExt(data.getMessage());
            }
        } else {
            ToastExt.showExt("取电失败");
        }
    }

    @Override
    public void onLoginCMS(boolean success, String message) {
        ToastExt.showExt(message);
    }

    @Override
    public void onResume() {
        super.onResume();
        update(false);
    }

    private void update(boolean showLoading) {
        getBalance(showLoading);
        getActivity().startService(new Intent(getActivity(), BatteryService.class));
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onMessageEvent(MessageEvent.BatteryInfo info) {
        Log.d("cgd", "update battery info");
        if (info.sop > 0) {
            if (info.online) {
                mTvBatteryPP.setText(String.format("%d%%", info.sop));
            } else {
                long l = System.currentTimeMillis() - info.time;
                long min = l / (1000 * 60);
                String oldSop = String.valueOf(info.sop);
                float minPP = 100 / 120f;//跑1分钟消耗电量百分比
                float consume = min * minPP;
                float v = Float.parseFloat(oldSop) - consume;
                if (v > 0) {
                    mTvBatteryPP.setText(String.format("%d%%", v));
                } else {
                    mTvBatteryPP.setText("0%");
                }
            }


        }
        if (!TextUtils.isEmpty(info.sn)) {
            mTvBind.setText(info.sn);
        }
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

