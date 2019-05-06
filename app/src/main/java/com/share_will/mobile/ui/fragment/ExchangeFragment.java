package com.share_will.mobile.ui.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MultiPointItem;
import com.amap.api.maps.model.MultiPointOverlay;
import com.amap.api.maps.model.MultiPointOverlayOptions;
import com.amap.api.navi.model.NaviLatLng;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RidePath;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;
import com.amap.overlay.RideRouteOverlay;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.share_will.mobile.App;
import com.share_will.mobile.MessageEvent;
import com.share_will.mobile.R;
import com.share_will.mobile.model.entity.CabinetEntity;
import com.share_will.mobile.model.entity.CityEntity;
import com.share_will.mobile.presenter.HomePresenter;
import com.share_will.mobile.services.BatteryService;
import com.share_will.mobile.services.LocationService;
import com.share_will.mobile.ui.activity.BespeakActivity;
import com.share_will.mobile.ui.activity.CaptureActivity;
import com.share_will.mobile.ui.activity.NaviActivity;
import com.share_will.mobile.ui.views.HomeView;
import com.share_will.mobile.utils.Utils;
import com.ubock.library.base.BaseEntity;
import com.ubock.library.base.BaseFragment;
import com.ubock.library.ui.dialog.ToastExt;
import com.ubock.library.utils.DateUtils;
import com.ubock.library.utils.LogUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static com.amap.api.services.route.RouteSearch.RIDING_RECOMMEND;


public class ExchangeFragment extends BaseFragment<HomePresenter> implements HomeView, View.OnClickListener, AMap.InfoWindowAdapter {

    TextureMapView mMapView = null;
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

    private Button mBtnBespeak;
    private Button mNaviBtn;
    private ImageButton mRefreshBtn;
    private ImageButton mPositionBtn;
    private ImageButton mScannerBtn;

    private View mCabinetInfoView;
    private TextView mCabinetTitle;
    private TextView mCabinetSN;
    private TextView mFullNum;
    private TextView mEmptyHouse;
    private TextView mCabinetAddress;
    private CabinetEntity mCurrentCabinet;
    public final static String CABINETTITLE = "cabinettitle";
    public final static String CABINETSN = "CabinetSN";
    public final static String CABINETADDRESS = "CabinetAddress";
    public final static String FULLNUM = "FullNum";

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
    /**
     * 点击地图标注状态 0:未点击 1:点击后
     */
    private int clickType = 0;
    private RideRouteOverlay rideRouteOverlay;
    private PopupWindow mPopupWindow;
    private TextView mTvAddress;
    private View mViewPopup;
    private long ridePathDuration;
    private float ridePathDistance;
    private TextView mDistance;
    private TextView mDuration;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_exchange;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        setTitle("换电服务");
        showBackMenu(false);
        mNaviBtn = view.findViewById(R.id.btn_navi);
        mNaviBtn.setOnClickListener(this);
        mRefreshBtn = view.findViewById(R.id.btn_refresh);
        mRefreshBtn.setOnClickListener(this);
        mScannerBtn = view.findViewById(R.id.btn_scan);
        mScannerBtn.setOnClickListener(this);
        mPositionBtn = view.findViewById(R.id.btn_position);
        mPositionBtn.setOnClickListener(this);
        mCabinetInfoView = view.findViewById(R.id.cabinet_info);
        mCabinetTitle = view.findViewById(R.id.cabinet_title);
        mCabinetSN = view.findViewById(R.id.cabinet_sn);
        mFullNum = view.findViewById(R.id.full_num);
        mEmptyHouse = view.findViewById(R.id.cabinet_empty_house);
//        mCabinetAddress = view.findViewById(R.id.cabinet_address);
        battery_num = view.findViewById(R.id.battery_num);
        battery_numPP = view.findViewById(R.id.battery_numPP);
        EventBus.getDefault().register(this);
        mBtnBespeak = view.findViewById(R.id.btn_bespeak);
        mBtnBespeak.setOnClickListener(this);
        mCity = view.findViewById(R.id.tv_city);
        mCity.setOnClickListener(this);
        //获取地图控件引用
        mMapView = view.findViewById(R.id.map);

