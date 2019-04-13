package com.share_will.mobile.ui.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.share_will.mobile.App;
import com.share_will.mobile.Constant;
import com.share_will.mobile.MessageEvent;
import com.share_will.mobile.R;
import com.share_will.mobile.model.entity.UserInfo;
import com.share_will.mobile.presenter.UserCenterPresenter;
import com.share_will.mobile.services.BatteryService;
import com.share_will.mobile.ui.views.UserCenterView;
import com.share_will.mobile.ui.widget.RowItemView;
import com.ubock.library.base.BaseConfig;
import com.ubock.library.base.BaseEntity;
import com.ubock.library.base.BaseFragmentActivity;
import com.ubock.library.ui.activity.MainActivity;
import com.ubock.library.ui.dialog.ToastExt;
import com.ubock.library.utils.AppUtils;
import com.ubock.library.utils.LogUtils;
import com.ubock.library.utils.SharedPreferencesUtils;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author chenguandu
 * 功能描述：个人中心
 */
public class PersonInfoActivity extends BaseFragmentActivity<UserCenterPresenter> implements UserCenterView {
    /**
     * 分享
     */
    public static final int REQUESTCODE_SHARE = 10010;
    /**
     * 机柜后台扫码
     */
    private static final int REQUEST_CODE_SCANLOGIN_CODE = 10011;

    StringBuffer sb;

    InputMethodManager manager;


    public TextView text_yue;
    private TextView mAccount;
    private TextView mPackage;
    private TextView mDeposit;
    private View mApplyRefund;
    private View btn_addMoney;

    private TextView battery_num;
    private TextView battery_numPP;
    private TextView battery_use;
    private TextView battery_sn;
    private ImageView battery_sop;
    private TextView mVersion;
    private RowItemView mRivScanLogin;
    public final static String ACTIVITY = "PersonInfoActivity";

