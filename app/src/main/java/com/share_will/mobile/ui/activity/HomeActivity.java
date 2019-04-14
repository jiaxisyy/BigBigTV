package com.share_will.mobile.ui.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.share_will.mobile.App;
import com.share_will.mobile.Constant;
import com.share_will.mobile.R;
import com.share_will.mobile.model.entity.CabinetEntity;
import com.share_will.mobile.model.entity.CityEntity;
import com.share_will.mobile.model.entity.NotifyMessageEntity;
import com.share_will.mobile.presenter.HomePresenter;
import com.share_will.mobile.services.LocationService;
import com.share_will.mobile.ui.fragment.AlarmFragment;
import com.share_will.mobile.ui.fragment.ExchangeFragment;
import com.share_will.mobile.ui.fragment.HomeFragment;
import com.share_will.mobile.ui.fragment.MallFragment;
import com.share_will.mobile.ui.fragment.MyFragment;
import com.share_will.mobile.ui.views.HomeView;
import com.ubock.library.base.BaseEntity;
import com.ubock.library.base.BaseFragment;
import com.ubock.library.base.BaseTabContainerActivity;
import com.ubock.library.ui.dialog.ToastExt;
import com.ubock.library.utils.AppUtils;
import com.ubock.library.utils.LogUtils;
import com.ubock.library.utils.SharedPreferencesUtils;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class HomeActivity extends BaseTabContainerActivity<HomePresenter> implements HomeView {
    private BaseFragment[] mFragments = {new HomeFragment(),
            new AlarmFragment(),
            new ExchangeFragment(),
            new MallFragment(),
            new MyFragment()};
    private String[] mTitles = {"首页", "智慧消防", "换电", "网上商城", "个人中心"};
    private Integer[] mIcons = {R.drawable.main_menu_home_selector,
            R.drawable.main_menu_warning_selector,
            R.drawable.tab_exchange,
            R.drawable.main_menu_mall_selector,
            R.drawable.main_menu_my_selector};

    /**
     * 扫码请求码
     */
    public final static int REQUEST_CODE_SCAN_CODE = 1;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_home;
    }

    @Override
    protected boolean onCreateCustomView(TabLayout tabLayout) {

        int size = tabLayout.getTabCount();
        LinearLayout view;
        TextView title;
        ImageView icon;
        int screenWidth = AppUtils.getScreenWidth(this);
//        int per = screenWidth / 10;//把屏幕分成10份，1、4各占两份， 2、3各占3份，然后2、3各减1份给中间扫码按钮
        LogUtils.d("screenWidth:" + screenWidth);
        for (int i = 0; i < size; i++) {
            view = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.main_menu_item, null);
            title = view.findViewById(R.id.tv_menu_text);
            icon = view.findViewById(R.id.iv_icon);
            title.setText(mTitles[i]);
            icon.setImageResource(mIcons[i]);
            tabLayout.getTabAt(i).setCustomView(view);
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        openUserProtocol();
        showNotifyMessage();
    }

    @Override
    protected List<String> getTitleList() {
        return Arrays.asList(mTitles);
    }

    @Override
    protected BaseFragment getFragment(int position) {
        return mFragments[position];
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        setOffscreenPageLimit(3);
    }

    /**
     * 打开用户使用协议
     */
    private void openUserProtocol() {
        boolean isRead = SharedPreferencesUtils.getBooleanSF(this, Constant.KEY_USER_PROTOCOL_READ);
        if (!isRead) {
            Intent intent = new Intent(this, ProtocolActivity.class);
            startActivity(intent);
        }
    }

    private boolean isLogin() {
        boolean ret = App.getInstance().isLogin();
        if (!ret){
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setData(getIntent().getData());
            startActivity(intent);
            return ret;
        }
        return ret;
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (!isLogin()){
            return;
        }
        getPresenter().parseUri(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (!isLogin()){
            return;
        }
        openUserProtocol();
        getPresenter().parseUri(intent);
    }

    /**
     * 打开扫码界面
     */
    public void scan(View view) {
        setSelect(2);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SCAN_CODE) {
            //处理扫码换电
            if (resultCode == RESULT_OK) {
                String result = data.getStringExtra("scan_result");
                getPresenter().handleScanResult(result);
            }
        }
    }

    @Override
    public void onLoadCabinetList(BaseEntity<List<CabinetEntity>> ret) {

    }

    @Override
    public void onLoadFullBattery(BaseEntity<Map<String, Integer>> ret) {

    }

    /**
     * 扫描二维码登录回调
     *
     * @param ret 返回数据
     */
    @Override
    public void onScanCode(BaseEntity<Object> ret) {
        if (ret != null) {
            if (ret.getCode() == 0) {
                ToastExt.showExt("登录成功");
            } else if (!TextUtils.isEmpty(ret.getMessage())) {
                if (ret.getMessage().contains("消息发送超时")) {
                    ToastExt.showExt("二维码已过期");
                } else {
                    ToastExt.showExt(ret.getMessage());
                }
            } else {
                ToastExt.showExt("登录失败");
            }
        } else {
            ToastExt.showExt("登录失败");
        }
    }

    /**
     * 充电
     */
    public void chargingBattery(String sn, int channel) {
        Intent intent = new Intent(this, ChargingActivity.class);
        intent.putExtra("sn", sn);
        intent.putExtra("channel", channel);
        startActivity(intent);
    }

    @Override
    public void onLoadCityList(BaseEntity<List<CityEntity>> ret) {

    }

    /**
     * 每日提示信息
     */
    private void showNotifyMessage() {

        getPresenter().getNotifyMessage(App.getInstance().getUserId());
    }

    @Override
    public void showNotifyMessage(boolean isShow, NotifyMessageEntity data) {
        if (isShow) {
            //判断是否在显示时间段内
            long now = System.currentTimeMillis();
            if(now>=data.getStartTime()&&now<=data.getEndTime()){
                int num = SharedPreferencesUtils.getIntergerSF(this, String.valueOf(data.getMessageId()));
                if(num<data.getShowNumber()){
                    notifyMessageDialog(data.getMessageTitle(),data.getMessageContent());
                    SharedPreferencesUtils.setIntergerSF(this,String.valueOf(data.getMessageId()),num+1);
                    SharedPreferencesUtils.setStringSF(this,"TONIGHT",String.valueOf(getTimesnight()));
                }
            }
            String tonight = SharedPreferencesUtils.getStringSF(this, "TONIGHT");
            if(now>Long.valueOf(tonight)){
                SharedPreferencesUtils.setIntergerSF(this,String.valueOf(data.getMessageId()),0);

            }
        }
    }

    /**
     * 每日提示
     */
    private void notifyMessageDialog(String title,String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setIcon(null)
                .setCancelable(true)
                .setMessage(message)
                .setPositiveButton(R.string.alert_yes_button, (dialog, which) ->dialog.dismiss()).create().show();
    }

    /**
     *  获得当天24点时间
     */
    public static long getTimesnight(){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 24);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return  cal.getTimeInMillis();
    }

    @NeedsPermission({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    void onAllowPermission() {
        Log.d("cgd", "onAllowPermission");
        startService(new Intent(this, LocationService.class));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        HomeActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);

    }

    @OnPermissionDenied({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    void onPermissionDenied() {
        Toast.makeText(this, "已拒绝定位权限", Toast.LENGTH_LONG).show();
    }

    @OnNeverAskAgain({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    void onNeverAskAgain() {
        Toast.makeText(this, "已拒绝定位权限", Toast.LENGTH_LONG).show();
    }


    /**
     * 退出对话框
     */
    private void quitDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.alerm_title)
                .setIcon(null)
                .setCancelable(true)
                .setMessage(R.string.alert_quit_confirm)
                .setPositiveButton(R.string.alert_yes_button, (dialog, which) -> moveTaskToBack(true))
                .setNegativeButton(R.string.alert_no_button, (dialog, which) -> dialog.dismiss()).create().show();
    }

    @Override
    public void onBackPressed() {
        quitDialog();
    }
}