        mViewPopup = LayoutInflater.from(getContext()).inflate(R.layout.popupwindow_map_marker, null);
        mPopupWindow = new PopupWindow(mViewPopup, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setFocusable(false);
        //下面的是设置外部是否可以点击
        mPopupWindow.setOutsideTouchable(false);
        mViewPopup.findViewById(R.id.tv_pop_map_marker_gps).setOnClickListener(this);
        mTvAddress = mViewPopup.findViewById(R.id.tv_pop_map_marker_address);
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
            mAMap.setInfoWindowAdapter(this);
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
        mCityPickerView = new OptionsPickerBuilder(this.getContext(), new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                CityEntity city = getPresenter().getModel().getCity(options1);
                if (city != null) {
                    mCityEntity = city;
                    App.getInstance().getGlobalModel().setCityEntity(city);
                    if (!TextUtils.isEmpty(city.getStationCity())) {
                        mCity.setText(city.getStationCity());
                    }
                    getCabinetList(city.getAreaCode());
                    LatLng latLng = new LatLng(city.getLatitude(), city.getLongitude());
                    mAMap.animateCamera(CameraUpdateFactory.changeLatLng(latLng));
                }
            }
        }).setCyclic(false, false, false).build();

        getContext().startService(new Intent(this.getActivity(), BatteryService.class));
        refresh();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPresenter().getCityList();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause()，暂停地图的绘制
        mMapView.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onLoadCityList(BaseEntity<List<CityEntity>> ret) {
        if (ret != null && ret.getData() != null) {
            mCityList = ret.getData();
            mCityPickerView.setPicker(mCityList);
        }
//        MapActivityPermissionsDispatcher.onAllowPermissionWithPermissionCheck(this);
    }

    public void showCityDialog() {
        if (mCityList == null || mCityList.isEmpty()) {
            getPresenter().getCityList();
        }
        mCityPickerView.show();
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
                if (!TextUtils.isEmpty(city.getStationCity())) {
                    mCity.setText(city.getStationCity());
                }
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
            mPopupWindow.dismiss();
//            if (clickMarker != null) {
//                clickMarker.hideInfoWindow();
//            }
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
                clickType = 0;
            } else {
                mCabinetInfoView.setVisibility(View.VISIBLE);
                if (!mPopupWindow.isShowing()) {
                    showPopupWindow();
                }
                getFullBattery(cabinetEntity.getCabinetSn());
                clickType = 1;
            }
        } else {
            if (!TextUtils.isEmpty(cabinetEntity.getStation())) {
                mCabinetTitle.setText(String.format("名称:%s", cabinetEntity.getStation()));
            }
            if (!TextUtils.isEmpty(cabinetEntity.getCabinetSn())) {
                mCabinetSN.setText(String.format("SN码:%s", cabinetEntity.getCabinetSn()));
            }
            if (!TextUtils.isEmpty(cabinetEntity.getAddress())) {
                mTvAddress.setText(String.format("地址:%s", cabinetEntity.getAddress()));
            }
            mEmptyHouse.setText(String.format("空仓数量:%s/%s", cabinetEntity.getEmptyHouse(), cabinetEntity.getWareHouseTotal()));

//            mBespeakIntent = new Intent(this.getActivity(), BespeakActivity.class);
//            mBespeakIntent.putExtra(CABINETTITLE, cabinetEntity.getStation());
//            mBespeakIntent.putExtra(CABINETSN, cabinetEntity.getCabinetSn());
//            mBespeakIntent.putExtra(CABINETADDRESS, cabinetEntity.getAddress());

            mCabinetInfoView.setVisibility(View.VISIBLE);
            if (!mPopupWindow.isShowing()) {
                showPopupWindow();
            }
            mCurrentCabinet = cabinetEntity;

            getFullBattery(cabinetEntity.getCabinetSn());

            clickType = 1;
        }
    }

    /**
     * 点击机柜后显示地址和开始导航按钮弹窗
     */
    private void showPopupWindow() {
        mPopupWindow.showAtLocation(getView(), Gravity.BOTTOM, 0, 0);
    }

    /**
     * 点击事件监听回调
     *
     * @param view 是哪个view被点击了
     */
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.tv_city:
                hideCabinetInfo();
                showCityDialog();
                break;
            case R.id.btn_bespeak:
                //预约换电
                startActivity(mBespeakIntent);
                break;
            case R.id.btn_refresh:
                //刷新
                refresh();
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
                if (Utils.isOPenGPS(this.getContext())) {
                    naviTo(mCurrentCabinet.getLatitude(), mCurrentCabinet.getLongitude());
                } else {
                    // 转到手机GPS设置界面
                    openPGSDialog();
                }
                break;
            case R.id.tv_pop_map_marker_gps:
                //导航到机柜
                if (Utils.isOPenGPS(this.getContext())) {
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_OPEN_GPS) {
            if (Utils.isOPenGPS(this.getContext())) {
                naviTo(mCurrentCabinet.getLatitude(), mCurrentCabinet.getLongitude());
            } else {
                Toast.makeText(this.getContext(), "未打开GPS,无法使用导航功能", Toast.LENGTH_LONG).show();
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
        Intent intent = new Intent(this.getActivity(), CaptureActivity.class);
        startActivityForResult(intent, REQUEST_CODE_SCAN_CODE);
    }

    /**
     * 刷新电量信息
     */
    public void refresh() {
        clickType = 0;
        showLoading("获取数据...");
        getContext().startService(new Intent(this.getActivity(), BatteryService.class));
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
        clickType = 0;
        if (mCurrentLocation != null) {
            LatLng latLng = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
            showCurrentPosition(latLng);
            mAMap.animateCamera(CameraUpdateFactory.changeLatLng(latLng));
        } else {
            getContext().startService(new Intent(this.getActivity(), LocationService.class));
        }
    }

    /**
     * 打开GPS对话框
     */
    private void openPGSDialog() {
        new AlertDialog.Builder(this.getContext())
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
            Intent intent = new Intent(this.getActivity(), NaviActivity.class);
            NaviLatLng startPoint = new NaviLatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
            NaviLatLng endPoint = new NaviLatLng(latitude, longitude);
            intent.putExtra("naviType", NaviActivity.NAVI_TYPE_RIDE);
            intent.putExtra("startPoint", startPoint);
            intent.putExtra("endPoint", endPoint);
            startActivity(intent);
        } else {
            Toast.makeText(this.getContext(), "定位失败，不能导航", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 初始化海量点
     */
    private void initMultiPoint() {
        MultiPointOverlayOptions overlayOptions = new MultiPointOverlayOptions();
        overlayOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_map_cabinet));//设置图标
        overlayOptions.anchor(0.5f, 0.5f); //设置锚点
        mMultiPointOverlay = mAMap.addMultiPointOverlay(overlayOptions);
    }


    // 定义 Marker 点击事件监听
    AMap.OnMarkerClickListener markerClickListener = new AMap.OnMarkerClickListener() {
        // marker 对象被点击时回调的接口
        // 返回 true 则表示接口已响应事件，否则返回false
        @Override
        public boolean onMarkerClick(Marker marker) {

            if (mCabinetList.size() > 0) {
                CabinetEntity cabinetEntity = mCabinetList.get(Integer.parseInt(marker.getSnippet()));
                showCabinetInfo(cabinetEntity);
                if (cabinetEntity.isOnline()) {
                    showRideRoute(cabinetEntity);
                    marker.showInfoWindow();
                } else {
                    showMessage("设备离线中");
                    if (clickType == 1) {
                        if (rideRouteOverlay != null) {
                            rideRouteOverlay.removeFromMap();
                            clickMarker.hideInfoWindow();
                        }
                    }
                }
            }
            clickMarker = marker;
            return true;
        }
    };

    /**
     * 骑行路线图
     */
    private void showRideRoute(CabinetEntity cabinetEntity) {
        LogUtils.d(clickType + "===== 0:未点击 1:点击后=====");
        if (rideRouteOverlay != null) {
            rideRouteOverlay.removeFromMap();
        }
        RouteSearch routeSearch = new RouteSearch(getActivity());
        RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(new LatLonPoint(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()),
                new LatLonPoint(cabinetEntity.getLatitude(), cabinetEntity.getLongitude()));
        RouteSearch.RideRouteQuery query = new RouteSearch.RideRouteQuery(fromAndTo, RIDING_RECOMMEND);
        routeSearch.calculateRideRouteAsyn(query);
        routeSearch.setRouteSearchListener(new RouteSearch.OnRouteSearchListener() {
            @Override
            public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

            }

            @Override
            public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int i) {


            }

            @Override
            public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {

            }

            @Override
            public void onRideRouteSearched(RideRouteResult rideRouteResult, int errorCode) {
                if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
                    if (rideRouteResult != null && rideRouteResult.getPaths() != null) {
                        if (rideRouteResult.getPaths().size() > 0) {
                            RidePath ridePath = rideRouteResult.getPaths().get(0);

                            if (ridePath == null) {
                                return;
                            }
                            rideRouteOverlay = new RideRouteOverlay(getActivity(), mAMap, ridePath, rideRouteResult.getStartPos(), rideRouteResult.getTargetPos());
                            rideRouteOverlay.setNodeIconVisibility(false);//设置节点marker是否显示
                            rideRouteOverlay.removeFromMap();
                            rideRouteOverlay.addToMap();
                            ridePathDuration = ridePath.getDuration();
                            ridePathDistance = ridePath.getDistance();
                            LogUtils.d("骑行时间:" + ridePathDuration + "骑行距离:" + ridePathDistance);
                            if (ridePathDistance != 0) {
                                float v = ridePathDistance / 1000;
                                DecimalFormat decimalFormat = new DecimalFormat("0.0");
                                String p = decimalFormat.format(v);
                                mDistance.setText(p + "公里");
                            }
                            if (ridePathDuration != 0) {
                                mDuration.setText(ridePathDuration / 60 + "分钟");
                            }
                        }
                    }
                }
            }
        });

    }


    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_SHOW_CABINET_LIST:
                    useMarker();
                    break;
                case MSG_SHOW_FULL_BATTERY_COUNT:
                    mFullNum.setText(String.format("可换电池数量:%d/%d", msg.arg1, mCabinetList.get(Integer.parseInt(clickMarker.getSnippet())).getBatteryCount()));
