package com.share_will.mobile.ui.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
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
import com.share_will.mobile.MessageEvent;
import com.share_will.mobile.R;
import com.share_will.mobile.model.entity.FixStationEnity;
import com.share_will.mobile.presenter.FixMapPresenter;
import com.share_will.mobile.services.LocationService;
import com.share_will.mobile.ui.views.IFixMapView;
import com.share_will.mobile.utils.Utils;
import com.ubock.library.base.BaseEntity;
import com.ubock.library.base.BaseFragmentActivity;
import com.ubock.library.utils.LogUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static com.amap.api.services.route.RouteSearch.RIDING_RECOMMEND;
import static com.share_will.mobile.ui.fragment.ExchangeFragment.REQUEST_CODE_OPEN_GPS;

public class FixMapActivity extends BaseFragmentActivity<FixMapPresenter> implements View.OnClickListener, IFixMapView, AMap.InfoWindowAdapter {
    private TextView tvPopMapFixAddress;
    private TextView tvPopMapFixShopName;
    private TextView tvPopMapFixName;
    private TextView tvPopMapFixBusiness;
    private ImageView ivPopMapFixGps;
    private ImageButton btnRefresh;
    private ImageButton btnPosition;
    private AMap mAMap = null;
    private Location mCurrentLocation = null;
    private Marker mLocationMarker = null;
    private MapView mMapView = null;
    private List<FixStationEnity> mFixStationList;
    private long ridePathDuration;
    private float ridePathDistance;
    private TextView mDistance;
    private TextView mDuration;
    private RideRouteOverlay rideRouteOverlay;
    private Marker clickMarker;
    private PopupWindow mPopupWindow;
    private TextView mTvAddress;
    private TextView mTvShopName;
    private TextView mTvBossName;
    private TextView mTvBusiness;
    private View mViewPopup;
    private View view;
    private FixStationEnity mCurrentStation;
    private View stationView;
    private View stationDetailView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_map_fix;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

        setTitle("车辆维修");
        EventBus.getDefault().register(this);

        btnRefresh = findViewById(R.id.btn_refresh);
        btnPosition = findViewById(R.id.btn_position);
        btnRefresh.setOnClickListener(this);
        btnPosition.setOnClickListener(this);

        view = findViewById(R.id.ll_view);

        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        //获取地图控件引用
        mMapView = findViewById(R.id.map);

