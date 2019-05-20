package com.share_will.mobile.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.share_will.mobile.App;
import com.share_will.mobile.R;
import com.share_will.mobile.model.entity.AlarmEntity;
import com.share_will.mobile.model.entity.BannerEntity;
import com.share_will.mobile.model.entity.BatteryEntity;
import com.share_will.mobile.model.entity.CabinetEntity;
import com.share_will.mobile.model.entity.ChargeBatteryEntity;
import com.share_will.mobile.model.entity.UserInfo;
import com.share_will.mobile.presenter.HomeFragmentPresenter;
import com.share_will.mobile.presenter.UserCenterPresenter;
import com.share_will.mobile.ui.activity.AlarmListActivity;
import com.share_will.mobile.ui.activity.BannerDetailActivity;
import com.share_will.mobile.ui.activity.CaptureActivity;
import com.share_will.mobile.ui.activity.ChargeStakeActivity;
import com.share_will.mobile.ui.activity.HomeActivity;
import com.share_will.mobile.ui.activity.HomeServiceActivity;
import com.share_will.mobile.ui.activity.MyBatteryActivity;
import com.share_will.mobile.ui.adapter.GlideImageLoader;
import com.share_will.mobile.ui.views.IHomeFragmentView;
import com.share_will.mobile.ui.views.UserCenterView;
import com.ubock.library.annotation.PresenterInjector;
import com.ubock.library.base.BaseEntity;
import com.ubock.library.base.BaseFragment;
import com.ubock.library.ui.dialog.ToastExt;
import com.ubock.library.utils.DateUtils;
import com.ubock.library.utils.LogUtils;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HomeFragment extends BaseFragment<HomeFragmentPresenter> implements IHomeFragmentView, View.OnClickListener
        , UserCenterView {
    private TextView mAlarmTitle;
    private TextView mAlarmPositionName;
    private TextView mAlarmRemark;
    private TextView mAlarmTime;
    private TextView mAlarmLevel;
    private TextView mTopCharge;
    private TextView mTopChargeStake;

    private RelativeLayout mRlAlarm;
    private RelativeLayout mRlBattery;
    private TextView mStartTime;
    private TextView mEnoughTime;
    private TextView mDurationTime;
    private TextView mNowSop;
    private TextView mEnergy;
    private TextView mAddress;
    private TextView mDoor;
    private TextView mMoneyCharge;
    private TextView mMoneManage;
    private TextView mMoneyAll;
    private RelativeLayout mCardMoney;
    private TextView mNoAlarm;
    private LinearLayout mNoBatteryCon;
    private TextView mRentalBattery;
    private TextView mGetBattery;
    private TextView mBindBattery;
    private View mLayoutBottom;
    private SwipeRefreshLayout mRefreshLayout;
    private boolean hasChargeBatteryInfo = false;
    @PresenterInjector
    UserCenterPresenter mUserCenterPresenter;

    private UserInfo mUserInfo;
    //扫码绑定电池
    public final static int REQUEST_CODE_BIND_BATTERY = 100;
    //扫码领取电池
    public final static int REQUEST_CODE_GET_BATTERY = 101;
    private ImageView mArrowRight;
    private TextView mTopRent;
    private TextView mTopStorage;
    private TextView mTopExchange;
    private BatteryEntity batteryEntity;
    private ChargeBatteryEntity chargeBatteryEntity;
    private Banner mBanner;
    /**
     * 我的电池信息
     */
    private View mMyBatteryView;
    private ImageView mIvBatteryPic;
    private TextView mTvMyBatterySn;
    private TextView mTvMyBatteryModel;
    private TextView mTvMyBatteryUsed;
    private TextView mTvMyBatteryMileage;
    private TextView mTvMyBatteryRsoc;
    //    private List<String> mRemoteImages = new ArrayList<>();
    private Set<String> mRemoteImagesSet = new HashSet<>();
    private TextureMapView mMapView;
    private AMap mAMap;
    private Marker mLocationMarker;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        showBackMenu(false);
        setTitle("MOSS");
//        View vie = view.findViewById(R.id.topbar);
//        vie.setBackgroundColor(Color.parseColor("#FFFFFF"));
        mRefreshLayout = view.findViewById(R.id.refresh_home);
        mRefreshLayout.setRefreshing(true);
        //执行刷新
        mRefreshLayout.setOnRefreshListener(this::initData);
        mAlarmTitle = view.findViewById(R.id.tv_home_alarm_title);
        mAlarmPositionName = view.findViewById(R.id.tv_home_alarm_positionName);
        mAlarmRemark = view.findViewById(R.id.tv_home_alarm_remark);
        mAlarmTime = view.findViewById(R.id.tv_home_alarm_time);
        mAlarmLevel = view.findViewById(R.id.tv_home_alarm_level);
        mTopCharge = view.findViewById(R.id.tv_home_top_charge);
        mTopChargeStake = view.findViewById(R.id.tv_home_top_charge_stake);
        mTopRent = view.findViewById(R.id.tv_home_top_rent);
        mTopStorage = view.findViewById(R.id.tv_home_top_storage);
        mTopExchange = view.findViewById(R.id.tv_home_top_exchange);
        mRlAlarm = view.findViewById(R.id.rl_home_alarmInfo);
        mRlBattery = view.findViewById(R.id.rl_home_batteryInfo);
        mNoAlarm = view.findViewById(R.id.tv_home_no_alarm);
        mNoBatteryCon = view.findViewById(R.id.ll_no_battery_con);
        mRentalBattery = view.findViewById(R.id.rental_battery);
        mGetBattery = view.findViewById(R.id.get_battery);
        mBindBattery = view.findViewById(R.id.bind_battery);
        mLayoutBottom = view.findViewById(R.id.include_layout_home_bottom);
        mStartTime = view.findViewById(R.id.tv_home_charge_start_time);
        mEnoughTime = view.findViewById(R.id.tv_home_charge_enough_time);
        mDurationTime = view.findViewById(R.id.tv_home_charge_duration_time);
        mNowSop = view.findViewById(R.id.tv_home_charge_now_sop);
        mEnergy = view.findViewById(R.id.tv_home_charge_energy);
        mAddress = view.findViewById(R.id.tv_home_charge_address);
        mDoor = view.findViewById(R.id.tv_home_charge_door);
        mMoneyCharge = view.findViewById(R.id.tv_home_money_charge);
        mMoneManage = view.findViewById(R.id.tv_home_money_manage);
        mMoneyAll = view.findViewById(R.id.tv_home_money_all);
        mCardMoney = view.findViewById(R.id.rl_card_money);
        mArrowRight = view.findViewById(R.id.iv_main_arrow_right);
        mBanner = view.findViewById(R.id.banner_main_top);
        //我的电池信息
        mIvBatteryPic = view.findViewById(R.id.iv_home_my_battery_pic);
        mTvMyBatterySn = view.findViewById(R.id.tv_home_my_battery_sn);
        mTvMyBatteryModel = view.findViewById(R.id.tv_home_my_battery_model);
        mMapView = view.findViewById(R.id.map_home_battery);
        mMapView.onCreate(savedInstanceState);

        mTvMyBatteryUsed = view.findViewById(R.id.tv_home_my_battery_used);
        mTvMyBatteryMileage = view.findViewById(R.id.tv_home_my_battery_mileage);
        mTvMyBatteryRsoc = view.findViewById(R.id.tv_home_my_battery_rsoc);
        mMyBatteryView = view.findViewById(R.id.ll_home_my_battery);

        mTopCharge.setOnClickListener(this);
        mTopChargeStake.setOnClickListener(this);
        mTopRent.setOnClickListener(this);
        mTopStorage.setOnClickListener(this);
        mRlAlarm.setOnClickListener(this);
        mRlBattery.setOnClickListener(this);
        mRentalBattery.setOnClickListener(this);
        mGetBattery.setOnClickListener(this);
        mBindBattery.setOnClickListener(this);
        mTopExchange.setOnClickListener(this);
        initMapView();
        initData();
    }


    private void initBanner() {
        getPresenter().getBannerUrl();
    }

    private void initData() {
        initBanner();
        mUserCenterPresenter.getBalance(App.getInstance().getUserId(), false);
        getPresenter().getAlarmList(App.getInstance().getUserId(), App.getInstance().getToken());
        getPresenter().getChargeBatteryInfo(App.getInstance().getUserId(), App.getInstance().getToken());
    }

    private void initMapView() {
        //初始化地图控制器对象
        if (mAMap == null) {
            mAMap = mMapView.getMap();
            // 绑定 Marker 被点击事件
            mAMap.setOnMarkerClickListener(markerClickListener);

            LogUtils.d("mAMap is null");
            mAMap.getUiSettings().setZoomControlsEnabled(false);
            mAMap.getUiSettings().setLogoPosition(AMapOptions.LOGO_POSITION_BOTTOM_CENTER);
            mAMap.moveCamera(CameraUpdateFactory.zoomTo(14));
        }
    }

    // 定义 Marker 点击事件监听
    AMap.OnMarkerClickListener markerClickListener = new AMap.OnMarkerClickListener() {
        // marker 对象被点击时回调的接口
        // 返回 true 则表示接口已响应事件，否则返回false
        @Override
        public boolean onMarkerClick(Marker marker) {
            return true;
        }
    };

    /**
     * 使用标注点
     */
    private void useMarker() {
        if (mAMap != null) {
            mAMap.clear();
            LatLng latLng = new LatLng(Double.parseDouble(batteryEntity.getLatitude()), Double.parseDouble(batteryEntity.getLongitude()));
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title("");
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_battery_));
            markerOptions.setFlat(true);//设置marker平贴地图效果
            mAMap.addMarker(markerOptions);
            mAMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng));
        }
    }

    private void showMyBatteryView(boolean show) {
        if (show) {
            mMyBatteryView.setVisibility(View.VISIBLE);
            mLayoutBottom.setVisibility(View.GONE);
        } else {
            mMyBatteryView.setVisibility(View.GONE);
//            mLayoutBottom.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 显示无电池信息框
     *
     * @param show       是否显示
     * @param hasDeposit 是否有押金
     */
    private void showNoBatteryView(boolean show, boolean hasDeposit) {
        if (show) {
            mNoBatteryCon.setVisibility(View.VISIBLE);
            if (hasDeposit) {
                mGetBattery.setVisibility(View.VISIBLE);
                mRentalBattery.setVisibility(View.GONE);
            } else {
                mGetBattery.setVisibility(View.GONE);
                mRentalBattery.setVisibility(View.VISIBLE);
            }
        } else {
            mNoBatteryCon.setVisibility(View.GONE);
        }
    }

    private void goToShop() {
        Intent intent = new Intent(this.getContext(), HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("page", HomeActivity.PAGE_SHOP);
        startActivity(intent);
    }

    private void getBattery() {
        Intent inte = new Intent(this.getContext(), CaptureActivity.class);
        startActivityForResult(inte, REQUEST_CODE_GET_BATTERY);
    }

    private void goToExchange() {
        Intent intent = new Intent(this.getContext(), HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("page", HomeActivity.PAGE_EXCHANGE);
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mAMap = null;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onStart() {
        super.onStart();
        mBanner.startAutoPlay();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onStop() {
        super.onStop();
        mBanner.stopAutoPlay();
    }

    @Override
    public void onLoadBalance(BaseEntity<UserInfo> data) {
        if (data != null) {
            mUserInfo = data.getData();
        }
    }

    @Override
    public void onGetBannerUrlResult(BaseEntity<List<BannerEntity>> data) {
        if (data != null) {
            List<BannerEntity> beanList = data.getData();
            for (BannerEntity dataBean : beanList) {
                if (!TextUtils.isEmpty(dataBean.getAdvert_path())) {
                    mRemoteImagesSet.add(dataBean.getAdvert_path());
                }
            }
            LogUtils.d("mRemoteImagesSet.size()=" + mRemoteImagesSet.size());
            List<String> strings = new ArrayList<>(mRemoteImagesSet);
            mBanner.setOnBannerListener(new OnBannerListener() {
                @Override
                public void OnBannerClick(int position) {
                    String s = strings.get(position);
                    LogUtils.d("OnBannerClick->url="+s);
                    for (BannerEntity bannerEntity : beanList) {
                        if (!TextUtils.isEmpty(bannerEntity.getAdvert_detail())) {
                            if (s.equals(bannerEntity.getAdvert_path())) {
                                Intent intent = new Intent(getActivity(), BannerDetailActivity.class);
                                intent.putExtra("banner_detail_url", bannerEntity.getAdvert_detail());
                                LogUtils.d("OnBannerClick->banner_detail_url="+bannerEntity.getAdvert_detail());
                                startActivity(intent);
                            }
                        }
                    }
                }
            });
            mBanner.setImages(strings).setImageLoader(new GlideImageLoader()).start();
        }
    }

    @Override
    public void onLoadAlarmResult(BaseEntity<AlarmEntity> data) {
        int validPos = -1;
        if (data != null) {
            List<AlarmEntity.SmokeBean> smokeBeanList = data.getData().getSmoke();
            int size = smokeBeanList.size();
            if (size > 0) {
                for (int i = 0; i < size; i++) {
                    if (smokeBeanList.get(i).getConfirmstate() != 1 && !TextUtils.isEmpty(smokeBeanList.get(i).getAlarmcode())) {
                        validPos = i;
                    }
                }
                if (validPos != -1) {
                    AlarmEntity.SmokeBean alarmEntity = data.getData().getSmoke().get(validPos);
                    mAlarmTitle.setVisibility(View.VISIBLE);
                    mAlarmPositionName.setVisibility(View.VISIBLE);
                    mAlarmRemark.setVisibility(View.VISIBLE);
                    mAlarmTime.setVisibility(View.VISIBLE);
                    mAlarmLevel.setVisibility(View.VISIBLE);
                    mNoAlarm.setVisibility(View.INVISIBLE);
                    if (!TextUtils.isEmpty(alarmEntity.getTitle())) {
                        mAlarmTitle.setText("标题: " + alarmEntity.getTitle());
                    }
                    if (!TextUtils.isEmpty(alarmEntity.getPositionName())) {
                        mAlarmPositionName.setText(alarmEntity.getPositionName());
                    }
                    if (!TextUtils.isEmpty(alarmEntity.getRemark())) {
                        mAlarmRemark.setText(alarmEntity.getRemark());
                    }
                    if (alarmEntity.getAlarmtime() != 0) {
                        mAlarmTime.setText("告警时间   " + DateUtils.timeStampToString(alarmEntity.getAlarmtime(), DateUtils.YYYYMMDD_HHMMSS));
                    }
                    mAlarmLevel.setText("告警级别   " + alarmEntity.getAlarmlevel() + "级");
                } else {
                    mNoAlarm.setVisibility(View.VISIBLE);
                    mAlarmTitle.setVisibility(View.INVISIBLE);
                    mAlarmPositionName.setVisibility(View.INVISIBLE);
                    mAlarmRemark.setVisibility(View.INVISIBLE);
                    mAlarmTime.setVisibility(View.INVISIBLE);
                    mAlarmLevel.setVisibility(View.INVISIBLE);
                }
            }

        }

    }


    @Override
    public void onLoadChargeBatteryInfoResult(BaseEntity<ChargeBatteryEntity> data) {
        if (data != null) {
            mLayoutBottom.setVisibility(View.VISIBLE);
            showMyBatteryView(false);
            chargeBatteryEntity = data.getData();
            if (chargeBatteryEntity != null) {
                if (chargeBatteryEntity.getStartTime() > 0) {
                    mStartTime.setText("开始时间:   " + DateUtils.timeStampToString(chargeBatteryEntity.getStartTime(), DateUtils.YYYYMMDD_HHMMSS));
                }
                if (chargeBatteryEntity.getFullTime() != 0) {
                    mEnoughTime.setText("充满时间:   " + DateUtils.timeStampToString(chargeBatteryEntity.getFullTime(), DateUtils.YYYYMMDD_HHMMSS));
                    long l = chargeBatteryEntity.getFullTime() - chargeBatteryEntity.getStartTime();
                    mDurationTime.setText("充电时长:   " + DateUtils.unixToUTcTimeDuration(l));
                } else {
                    mEnoughTime.setVisibility(View.GONE);
                    long l = chargeBatteryEntity.getNowTime() - chargeBatteryEntity.getStartTime();
                    mDurationTime.setText("充电时长:   " + DateUtils.unixToUTcTimeDuration(l));
                }
                mNowSop.setText("当前电量:   " + chargeBatteryEntity.getSop() + "%");
                mEnergy.setText("已充能量点:   " + chargeBatteryEntity.getEnergy());

                if (!TextUtils.isEmpty(chargeBatteryEntity.getCabinetAddress())) {
                    mAddress.setText("电池位置:   " + chargeBatteryEntity.getCabinetAddress());
                }
                mDoor.setText("仓门号:   " + chargeBatteryEntity.getDoor() + "号");
                mMoneyCharge.setText(intChange(chargeBatteryEntity.getMoney() / 100f) + "元");
                mMoneManage.setText(intChange(chargeBatteryEntity.getManageMoney() / 100f) + "元");
                int all = chargeBatteryEntity.getMoney() + chargeBatteryEntity.getManageMoney();
                mMoneyAll.setText("合计:" + intChange(all / 100f) + "元");
                mRefreshLayout.setRefreshing(false);
                mArrowRight.setVisibility(View.VISIBLE);
                hasChargeBatteryInfo = true;
                showNoBatteryView(false, mUserInfo != null && mUserInfo.getDeposit() > 0);

            }
        } else {
            //没有充电电池,展示已有电池信息
            hasChargeBatteryInfo = false;
            getPresenter().getBatteryInfo(App.getInstance().getUserId(), App.getInstance().getToken());
        }

    }

    public String intChange(float num) {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(num);
    }

    @Override
    public void onLoadBatteryInfoResult(BaseEntity<BatteryEntity> data) {

        if (data != null) {
            batteryEntity = data.getData();
            if (batteryEntity != null) {
                showMyBatteryView(true);
                mArrowRight.setVisibility(View.INVISIBLE);
                if (!TextUtils.isEmpty(batteryEntity.getSn())) {
                    mTvMyBatterySn.setText("电池SN:   " + batteryEntity.getSn());
                }
                if (!TextUtils.isEmpty(batteryEntity.getDischarges())) {
                    mTvMyBatteryUsed.setText("电池已使用次数:    " + batteryEntity.getDischarges() + "次");
                }
                if (!TextUtils.isEmpty(batteryEntity.getLatitude()) && !TextUtils.isEmpty(batteryEntity.getLongitude())) {
                    //添加电池标注
                    useMarker();
                }
                if (!TextUtils.isEmpty(batteryEntity.getSop())) {
                    Integer sop = Integer.valueOf(batteryEntity.getSop());
                    mTvMyBatteryMileage.setText("电池可骑行里程 (预估) :   " + sop / 20f * 5 + "km");
                    mTvMyBatteryRsoc.setText(batteryEntity.getSop() + "%");
                    switch (sop / 20) {
                        case 0:
                            mIvBatteryPic.setImageResource(R.drawable.icon_mybattery_00);
                            break;
                        case 1:
                            mIvBatteryPic.setImageResource(R.drawable.icon_mybattery_01);
                            break;
                        case 2:
                            mIvBatteryPic.setImageResource(R.drawable.icon_mybattery_02);
                            break;
                        case 3:
                            mIvBatteryPic.setImageResource(R.drawable.icon_mybattery_03);
                            break;
                        case 4:
                            mIvBatteryPic.setImageResource(R.drawable.icon_mybattery_04);
                            break;
                        case 5:
                            mIvBatteryPic.setImageResource(R.drawable.icon_mybattery_05);
                            break;
                    }
                }

//                mDurationTime.setVisibility(View.GONE);
//                mNowSop.setVisibility(View.GONE);
//                mEnergy.setVisibility(View.GONE);
//                mAddress.setVisibility(View.GONE);
//                mDoor.setVisibility(View.GONE);
//                mCardMoney.setVisibility(View.GONE);
//                mLayoutBottom.setVisibility(View.VISIBLE);
                showNoBatteryView(false, mUserInfo != null && mUserInfo.getDeposit() > 0);
            } else {
                showMyBatteryView(false);
                mLayoutBottom.setVisibility(View.GONE);
                showNoBatteryView(true, mUserInfo != null && mUserInfo.getDeposit() > 0);
                mArrowRight.setVisibility(View.INVISIBLE);
            }
        } else {
            showMyBatteryView(false);
            mLayoutBottom.setVisibility(View.GONE);
            showNoBatteryView(true, mUserInfo != null && mUserInfo.getDeposit() > 0);
            mArrowRight.setVisibility(View.INVISIBLE);
        }
        if (mRefreshLayout.isRefreshing()) {
            mRefreshLayout.setRefreshing(false);
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_home_top_exchange:
                goToExchange();
                break;
            case R.id.tv_home_top_charge:
                startActivity(new Intent(getActivity(), HomeServiceActivity.class));
                break;
            case R.id.tv_home_top_charge_stake:
                startActivity(new Intent(getActivity(), ChargeStakeActivity.class));
                break;
            case R.id.tv_home_top_rent:
                if (batteryEntity != null || chargeBatteryEntity != null) {
                    ToastExt.showExt("已有电池,无需领取");
                } else {
                    if (mUserInfo != null) {
                        if (mUserInfo.getDeposit() > 0) {
                            getBattery();
                        } else {
                            goToShop();
                        }
                    }
                }
                break;
            case R.id.tv_home_top_storage:
                ToastExt.showExt("功能暂未开放");
                break;
            case R.id.rl_home_alarmInfo:
                startActivity(new Intent(getActivity(), AlarmListActivity.class));
                break;
            case R.id.rl_home_batteryInfo:
                if (mArrowRight.getVisibility() == View.VISIBLE) {
                    if (hasChargeBatteryInfo) {
                        startActivity(new Intent(getActivity(), HomeServiceActivity.class));
                    } else {
                        Intent intent = new Intent(getActivity(), MyBatteryActivity.class);
                        intent.putExtra("isShowBindView", false);
                        startActivity(intent);
                    }
                }
                break;
            case R.id.get_battery:
                getBattery();
                break;
            case R.id.rental_battery:
                goToShop();
                break;
            case R.id.bind_battery:
                Intent intent1 = new Intent(this.getContext(), CaptureActivity.class);
                intent1.putExtra(CaptureActivity.KEY_SHOW_MANUAL_INPUT, true);
                intent1.putExtra(CaptureActivity.KEY_SHOW_LIGHT, true);
                startActivityForResult(intent1, REQUEST_CODE_BIND_BATTERY);
                break;
            default:
                break;

        }
    }


    @Override
    public void onScanCodeGetBatteryResult(BaseEntity<Object> data) {
        if (data != null) {
            if (data.getCode() == 0) {
                ToastExt.showExt("扫码验证成功,请及时领取电池");
            } else {
                ToastExt.showExt(data.getMessage());
            }
        } else {
            ToastExt.showExt("领取电池失败,请稍候再试");
        }
    }

    @Override
    public void onBindBatteryResult(BaseEntity<Object> data) {
        if (data != null) {
            if (data.getCode() == 0) {
                ToastExt.showExt("绑定电池成功");
            } else {
                ToastExt.showExt("绑定电池失败");
            }
        } else {
            ToastExt.showExt("绑定电池失败");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            String resultData = data.getStringExtra(CaptureActivity.KEY_SCAN_RESULT);
            if (requestCode == REQUEST_CODE_BIND_BATTERY) {
                boolean manualInput = data.getBooleanExtra(CaptureActivity.KEY_SHOW_MANUAL_INPUT, false);
                if (manualInput) {
                    Intent intent = new Intent(getActivity(), MyBatteryActivity.class);
                    intent.putExtra("isShowBindView", true);
                    startActivity(intent);
                } else {
                    if (TextUtils.isEmpty(resultData) || resultData.length() != 16) {
                        ToastExt.showExt("无效二维码/条码");
                    } else {
                        getPresenter().bindBattery(App.getInstance().getUserId(), resultData);
                    }
                }
            } else if (requestCode == REQUEST_CODE_GET_BATTERY) {
                if (TextUtils.isEmpty(resultData)) {
                    ToastExt.showExt("无效二维码");
                } else {
                    try {
                        Uri uri = Uri.parse(resultData);
                        if (uri != null) {
                            String sn = uri.getQueryParameter("sn");
                            String time = uri.getQueryParameter("time");
                            if (!TextUtils.isEmpty(sn) && !TextUtils.isEmpty(time)) {
                                getPresenter().scanCodeGetBattery(sn, App.getInstance().getUserId());
                            } else {
                                ToastExt.showExt("无效二维码");
                            }
                        } else {
                            ToastExt.showExt("无效二维码");
                        }
                    } catch (Exception e) {
                        ToastExt.showExt("无效二维码");
                        LogUtils.e(e);
                    }
                }
            } else {
            }
        }
    }
}
