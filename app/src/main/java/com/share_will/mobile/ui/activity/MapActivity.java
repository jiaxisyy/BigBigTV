package com.share_will.mobile.ui.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MultiPointItem;
import com.amap.api.maps.model.MultiPointOverlay;
import com.amap.api.maps.model.MultiPointOverlayOptions;
import com.amap.api.navi.model.NaviLatLng;
import com.autonavi.amap.mapcore.interfaces.IMarker;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.share_will.mobile.App;
import com.share_will.mobile.Constant;
import com.share_will.mobile.MessageEvent;
import com.share_will.mobile.R;
import com.share_will.mobile.model.entity.CabinetEntity;
import com.share_will.mobile.model.entity.CityEntity;
import com.share_will.mobile.model.entity.NotifyMessageEntity;
import com.share_will.mobile.presenter.HomePresenter;
import com.share_will.mobile.services.BatteryService;
import com.share_will.mobile.services.LocationService;
import com.share_will.mobile.ui.dialog.InfoWindows;
import com.share_will.mobile.ui.views.HomeView;
import com.share_will.mobile.utils.Utils;
import com.ubock.library.base.BaseEntity;
import com.ubock.library.base.BaseFragmentActivity;
import com.ubock.library.ui.dialog.ToastExt;
import com.ubock.library.utils.DateUtils;
import com.ubock.library.utils.LogUtils;
import com.ubock.library.utils.SharedPreferencesUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class MapActivity extends BaseFragmentActivity<HomePresenter> implements HomeView {
    MapView mMapView = null;
    AMap mAMap = null;
    Location mCurrentLocation = null;
    Marker mLocationMarker = null;
    private List<CabinetEntity> mCabinetList = new ArrayList<>();
    MultiPointOverlay mMultiPointOverlay;
    private ImageView battery_num;
    /**
     * 电量百分比
     */
    private TextView battery_numPP;
    private TextView mCity;
    private InfoWindows mInfoWindows;
    private Button mBtnBespeak;

    private View mCabinetInfoView;
    private TextView mCabinetTitle;
    private TextView mCabinetSN;
    private TextView mFullNum;
    private TextView mCabinetAddress;
    private CabinetEntity mCurrentCabinet;
    public final static String CABINETTITLE = "com.share_will.mobile.ui.activity.MapActivity.cabinettitle";
    public final static String CABINETSN = "com.share_will.mobile.ui.activity.MapActivity.CabinetSN";
    public final static String CABINETADDRESS = "com.share_will.mobile.ui.activity.MapActivity.CabinetAddress";
    public final static String FULLNUM = "com.share_will.mobile.ui.activity.MapActivity.FullNum";

    /**
     * 显示电柜列表消息
     */
    public final static int MSG_SHOW_CABINET_LIST = 0;
    public final static int MSG_SHOW_FULL_BATTERY_COUNT = 1;

    /**
     * 打开GPS请求码
     */
    public final static int REQUEST_CODE_OPEN_GPS = 0;
    /**
     * 扫码请求码
     */
    public final static int REQUEST_CODE_SCAN_CODE = 1;

    private OptionsPickerView mCityPickerView;
    private CityEntity mCityEntity;
    private List<CityEntity> mCityList = null;
    private Intent mBespeakIntent;
    private Marker clickMarker;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_map;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        getPresenter().getCityList();

//        if (isLogin()) {
//            Intent intent = new Intent(this, LoginActivity.class);
//            intent.setData(getIntent().getData());
//            startActivity(intent);
//        }
        openUserProtocol();
        showNotifyMessage();

    }

    /**
     * 每日提示信息
     */
    private void showNotifyMessage() {

        getPresenter().getNotifyMessage(App.getInstance().getUserId());
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mCabinetInfoView = findViewById(R.id.cabinet_info);
        mCabinetTitle = findViewById(R.id.cabinet_title);
        mCabinetSN = findViewById(R.id.cabinet_sn);
        mFullNum = findViewById(R.id.full_num);
        mCabinetAddress = findViewById(R.id.cabinet_address);
        battery_num = findViewById(R.id.battery_num);
        battery_numPP = findView(R.id.battery_numPP);
        mBtnBespeak = findView(R.id.btn_bespeak);
        mCity = findViewById(R.id.tv_city);
        //获取地图控件引用
        mMapView = findViewById(R.id.map);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView.onCreate(savedInstanceState);
        //初始化地图控制器对象
        if (mAMap == null) {
            mAMap = mMapView.getMap();
            mAMap.setOnMapClickListener((v) -> {
                if (mCabinetInfoView.getVisibility() == View.VISIBLE) {
                    hideCabinetInfo();
                }
            });
            mAMap.getUiSettings().setZoomControlsEnabled(false);
            mAMap.getUiSettings().setLogoPosition(AMapOptions.LOGO_POSITION_BOTTOM_CENTER);
            mAMap.moveCamera(CameraUpdateFactory.zoomTo(14));
//            initMultiPoint();
//            mAMap.setOnMultiPointClickListener(mMultiPointClickListener);
            mAMap.setOnMarkerClickListener(markerClickListener);
            mInfoWindows = new InfoWindows(this);
            mAMap.setInfoWindowAdapter(mInfoWindows);
            //添加Marker显示定位位置
            if (mLocationMarker == null) {
                //如果是空的添加一个新的,icon方法就是设置定位图标，可以自定义
                mLocationMarker = mAMap.addMarker(new MarkerOptions()
                        .visible(true)
                        .anchor(0.5f, 0.5f) //设置锚点
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.gps_point)));
            }
        }

        //城市选择器
        mCityPickerView = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                CityEntity city = getPresenter().getModel().getCity(options1);
                if (city != null) {
                    mCityEntity = city;
                    App.getInstance().getGlobalModel().setCityEntity(city);
                    mCity.setText(city.getStationCity());
                    getCabinetList(city.getAreaCode());
                    LatLng latLng = new LatLng(city.getLatitude(), city.getLongitude());
                    mAMap.animateCamera(CameraUpdateFactory.changeLatLng(latLng));
                }
            }
        }).setCyclic(false, false, false).build();
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

    @Override
    public void onLoadCityList(BaseEntity<List<CityEntity>> ret) {
        if (ret != null && ret.getData() != null) {
            mCityList = ret.getData();
            mCityPickerView.setPicker(mCityList);
        }
        MapActivityPermissionsDispatcher.onAllowPermissionWithPermissionCheck(this);
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
    public void showCityDialog(View view) {
        if (mCityList == null || mCityList.isEmpty()) {
            getPresenter().getCityList();
        }
        mCityPickerView.show();



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


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause()，暂停地图的绘制
        mMapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
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

    @NeedsPermission({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    void onAllowPermission() {
        Log.d("cgd", "onAllowPermission");
        startService(new Intent(this, LocationService.class));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MapActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);

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
     * 设置城市
     */
    private void setCity(String cityCode) {
        if (cityCode == null) {
            return;
        }
        int len = getPresenter().getModel().getCity().size();
        CityEntity city;
        for (int i = 0; i < len; i++) {
            city = getPresenter().getModel().getCity(i);
            if (cityCode.equals(city.getAreaCode())) {
                App.getInstance().getGlobalModel().setCityEntity(city);
                mCityPickerView.setSelectOptions(i);
                mCity.setText(city.getStationCity());

                break;


            }
        }
    }

    /**
     * 位置发生变化通知
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLocationChanged(MessageEvent.LocationEvent event) {
        Log.d("cgd", "Location:" + event.location.getLongitude() + ";" + event.location.getLatitude());
        LatLng latLng = new LatLng(event.location.getLatitude(), event.location.getLongitude());
        //第一次定位，视图移动到当前位置
        if (mCurrentLocation == null) {
            //视图移动到当前位置
            mAMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng));
            if (event.location instanceof AMapLocation) {
                AMapLocation al = (AMapLocation) event.location;
                //显示城市名称
                if (!mCity.getText().toString().equals(al.getCity())) {
                    mCity.setText(al.getCity());
                }
                mCityEntity = new CityEntity();
                mCityEntity.setAreaCode(al.getCityCode());
                mCityEntity.setStationCity(al.getCity());
                App.getInstance().getGlobalModel().setCityEntity(mCityEntity);
                setCity(al.getCityCode());

            }
        }
        mCurrentLocation = event.location;
        //如果定位成功且充电柜列表为空,则加载充电柜列表
        if (event.location instanceof AMapLocation) {
            getCabinetList(((AMapLocation) event.location).getCityCode());
        }
        //显示当前位置marker
        showCurrentPosition(latLng);
    }

    /**
     * 显示当前位置提示信息
     *
     * @param latLng
     */
    private void showCurrentPosition(LatLng latLng) {
        mLocationMarker.setPosition(latLng);
    }

    /**
     * 隐藏换电柜信息
     */
    private void hideCabinetInfo() {
        if (mCabinetInfoView.getVisibility() == View.VISIBLE) {
            mCabinetInfoView.setVisibility(View.GONE);
        }
    }

    /**
     * 显示当前选择的换电柜信息
     *
     * @param cabinetEntity
     */
    private void showCabinetInfo(CabinetEntity cabinetEntity) {
        boolean entitySubscribe = cabinetEntity.isSubscribe();
        if (cabinetEntity == null) {
            return;
        }

        if (mCurrentCabinet == cabinetEntity) {
            //点击的机柜正在显示则隐藏，否则显示
            if (mCabinetInfoView.getVisibility() == View.VISIBLE) {
                hideCabinetInfo();
            } else {
                mCabinetInfoView.setVisibility(View.VISIBLE);
                getFullBattery(cabinetEntity.getCabinetSn());
            }
        } else {
            mCabinetTitle.setText(String.format("名称:%s", cabinetEntity.getStation()));
            mCabinetSN.setText(String.format("SN码:%s", cabinetEntity.getCabinetSn()));
            mCabinetAddress.setText(String.format("地址:%s", cabinetEntity.getAddress()));
            mBespeakIntent = new Intent(this, BespeakActivity.class);
            mBespeakIntent.putExtra(CABINETTITLE, cabinetEntity.getStation());
            mBespeakIntent.putExtra(CABINETSN, cabinetEntity.getCabinetSn());
            mBespeakIntent.putExtra(CABINETADDRESS, cabinetEntity.getAddress());

            if (entitySubscribe) {
                mBtnBespeak.setVisibility(View.VISIBLE);
            } else {
                mBtnBespeak.setVisibility(View.INVISIBLE);
            }
            mCabinetInfoView.setVisibility(View.VISIBLE);
            mCurrentCabinet = cabinetEntity;
            getFullBattery(cabinetEntity.getCabinetSn());
        }

    }

    /**
     * 点击事件监听回调
     *
     * @param view 是哪个view被点击了
     */
    public void onClick(View view) {
        if (view.getId() != R.id.btn_navi) {
            hideCabinetInfo();
        }
        switch (view.getId()) {
            case R.id.btn_bespeak:
                //预约换电
                startActivity(mBespeakIntent);
                break;
            case R.id.btn_shoping_car:
                //商城
                startActivity(new Intent(this, ShopActivity.class));
                break;
            case R.id.btn_refresh:
                //刷新
                refresh();
                break;
            case R.id.btn_user:
                //用户中心
                userCenter();
                break;
            case R.id.btn_scan:
                //扫码换电
                scan();
                break;
            case R.id.btn_position:
                //显示前位置
                getLocation();
                break;
            case R.id.btn_navi:
                //导航到机柜
                if (Utils.isOPenGPS(MapActivity.this)) {
                    naviTo(mCurrentCabinet.getLatitude(), mCurrentCabinet.getLongitude());
                } else {
                    // 转到手机GPS设置界面
                    openPGSDialog();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_OPEN_GPS) {
            if (Utils.isOPenGPS(MapActivity.this)) {
                naviTo(mCurrentCabinet.getLatitude(), mCurrentCabinet.getLongitude());
            } else {
                Toast.makeText(this, "未打开GPS,无法使用导航功能", Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == REQUEST_CODE_SCAN_CODE) {
            LogUtils.d("扫码换电测试1");
            //处理扫码换电
            if (resultCode == RESULT_OK) {
                String result = data.getStringExtra("scan_result");
                getPresenter().handleScanResult(result);

            }
        }
    }

    /**
     * 打开扫码界面
     */
    public void scan() {
        Intent intent = new Intent(this, CaptureActivity.class);
        startActivityForResult(intent, REQUEST_CODE_SCAN_CODE);
    }

    /**
     * 打开用户中心
     */
    public void userCenter() {
        Intent intent = new Intent(this, PersonInfoActivity.class);
        startActivity(intent);
    }

    /**
     * 刷新电量信息
     */
    public void refresh() {
        showLoading("获取数据...");
        startService(new Intent(this, BatteryService.class));
        if (mCityEntity != null) {
            getCabinetList(mCityEntity.getAreaCode());
        } else {
            getCabinetList("0");
        }
    }

    /**
     * 显示当前位置
     */
    public void getLocation() {
        if (mCurrentLocation != null) {
            LatLng latLng = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
            showCurrentPosition(latLng);
            mAMap.animateCamera(CameraUpdateFactory.changeLatLng(latLng));
        } else {
            startService(new Intent(this, LocationService.class));
        }
    }

    /**
     * 打开GPS对话框
     */
    private void openPGSDialog() {
        new AlertDialog.Builder(this)
                .setIcon(null)
                .setCancelable(true)
                .setMessage("GPS功能已关闭，导航需要打开GPS")
                .setPositiveButton(R.string.alert_yes_button, (dialog, which) -> {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivityForResult(intent, REQUEST_CODE_OPEN_GPS); // 设置完成后返回到原来的界面
                })
                .setNegativeButton(R.string.alert_no_button, (dialog, which) -> dialog.dismiss()).create().show();
    }

    private void naviTo(double latitude, double longitude) {
        if (mCurrentLocation != null) {
            Intent intent = new Intent(this, NaviActivity.class);
            NaviLatLng startPoint = new NaviLatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
            NaviLatLng endPoint = new NaviLatLng(latitude, longitude);
            intent.putExtra("naviType", NaviActivity.NAVI_TYPE_RIDE);
            intent.putExtra("startPoint", startPoint);
            intent.putExtra("endPoint", endPoint);
            startActivity(intent);
        } else {
            Toast.makeText(this, "定位失败，不能导航", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 初始化海量点
     */
    private void initMultiPoint() {
        MultiPointOverlayOptions overlayOptions = new MultiPointOverlayOptions();

        overlayOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.cabinet));//设置图标
        overlayOptions.anchor(0.5f, 0.5f); //设置锚点
        mMultiPointOverlay = mAMap.addMultiPointOverlay(overlayOptions);
    }

    // 定义海量点点击事件
/*    AMap.OnMultiPointClickListener mMultiPointClickListener = new AMap.OnMultiPointClickListener() {
        // 海量点中某一点被点击时回调的接口
        // 返回 true 则表示接口已响应事件，否则返回false
        @Override
        public boolean onPointClick(MultiPointItem pointItem) {
            Log.d("cgd", "MultiPointItem:" + pointItem.getTitle());
            showCabinetInfo((CabinetEntity) pointItem.getObject());
            return true;
        }
    };*/
    // 定义 Marker 点击事件监听
    AMap.OnMarkerClickListener markerClickListener = new AMap.OnMarkerClickListener() {
        // marker 对象被点击时回调的接口
        // 返回 true 则表示接口已响应事件，否则返回false
        @Override
        public boolean onMarkerClick(Marker marker) {

            clickMarker = marker;
            showCabinetInfo(mCabinetList.get(Integer.parseInt(marker.getSnippet())));
            return true;
        }
    };

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_SHOW_CABINET_LIST:
                useMarker();
                break;
            case MSG_SHOW_FULL_BATTERY_COUNT:

                mFullNum.setText(String.format("可换电池数量:%d", msg.arg1));
                mBespeakIntent.putExtra(FULLNUM, msg.arg1);
                clickMarker.setIcon(BitmapDescriptorFactory.fromResource(getResources().getIdentifier("cabinet" + msg.arg1, "drawable", getPackageName())));
                break;
            default:
                break;
        }
    }

    /**
     * 使用标注点
     */
    private void useMarker() {

        int pos = 0;
        ArrayList<MarkerOptions> markerOptionsList = new ArrayList<>();
        for (CabinetEntity entity : mCabinetList) {
            if (entity == null) {
                continue;
            }
            LatLng latLng = new LatLng(entity.getLatitude(), entity.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title(entity.getStation());
            //用snippet模拟pos
            markerOptions.snippet(String.valueOf(pos++));
            int usableCount = entity.getUsableCount();
            //根据图片名获取对应id
            if (usableCount >= 0 && usableCount <= 18) {
                markerOptions.icon(BitmapDescriptorFactory.fromResource(getResources().getIdentifier("cabinet" + usableCount, "drawable", getPackageName())));
            } else {
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.cabinet0));
            }
            markerOptions.setFlat(true);//设置marker平贴地图效果
            markerOptionsList.add(markerOptions);
        }
        //是否改变地图状态以至于所有的marker对象都在当前地图可视区域范围内显示。
        mAMap.addMarkers(markerOptionsList, false);
    }

    /**
     * 使用海量点
     */
    private void useMultiPoint() {

        List<MultiPointItem> mMultiPointItemList = new ArrayList<>();
        for (CabinetEntity entity : mCabinetList) {

            if (entity == null) {
                continue;
            }
            LatLng latLng = new LatLng(entity.getLatitude(), entity.getLongitude());
            MultiPointItem multiPointItem = new MultiPointItem(latLng);
            multiPointItem.setTitle(entity.getStation());
            multiPointItem.setObject(entity);
            mMultiPointItemList.add(multiPointItem);
        }

        //不知道为什么无法更新海量点，所以这里只能销毁，再重新初始化了
        mMultiPointOverlay.remove();
        mMultiPointOverlay.destroy();
        initMultiPoint();
        mMultiPointOverlay.setItems(mMultiPointItemList);
    }

    @Override
    public void onLoadCabinetList(BaseEntity<List<CabinetEntity>> ret) {
        mCabinetList.clear();
        if (ret != null && ret.getCode() == 0) {
            mCabinetList.addAll(ret.getData());
        }
        sendEmptyMessage(MSG_SHOW_CABINET_LIST);
    }

    /**
     * 获取电柜列表
     *
     * @param cityCode 城市区号
     */
    private void getCabinetList(String cityCode) {
        double longitude = 0;
        double latitude = 0;
        if (mCurrentLocation != null){
            longitude = mCurrentLocation.getLongitude();
            latitude = mCurrentLocation.getLatitude();
        }
        getPresenter().getCabinetList(cityCode, longitude, latitude);
    }


    @Override
    public void onLoadFullBattery(BaseEntity<Map<String, Integer>> ret) {
        Message msg = Message.obtain();
        msg.what = MSG_SHOW_FULL_BATTERY_COUNT;
        if (ret != null && ret.getCode() == 0) {
            msg.arg1 = ret.getData().get("count");
        }
        sendMessage(msg);
    }

    /**
     * 获取可换电池数量
     *
     * @param sn 电柜SN
     */
    private void getFullBattery(String sn) {

        mFullNum.setText("可换电池数量:加载中...");
        getPresenter().getFullBattery(sn);

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

//    @Override
//    public void onBackPressed() {
//        quitDialog();
//    }

    /**
     * 电量变化通知
     *
     * @param info
     */
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onMessageEvent(MessageEvent.BatteryInfo info) {

        Log.d("cgd", "update battery info");
        hideLoading();
        if (info.sop < 0) {
            battery_num.setVisibility(View.INVISIBLE);
        } else {
            int i = (int) Math.ceil(info.sop / 20.0f);//20%一格电
            battery_num.setVisibility(View.VISIBLE);
            battery_numPP.setText(String.format("%d%%", info.sop));
            if (i > 5) {
                i = 5;
            }
            battery_num.setImageResource(getResources().getIdentifier("battery_" + i, "drawable", getPackageName()));
        }
    }
}
