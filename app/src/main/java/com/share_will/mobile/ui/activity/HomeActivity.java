package com.share_will.mobile.ui.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.share_will.mobile.App;
import com.share_will.mobile.Constant;
import com.share_will.mobile.R;
import com.share_will.mobile.model.entity.CabinetEntity;
import com.share_will.mobile.model.entity.CityEntity;
import com.share_will.mobile.model.entity.NotifyMessageEntity;
import com.share_will.mobile.presenter.HomePresenter;
import com.share_will.mobile.ui.fragment.HomeFragment;
import com.share_will.mobile.ui.fragment.MyFragment;
import com.share_will.mobile.ui.fragment.MallFragment;
import com.share_will.mobile.ui.fragment.AlarmFragment;
import com.share_will.mobile.ui.views.HomeView;
import com.ubock.library.base.BaseEntity;
import com.ubock.library.base.BaseFragment;
import com.ubock.library.base.BaseTabContainerActivity;
import com.ubock.library.ui.dialog.ToastExt;
import com.ubock.library.utils.AppUtils;
import com.ubock.library.utils.LogUtils;
import com.ubock.library.utils.SharedPreferencesUtils;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

public class HomeActivity extends BaseTabContainerActivity<HomePresenter> implements HomeView {
    private BaseFragment[] mFragments = {new HomeFragment(),
            new AlarmFragment(),
            new AlarmFragment(),
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
        int itemWidth;
        int per = screenWidth / 10;//把屏幕分成10份，1、4各占两份， 2、3各占3份，然后2、3各减1份给中间扫码按钮
        int padding = (per * 2 - AutoUtils.getPercentWidthSize(70)) / 2;//70是icon是大小
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
        return App.getInstance().isLogin();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        getPresenter().parseUri(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        getPresenter().parseUri(intent);
    }

    /**
     * 打开扫码界面
     */
    public void scan(View view) {
        startActivity(new Intent(this, MapActivity.class));
//        Intent intent = new Intent(this, CaptureActivity.class);
//        startActivityForResult(intent, REQUEST_CODE_SCAN_CODE);
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

    @Override
    public void showNotifyMessage(boolean isShow, NotifyMessageEntity data) {

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