//                    mBespeakIntent.putExtra(FULLNUM, msg.arg1);
//                    clickMarker.setIcon(BitmapDescriptorFactory.fromResource(getResources().getIdentifier("icon_battery" + msg.arg1, "drawable", getContext().getPackageName())));
                    break;
                default:
                    break;
            }
        }
    };


    /**
     * 使用标注点
     */
    private void useMarker() {
        mAMap.clear();
        //添加Marker显示定位位置
        //如果是空的添加一个新的,icon方法就是设置定位图标，可以自定义
        mLocationMarker = mAMap.addMarker(new MarkerOptions()
                .visible(true)
                .anchor(0.5f, 0.5f) //设置锚点
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.gps_point)));
        if (mCurrentLocation != null) {
            LatLng latLng = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
            showCurrentPosition(latLng);
        }
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

            markerOptions.snippet(String.valueOf(pos));

            int usableCount = entity.getUsableCount();
            //根据图片名获取对应id
            if (!entity.isOnline()) {
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_no_battery));
            } else {
                if (usableCount >= 0 && usableCount <= 18) {
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(getResources().getIdentifier("icon_battery_" + usableCount, "drawable", getContext().getPackageName())));
                } else {
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_battery_0));
                }
            }
            markerOptions.setFlat(true);//设置marker平贴地图效果
            markerOptionsList.add(markerOptions);
            pos++;
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
//        initMultiPoint();
        mMultiPointOverlay.setItems(mMultiPointItemList);
    }

    @Override
    public void onLoadCabinetList(BaseEntity<List<CabinetEntity>> ret) {
        mCabinetList.clear();
        if (ret != null && ret.getCode() == 0) {
            mCabinetList.addAll(ret.getData());
        }
        mHandler.sendEmptyMessage(MSG_SHOW_CABINET_LIST);
    }

    /**
     * 获取电柜列表
     *
     * @param cityCode 城市区号
     */
    private void getCabinetList(String cityCode) {
        double longitude = 0;
        double latitude = 0;
        if (mCurrentLocation != null) {
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
        mHandler.sendMessage(msg);
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
            battery_num.setImageResource(getResources().getIdentifier("battery_" + i, "drawable", getContext().getPackageName()));
        }
    }

    @Override
    public View getInfoWindow(Marker marker) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.window_map_exchange_rider_info, null);
        mDistance = view.findViewById(R.id.tv_window_map_rider_distance);
        mDuration = view.findViewById(R.id.tv_window_map_rider_duration);
        return view;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}