        mViewPopup = LayoutInflater.from(this).inflate(R.layout.popupwindow_map_fix, null);
        mPopupWindow = new PopupWindow(mViewPopup, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setFocusable(false);
        //下面的是设置外部是否可以点击
        mPopupWindow.setOutsideTouchable(true);
        mViewPopup.findViewById(R.id.iv_pop_map_fix_gps).setOnClickListener(this);
        stationDetailView = mViewPopup.findViewById(R.id.ll_pop_view_station_detail);
        mTvAddress = mViewPopup.findViewById(R.id.tv_pop_map_fix_address);
        mTvShopName = mViewPopup.findViewById(R.id.tv_pop_map_fix_shopName);
        mTvBossName = mViewPopup.findViewById(R.id.tv_pop_map_fix_bossName);
        mTvBusiness = mViewPopup.findViewById(R.id.tv_pop_map_fix_business);
        stationView = findViewById(R.id.ll_view_station_detail);

        mMapView.onCreate(savedInstanceState);
        //初始化地图控制器对象
        if (mAMap == null) {
            mAMap = mMapView.getMap();
            mAMap.getUiSettings().setZoomControlsEnabled(false);
            mAMap.getUiSettings().setLogoPosition(AMapOptions.LOGO_POSITION_BOTTOM_CENTER);
            mAMap.moveCamera(CameraUpdateFactory.zoomTo(14));
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
        getLocation();
        getPresenter().loadFixStation();
    }

    private void moveToTop() {
        //控件所在布局为相对布局,所以显示样式由relativelayout来决定
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        //locationX,  locationY分别代表控件距离父控件的左边距和上边距
        params.bottomMargin = 30;
        params.leftMargin = 30;
        params.addRule(RelativeLayout.ABOVE, stationDetailView.getId());
        btnRefresh.setLayoutParams(params);
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

    public void refresh() {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_refresh:
                //刷新
                refresh();
                break;
            case R.id.btn_position:
                //显示前位置
                getLocation();
                break;
            case R.id.iv_pop_map_fix_gps:
                //导航到机柜
                if (Utils.isOPenGPS(this)) {
                    naviTo(mCurrentStation.getLatitude(), mCurrentStation.getLongitude());
                } else {
                    // 转到手机GPS设置界面
                    openPGSDialog();
                }
                break;
        }
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
        }
        mCurrentLocation = event.location;
        //显示当前位置marker
        showCurrentPosition(latLng);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
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
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 显示当前位置提示信息
     *
     * @param latLng
     */
    private void showCurrentPosition(LatLng latLng) {
        mLocationMarker.setPosition(latLng);
    }

    @Override
    public void onLoadStationResult(BaseEntity<List<FixStationEnity>> data) {
        if (data != null) {
            mFixStationList = data.getData();
            useMarker();
        }
    }

    // 定义 Marker 点击事件监听
    AMap.OnMarkerClickListener markerClickListener = new AMap.OnMarkerClickListener() {
        // marker 对象被点击时回调的接口
        // 返回 true 则表示接口已响应事件，否则返回false
        @Override
        public boolean onMarkerClick(Marker marker) {

            if (mFixStationList.size() > 0) {
                FixStationEnity enity = mFixStationList.get(Integer.parseInt(marker.getSnippet()));
                showRideRoute(enity);
                marker.showInfoWindow();
                showFixStationInfo(enity);
            }
            clickMarker = marker;
            return true;
        }
    };

    private void showFixStationInfo(FixStationEnity enity) {

        if (mCurrentStation == enity) {
            //点击的机柜正在显示则隐藏，否则显示
            if (mPopupWindow.isShowing()) {
                mPopupWindow.dismiss();
            } else {
                showPopupWindow();
            }
        } else {
            if (!TextUtils.isEmpty(enity.getStationName())) {
                mTvShopName.setText(String.format("店名:%s", enity.getStationName()));
            }
            if (!TextUtils.isEmpty(enity.getStationMaster())) {
                mTvBossName.setText(String.format("联系人:%s", enity.getStationMaster()));
            }
            if (!TextUtils.isEmpty(enity.getStationAddress())) {
                mTvAddress.setText(String.format("地址:%s", enity.getStationAddress()));
            }
            if (!TextUtils.isEmpty(enity.getBusinessHours())) {
                mTvBusiness.setText(String.format("营业时间:%s", enity.getBusinessHours()));
            }
            if (!mPopupWindow.isShowing()) {
                showPopupWindow();
            }
            mCurrentStation = enity;
        }
    }

    private void showPopupWindow() {
//        moveToTop();
//        stationView.setVisibility(View.VISIBLE);
        mPopupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
    }

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
        for (FixStationEnity entity : mFixStationList) {
            if (entity == null) {
                continue;
            }
            LatLng latLng = new LatLng(entity.getLatitude(), entity.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title(entity.getStationName());
            //用snippet模拟pos
            markerOptions.snippet(String.valueOf(pos));
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_marker_fix));
            markerOptions.setFlat(true);//设置marker平贴地图效果
            markerOptionsList.add(markerOptions);
            pos++;
        }
        //是否改变地图状态以至于所有的marker对象都在当前地图可视区域范围内显示。
        mAMap.addMarkers(markerOptionsList, false);
    }

    /**
     * 骑行路线图
     */
    private void showRideRoute(FixStationEnity enity) {
        if (rideRouteOverlay != null) {
            rideRouteOverlay.removeFromMap();
        }
        RouteSearch routeSearch = new RouteSearch(this);
        RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(new LatLonPoint(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()),
                new LatLonPoint(enity.getLatitude(), enity.getLongitude()));
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
                            rideRouteOverlay = new RideRouteOverlay(FixMapActivity.this, mAMap, ridePath, rideRouteResult.getStartPos(), rideRouteResult.getTargetPos());
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

    @Override
    public View getInfoWindow(Marker marker) {
        View view = LayoutInflater.from(this).inflate(R.layout.window_map_exchange_rider_info, null);
        mDistance = view.findViewById(R.id.tv_window_map_rider_distance);
        mDuration = view.findViewById(R.id.tv_window_map_rider_duration);
        return view;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}
