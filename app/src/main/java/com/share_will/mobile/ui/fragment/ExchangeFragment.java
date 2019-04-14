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
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.share_will.mobile.ui.activity.PersonInfoActivity;
import com.share_will.mobile.ui.dialog.InfoWindows;
import com.share_will.mobile.ui.views.HomeView;
import com.share_will.mobile.utils.Utils;
import com.ubock.library.base.BaseEntity;
import com.ubock.library.base.BaseFragment;
import com.ubock.library.ui.dialog.ToastExt;
import com.ubock.library.utils.LogUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class ExchangeFragment extends BaseFragment<HomePresenter> implements HomeView, View.OnClickListener{

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
    private InfoWindows mInfoWindows;
    private Button mBtnBespeak;
    private Button mNaviBtn;
    private ImageButton mRefreshBtn;
    private ImageButton mPositionBtn;
    private ImageButton mScannerBtn;

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
        mCabinetAddress = view.findViewById(R.id.cabinet_address);
        battery_num = view.findViewById(R.id.battery_num);
        battery_numPP = view.findViewById(R.id.battery_numPP);
        mBtnBespeak = view.findViewById(R.id.btn_bespeak);
        mBtnBespeak.setOnClickListener(this);
        mCity = view.findViewById(R.id.tv_city);
        mCity.setOnClickListener(this);
        //获取地图控件引用
        mMapView = view.findViewById(R.id.map);
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
            mInfoWindows = new InfoWindows(this.getActivity());
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
        mCityPickerView = new OptionsPickerBuilder(this.getContext(), new OnOptionsSelectListener() {
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

        getContext().startService(new Intent(this.getActivity(), BatteryService.class));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
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
            mBespeakIntent = new Intent(this.getActivity(), BespeakActivity.class);
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
            case R.id.tv_city:
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
     * 打开用户中心
     */
    public void userCenter() {
        Intent intent = new Intent(this.getActivity(), PersonInfoActivity.class);
        startActivity(intent);
    }

    /**
     * 刷新电量信息
     */
    public void refresh() {
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

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_SHOW_CABINET_LIST:
                    useMarker();
                    break;
                case MSG_SHOW_FULL_BATTERY_COUNT:

                    mFullNum.setText(String.format("可换电池数量:%d", msg.arg1));
                    mBespeakIntent.putExtra(FULLNUM, msg.arg1);
                    clickMarker.setIcon(BitmapDescriptorFactory.fromResource(getResources().getIdentifier("cabinet" + msg.arg1, "drawable", getContext().getPackageName())));
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
                markerOptions.icon(BitmapDescriptorFactory.fromResource(getResources().getIdentifier("cabinet" + usableCount, "drawable", getContext().getPackageName())));
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
}
