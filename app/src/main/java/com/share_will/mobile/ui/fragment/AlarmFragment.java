package com.share_will.mobile.ui.fragment;

import android.Manifest;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.share_will.mobile.App;
import com.share_will.mobile.MessageEvent;
import com.share_will.mobile.R;
import com.share_will.mobile.model.entity.AlarmEntity;
import com.share_will.mobile.presenter.AlarmFragmentPresenter;
import com.share_will.mobile.presenter.HomeFragmentPresenter;
import com.share_will.mobile.services.LocationService;
import com.share_will.mobile.ui.activity.AlarmListActivity;
import com.share_will.mobile.ui.dialog.InfoWindows;
import com.share_will.mobile.ui.views.IAlarmFragmentView;
import com.share_will.mobile.ui.views.IHomeFragmentView;
import com.ubock.library.annotation.PresenterInjector;
import com.ubock.library.base.BaseEntity;
import com.ubock.library.base.BaseFragment;
import com.ubock.library.utils.DateUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import permissions.dispatcher.NeedsPermission;

public class AlarmFragment extends BaseFragment<AlarmFragmentPresenter> implements IHomeFragmentView, View.OnClickListener, IAlarmFragmentView {
    private MapView mMapView = null;
    private AMap mAMap = null;
    private InfoWindows mInfoWindows;
    private Marker mLocationMarker = null;
    private Location mCurrentLocation = null;
    private ImageButton btnAlarmRefresh;
    private ImageButton btnAlarmPosition;
    private Button btnAlarmClose;
    private TextView mAlarmTitle;
    private List<AlarmEntity.SmokeBean> mSmokeList = new ArrayList<>();
    private AlarmEntity.SmokeBean mClickSmoke;

    @PresenterInjector
    HomeFragmentPresenter presenter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_alarm;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        setTitle("烟感告警");
        showBackMenu(false);
        EventBus.getDefault().register(this);
        btnAlarmRefresh = view.findViewById(R.id.btn_alarm_refresh);
        btnAlarmPosition = view.findViewById(R.id.btn_alarm_position);
        btnAlarmClose = view.findViewById(R.id.btn_alarm_close);
        mAlarmTitle = view.findViewById(R.id.tv_alarm_title);
        btnAlarmRefresh.setOnClickListener(this);
        btnAlarmPosition.setOnClickListener(this);
        btnAlarmClose.setOnClickListener(this);
        mAlarmTitle.setOnClickListener(this);
        initMap(view, savedInstanceState);
        getLocation();
        presenter.getAlarmList(App.getInstance().getUserId(), App.getInstance().getToken());
    }


    private void initMap(View view, Bundle savedInstanceState) {
        //获取地图控件引用
        mMapView = view.findViewById(R.id.map_alarm);
        mMapView.onSaveInstanceState(savedInstanceState);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView.onCreate(savedInstanceState);
        //初始化地图控制器对象
        if (mAMap == null) {
            mAMap = mMapView.getMap();
            mAMap.getUiSettings().setZoomControlsEnabled(false);
            mAMap.getUiSettings().setLogoPosition(AMapOptions.LOGO_POSITION_BOTTOM_CENTER);
            mAMap.moveCamera(CameraUpdateFactory.zoomTo(14));
            mInfoWindows = new InfoWindows(getActivity());
            mAMap.setInfoWindowAdapter(mInfoWindows);
            mAMap.setOnMarkerClickListener(markerClickListener);
            //添加Marker显示定位位置
            if (mLocationMarker == null) {
                //如果是空的添加一个新的,icon方法就是设置定位图标，可以自定义
                mLocationMarker = mAMap.addMarker(new MarkerOptions()
                        .visible(true)
                        .anchor(0.5f, 0.5f) //设置锚点
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.gps_point)));
            }
        }

    }

    /**
     * 关闭点击的告警
     */
    private void closeAlarm() {
        if (mClickSmoke != null) {
            getPresenter().closeAlarm(App.getInstance().getUserId(), mClickSmoke.getDevtype(), mClickSmoke.getDeveui());
        }
    }

    private void refresh() {
        presenter.getAlarmList(App.getInstance().getUserId(), App.getInstance().getToken());
    }

    // 定义 Marker 点击事件监听
    AMap.OnMarkerClickListener markerClickListener = new AMap.OnMarkerClickListener() {
        // marker 对象被点击时回调的接口
        // 返回 true 则表示接口已响应事件，否则返回false
        @Override
        public boolean onMarkerClick(Marker marker) {
            showCloseButton(mSmokeList.get(Integer.parseInt(marker.getSnippet())));
            return true;
        }
    };

    private void showCloseButton(AlarmEntity.SmokeBean data) {
        if (mClickSmoke == data) {
            //点击的正在显示则隐藏，否则显示
            if (btnAlarmClose.getVisibility() == View.VISIBLE) {
                btnAlarmClose.setVisibility(View.GONE);
            } else {
                btnAlarmClose.setVisibility(View.VISIBLE);
            }
        } else {
            btnAlarmClose.setVisibility(View.VISIBLE);
            mClickSmoke = data;
        }
    }

    /**
     * 使用标注点
     *
     * @param data
     */
    private void useMarker(AlarmEntity data) {
        int pos = 0;
        ArrayList<MarkerOptions> markerOptionsList = new ArrayList<>();
        for (AlarmEntity.SmokeBean entity : data.getSmoke()) {
            if (entity == null) {
                continue;
            }
            LatLng latLng = new LatLng(entity.getLatitude(), entity.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title(entity.getTitle());
            //用snippet模拟pos
            markerOptions.snippet(String.valueOf(pos++));
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_alarm_map_marker));
            markerOptions.setFlat(true);//设置marker平贴地图效果
            markerOptionsList.add(markerOptions);
        }
        //是否改变地图状态以至于所有的marker对象都在当前地图可视区域范围内显示。
        mAMap.addMarkers(markerOptionsList, false);
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

    /**
     * 显示当前位置提示信息
     *
     * @param latLng
     */
    private void showCurrentPosition(LatLng latLng) {
        mLocationMarker.setPosition(latLng);
    }

    @NeedsPermission({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    void onAllowPermission() {
        Log.d("cgd", "onAllowPermission");
        getActivity().startService(new Intent(getActivity(), LocationService.class));
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
            getActivity().startService(new Intent(getActivity(), LocationService.class));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_alarm_refresh:
                //刷新
                refresh();
                break;
            case R.id.btn_alarm_position:
                getLocation();
                break;
            case R.id.btn_alarm_close:
                closeAlarm();
                break;
            case R.id.tv_alarm_title:
                startActivity(new Intent(getContext(), AlarmListActivity.class));
                break;
        }

    }

    @Override
    public void onLoadAlarmResult(BaseEntity<AlarmEntity> data) {
        if (data != null) {
            mSmokeList.clear();
            mSmokeList.addAll(data.getData().getSmoke());
            mAlarmTitle.setVisibility(View.VISIBLE);
            AlarmEntity.SmokeBean smokeBean = data.getData().getSmoke().get(0);
            mAlarmTitle.setText(DateUtils.timeStampToString(smokeBean.getAlarmtime(), DateUtils.HHMMSS) + smokeBean.getPositionName() + "告警,点击查看详情......");
            useMarker(data.getData());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
        EventBus.getDefault().unregister(this);
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
        btnAlarmClose.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onCloseAlarmResult(BaseEntity<Object> s) {
        if (s != null) {
            btnAlarmClose.setVisibility(View.INVISIBLE);
        }
    }

}
