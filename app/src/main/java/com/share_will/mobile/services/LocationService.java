package com.share_will.mobile.services;

import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.share_will.mobile.App;
import com.share_will.mobile.MessageEvent;
import com.share_will.mobile.model.entity.LocationEntity;
import com.share_will.mobile.model.entity.LocationEntityDao;
import com.share_will.mobile.presenter.LocationServicePresenter;
import com.share_will.mobile.ui.views.LocationServiceView;
import com.share_will.mobile.utils.DBUtils;
import com.share_will.mobile.utils.ThreadPools;
import com.ubock.library.base.BaseEntity;
import com.ubock.library.base.BaseService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.greendao.query.QueryBuilder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class LocationService extends BaseService<LocationServicePresenter> implements AMapLocationListener, LocationServiceView {
    //声明mlocationClient对象
    public AMapLocationClient mlocationClient;
    //声明mLocationOption对象
    public AMapLocationClientOption mLocationOption = null;

    //上传位置和里程数消息
    public final static int MSG_UPLOAD_POSITION = 100;
    //上传位置和里程数时间间隔，单位：ms
    private final int mUploadPositionInterval = 5 * 60 * 1000;

    public LocationService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initLocation();
        mHandler.sendEmptyMessageDelayed(MSG_UPLOAD_POSITION, 1000);
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_UPLOAD_POSITION:
                    sendEmptyMessageDelayed(MSG_UPLOAD_POSITION, mUploadPositionInterval);
                    uploadPosition();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("cgd", "LocationService onStartCommand");
        if (mlocationClient != null) {
            if (!mlocationClient.isStarted()) {
                // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
                // 注意设置合适的定位时间的间隔（最小间隔支持为1000ms），并且在合适时间调用stopLocation()方法来取消定位请求
                // 在定位结束后，在合适的生命周期调用onDestroy()方法
                // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
                //启动定位
                mlocationClient.startLocation();
            } else {
                AMapLocation location = mlocationClient.getLastKnownLocation();
                if (location != null) {
                    EventBus.getDefault().post(new MessageEvent.LocationEvent(location));
                }
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("cgd", "LocationService onDestroy");
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
            mlocationClient = null;
        }
        mHandler.removeCallbacksAndMessages(null);
    }

    private void initLocation() {

        mlocationClient = new AMapLocationClient(this);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位监听
        mlocationClient.setLocationListener(this);
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationMode.Battery_Saving);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(30 * 60 * 1000);
        //设置定位参数
        mlocationClient.setLocationOption(mLocationOption);

    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        mlocationClient.stopLocation();
        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                amapLocation.getLatitude();//获取纬度
                amapLocation.getLongitude();//获取经度
                Log.e("cgd", String.format("location Longitude:%f,Latitude:%f", amapLocation.getLongitude(), amapLocation.getLatitude()));
                EventBus.getDefault().post(new MessageEvent.LocationEvent(amapLocation));
                long timestamp = System.currentTimeMillis();
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String date = df.format(new Date(timestamp));
                LocationEntity entity = new LocationEntity(null, amapLocation.getLongitude(), amapLocation.getLatitude(), 0, timestamp, date);
                save(entity);
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("cgd", "location Error, ErrCode:"
                        + amapLocation.getErrorCode() + ", errInfo:"
                        + amapLocation.getErrorInfo());
            }
        }
    }

    private void save(LocationEntity entity) {
        ThreadPools.execute(new SaveThread(entity));
    }

    @Override
    public void onUploadPosition(BaseEntity<Object> ret) {
        if (ret.getCode() != 0) {
            Log.d("cgd", "uploadPosition failed");
        }
    }

    /**
     * 上传位置和里程数
     */
    private void uploadPosition() {
        if (!App.getInstance().isLogin()) {
            Log.d("cgd", "unLogin");
            return;
        }
        Thread thread = new Thread() {
            public void run() {
                QueryBuilder<LocationEntity> builder = DBUtils.getDaoSession().getLocationEntityDao().queryBuilder();
                List<LocationEntity> list = builder.limit(1).orderDesc(LocationEntityDao.Properties.Id).list();
                if (list.size() > 0) {
                    LocationEntity e = list.get(0);
                    if (e.getRange() > 0 && getPresenter() != null) {
                        if (App.getInstance().getUserId() != null) {
                            getPresenter().uploadLocation(App.getInstance().getUserId(), e.getLongitude(), e.getLatitude(), e.getRange());
                        }
                    }
                }
            }
        };
        ThreadPools.execute(thread);
    }

    private static class SaveThread extends Thread {
        private LocationEntity entity;

        public SaveThread(LocationEntity entity) {
            this.entity = entity;
        }

        @Override
        public void run() {
            QueryBuilder<LocationEntity> builder = DBUtils.getDaoSession().getLocationEntityDao().queryBuilder();
            List<LocationEntity> list = builder.limit(1).orderDesc(LocationEntityDao.Properties.Id).list();
            if (list.size() > 0) {
                LocationEntity e = list.get(0);
                Log.d("saveLocation", String.format("oldLat:%f,oldLog:%f; newLat:%f, newLog:%f", e.getLatitude(), e.getLongitude(), entity.getLatitude(), entity.getLongitude()));
                if (entity.getLongitude() != e.getLongitude() ||
                        entity.getLatitude() != e.getLatitude()) {
                    LatLng start = new LatLng(entity.getLatitude(), entity.getLongitude());
                    LatLng end = new LatLng(e.getLatitude(), e.getLongitude());
                    float distance = AMapUtils.calculateLineDistance(start, end);
                    Log.d("saveLocation", String.format("distance=%f米", distance));
                    if (distance > 35) {
                        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                        String oldDate = df.format(new Date(e.getTimestamp()));
                        String newDate = df.format(new Date(entity.getTimestamp()));
                        if (oldDate.equals(newDate)) {//当天累加，第二天清零，重新计算
                            e.setRange(distance + e.getRange());
                        } else {
                            e.setRange(0);
                        }
                    } else {
                        Log.d("saveLocation", "ignore location");
                    }
                } else {
                    Log.d("saveLocation", "ignore location");
                }
                e.setLatitude(entity.getLatitude());
                e.setLongitude(entity.getLongitude());
                e.setDate(entity.getDate());
                e.setTimestamp(entity.getTimestamp());
                DBUtils.getDaoSession().getLocationEntityDao().update(e);
            } else {
                DBUtils.getDaoSession().getLocationEntityDao().insert(this.entity);
            }
        }
    }
}