    @Override
    protected int getLayoutId() {
        return R.layout.personalinfo_activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setTitle("个人中心");
        sb = new StringBuffer();
        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mAccount = findViewById(R.id.tv_account);
        mPackage = findViewById(R.id.tv_package);
        mAccount.setText(String.format("账号：%s", App.getInstance().getUserId()));
        text_yue = findViewById(R.id.text_yue);
        mDeposit = findViewById(R.id.tv_deposit);
        mApplyRefund = findViewById(R.id.applyRefund);
        mVersion = findViewById(R.id.tv_version);
        mVersion.setText("v" + AppUtils.getVersionName(this));

        battery_num = findViewById(R.id.battery_num);
        battery_numPP = findViewById(R.id.battery_numPP);

        battery_use = findViewById(R.id.battery_use);
        battery_sn = findViewById(R.id.battery_sn);
        battery_sop = findViewById(R.id.battery_sop);
        mRivScanLogin = findView(R.id.riv_scanLogin);

        btn_addMoney = findViewById(R.id.charging);
        btn_addMoney.setOnClickListener((v) -> {
            Intent intent = new Intent(PersonInfoActivity.this, RechargeActivity.class);
            PersonInfoActivity.this.startActivity(intent);
        });

        getBalance(true);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getBalance(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startService(new Intent(this, BatteryService.class));
    }

    public void shopping(View view) {
        startActivity(new Intent(this, ShopActivity.class));
    }

    public void rescue(View view) {
        startActivity(new Intent(this, RescueActivity.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void consume(View view) {
        startActivity(new Intent(this, ConsumeActivity.class));
    }

    @Override
    public void onLoadBalance(BaseEntity<UserInfo> entity) {
        if (entity != null && entity.getCode() == 0) {
            if (entity.getData() != null) {
                if (entity.getData().isAdminStatus()) {
                    mRivScanLogin.setVisibility(View.VISIBLE);
                }
                String balance = String.format("账户余额：%s元", NumberFormat.getInstance().format(entity.getData().getAccount() / 100f));
                text_yue.setText(balance);
                if (entity.getData().getDeposit() > 0) {
                    String deposit = String.format("押金：已缴(%s元)", NumberFormat.getInstance().format(entity.getData().getDeposit() / 100f));
                    mDeposit.setText(deposit);
                    setRefundState(entity.getData().getCauseStatus());
                } else {
                    mDeposit.setText(String.format("押金：未缴"));
                }
                SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd");
                String startTime = df.format(new Date(entity.getData().getBeginTime()));
                String endTime = df.format(new Date(entity.getData().getExpirationTime()));
                if (entity.getData().isVip()) {
                    mPackage.setText(String.format("套餐:%s-%s", startTime, endTime));
                    mPackage.setVisibility(View.VISIBLE);
                } else {
                    mPackage.setVisibility(View.GONE);
                }
            }
        }
    }

    @Override
    public void onLoginCMS(boolean success, String message) {
        ToastExt.showExt(message);
    }

    /**
     * 打开扫码界面
     */
    public void scan(View view) {
        Intent intent = new Intent(this, CaptureActivity.class);
        startActivityForResult(intent, REQUEST_CODE_SCANLOGIN_CODE);
    }

    /**
     * 申请退押金
     *
     * @param view
     */
    public void applyRefund(View view) {
        if (view.getTag() == null) {
            return;
        }
        int state = Integer.valueOf(view.getTag().toString());
        Intent intent = null;
        //-1:正常 0:正在退款中 1:退款成功 2:拒绝退款 3:取消退款
        switch (state) {
            case -1:
            case 3:
                intent = new Intent(this, RefundActivity.class);
                break;
            case 0:
            case 1:
            case 2:
                intent = new Intent(this, RefundDetailActivity.class);
                break;
            default:
                break;
        }
        if (intent != null) {
            startActivity(intent);
        }
    }

    /**
     * 押金进度情况
     * -1:正常 0:正在退款中 1:退款成功 2:拒绝退款 3:取消退款
     */
    private void setRefundState(int state) {
        mApplyRefund.setVisibility(View.VISIBLE);
        mApplyRefund.setTag(state);
    }

    /**
     * 获取用户账号余额、押金、套餐
     */
    private void getBalance(boolean showLoading) {
        final String username = App.getInstance().getUserId();
        getPresenter().getBalance(username, showLoading);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }

        return super.onTouchEvent(event);
    }

    /**
     * 打开预约记录
     *
     * @param view
     */
    public void openBespeakInfo(View view) {
        Intent intent = new Intent(this, BespeakActivity.class);
        intent.putExtra("fromActivity", ACTIVITY);
        startActivity(intent);
    }


    /**
     * 打开用户协议
     *
     * @param view
     */
    public void openUserProtocol(View view) {
        Intent intent = new Intent(this, ProtocolActivity.class);
        intent.putExtra("showTopBar", true);
        startActivity(intent);
    }

    /**
     * 分享
     *
     * @param view
     */
    public void share(View view) {
        if (Build.VERSION.SDK_INT >= 23) {
            String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE, Manifest.permission.READ_LOGS, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.SET_DEBUG_APP, Manifest.permission.SYSTEM_ALERT_WINDOW, Manifest.permission.GET_ACCOUNTS, Manifest.permission.WRITE_APN_SETTINGS};

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

                // 没有获得授权，申请授权
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission
                        .READ_PHONE_STATE)) {
                    // 帮跳转到该应用的设置界面，让用户手动授权
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                } else {
                    ActivityCompat.requestPermissions(this, mPermissionList, REQUESTCODE_SHARE);

                }
            } else {
                startShare();
            }
        } else {
            startShare();
        }
    }


    /**
     * 退出对话框
     */
    public void exit(View view) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.alerm_title)
                .setIcon(null)
                .setCancelable(true)
                .setMessage("确认退出当前账号吗?")
                .setPositiveButton(R.string.alert_yes_button,
                        (dialog, which) -> {
                            App.getInstance().getGlobalModel().setUserInfo(null);
                            SharedPreferencesUtils.saveDeviceData(PersonInfoActivity.this, Constant.USER_INFO, null);
                            Intent intent = new Intent(PersonInfoActivity.this, LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        })
                .setNegativeButton(R.string.alert_no_button,
                        (dialog, which) -> dialog.dismiss()).create().show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {

        if (requestCode == REQUESTCODE_SHARE) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startShare();
            } else {
                ToastExt.showExt("没有权限,请手动开启");
            }

        }

    }

    private void startShare() {
        new ShareAction(PersonInfoActivity.this).withText("hello").withMedia(new UMImage(this, R.drawable.ic_launcher)).setDisplayList(SHARE_MEDIA.SINA, SHARE_MEDIA.QQ, SHARE_MEDIA.WEIXIN)
                .setCallback(shareListener).open();
    }

    private UMShareListener shareListener = new UMShareListener() {
        /**
         * @descrption 分享开始的回调
         * @param platform 平台类型
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        /**
         * @descrption 分享成功的回调
         * @param platform 平台类型
         */
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Toast.makeText(PersonInfoActivity.this, "成功了", Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption 分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(PersonInfoActivity.this, "失败" + t.getMessage(), Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption 分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(PersonInfoActivity.this, "取消了", Toast.LENGTH_LONG).show();

        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onMessageEvent(MessageEvent.BatteryInfo info) {
        Log.d("cgd", "update battery info");
        if (info.sop < 0) {
            battery_num.setText("剩余能量：离线");
            battery_use.setText("已使用能量点：离线");
            battery_sop.setVisibility(View.INVISIBLE);
        } else {
            battery_use.setText(String.format("已使用能量点：%d", info.use));
            battery_num.setText("剩余能量：");
            int i = (int) Math.ceil(info.sop / 20.0f);//20%一格电
            battery_sop.setVisibility(View.VISIBLE);
            battery_numPP.setText(String.format("%d%%", info.sop));
            battery_sop.setImageResource(getResources().getIdentifier("battery_" + i, "drawable", getPackageName()));
            battery_numPP.setText(String.format("%d%%", info.sop));
        }
        battery_sn.setText(String.format("电池SN码：%s", info.sn == null ? "无" : info.sn));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SCANLOGIN_CODE && resultCode == RESULT_OK) {
            String result = data.getStringExtra("scan_result");
            LogUtils.d(result + "=====");
            //result  =   http://www.ep-ai.com/4GAgreement/qr/scanQR.html?customerCode=null&sn=E201805301000001&time=1547014958567
            if (result.contains(BaseConfig.PROJECT_NAME)) {
                if (result.contains("sn=") && result.contains("&time")) {
                    String sn = result.substring(result.indexOf("sn=") + 3, result.indexOf("&time"));
                    getPresenter().loginCMS(App.getInstance().getUserId(), sn, 3);
                }
            }
        }
    }
}
