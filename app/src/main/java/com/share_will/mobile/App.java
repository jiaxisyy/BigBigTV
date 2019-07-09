package com.share_will.mobile;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.multidex.MultiDex;
import android.text.TextUtils;
import android.util.Log;

import com.facebook.stetho.Stetho;
import com.share_will.mobile.model.GlobalModel;
import com.share_will.mobile.model.entity.UserInfo;
import com.share_will.mobile.services.AlarmReceiver;
import com.share_will.mobile.services.BatteryService;
import com.share_will.mobile.services.UpgradeService;
import com.share_will.mobile.utils.DBUtils;
import com.share_will.mobile.utils.Utils;
import com.tencent.bugly.crashreport.CrashReport;
import com.ubock.library.base.BaseApp;
import com.ubock.library.base.BaseConfig;
import com.ubock.library.utils.AppUtils;
import com.ubock.library.utils.SharedPreferencesUtils;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;

/**
 * Created by ChenGD on 2018/3/4.
 *
 * @author chenguandu
 */

public class App extends BaseApp implements Application.ActivityLifecycleCallbacks {
    private AlarmManager mAlarmManager;
    private volatile int mActivityCount = 0;
    private volatile int mActivityResumeCount = 0;
    private static App mSelf;
    //app是否退到后台
    private boolean mIsAppGotoBackground = true;
    //检查状态消息
    private final static int MSG_CHECK_APP_STATUS = 0;
    private GlobalModel mGlobalModel = new GlobalModel();

    @Override
    public void onCreate() {
        mSelf = this;
        initLog();
        loginCache();
        initServer();
        super.onCreate();
        MultiDex.install(this);
        DBUtils.initDB(this);
        startService(new Intent(this, BatteryService.class));
        initBatteryAlarm();
        initLocationAlarm();
        Stetho.initializeWithDefaults(this);
        registerActivityLifecycleCallbacks(this);
        initUM();

        if (!BuildConfig.DEBUG) {
            initBugly();
        }

        startService(new Intent(this, UpgradeService.class));
    }

    /**
     * 友盟初始化
     */
    private void initUM() {
        UMConfigure.init(this, "5bc6e46af1f55656b70002e1"
                , "umeng", UMConfigure.DEVICE_TYPE_PHONE, "");
//        PlatformConfig.setQQZone("1107898057", "POsAiq23YLNr6WIa");
        PlatformConfig.setQQZone("101520067", "95daa9d54e676a8e8f6a9550aa476181");

        PlatformConfig.setWeixin("wx17004e2cd8d5caca", "3baf1193c85774b3fd9d18447d76cab0");
        PlatformConfig.setSinaWeibo("3921700954", "04b48b094faeb16683c32669824ebdad", "http://sns.whalecloud.com");
    }



    private void initBugly() {
        String packageName = getPackageName();
        // 获取当前进程名
        String processName = AppUtils.getProcessName(android.os.Process.myPid());
        // 设置是否为上报进程
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(this);
        strategy.setUploadProcess(processName == null || processName.equals(packageName));
        CrashReport.putUserData(getApplicationContext(), "UserId", getUserId());
        // 初始化Bugly
        CrashReport.initCrashReport(getApplicationContext(), "c78cfb3bf7", BuildConfig.DEBUG, strategy);
    }

    public static App getInstance() {
        return mSelf;
    }

    public static String getChannel() {
        return Utils.getMetaData(mSelf, Constant.CHANNEL);
    }

    private void loginCache() {
        UserInfo userInfo = SharedPreferencesUtils.getDeviceData(this, Constant.USER_INFO);
        getGlobalModel().setUserInfo(userInfo);
    }

    public boolean isLogin() {
        return !TextUtils.isEmpty(getGlobalModel().getUserInfo().getToken());
    }

    public String getUserId() {
        return getGlobalModel().getUserInfo().getUserId();
    }

    public String getToken() {
        return getGlobalModel().getUserInfo().getToken();
    }

    public GlobalModel getGlobalModel() {
        if (mGlobalModel == null) {
            mGlobalModel = new GlobalModel();
        }
        return mGlobalModel;
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_CHECK_APP_STATUS:
                    //这个消息是在onActivityPaused延迟1秒钟发送的，如果mActivityResumeCount还是0，就说明app已经退到后台
                    //因为一般情况下界面切换是用不到1秒钟时间，如果超过1秒了，那说明界面有问题了，需要优化了
                    //请注意：界面切换是先执行当前界面的onPause，才执行新界面的onResume的，
                    //所以不在onResume里判断mActivityResumeCount == 0来判断应用是否在后台
                    if (mActivityResumeCount == 0) {
                        mIsAppGotoBackground = true;
                    }
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 初始化服务器
     */
    private void initServer() {

        String server = SharedPreferencesUtils.getStringSF(this, Constant.KEY_SERVER_ADDRESS);
        if (!TextUtils.isEmpty(server)) {
            if (!server.endsWith("/")){
                server += "/";
            }
            BaseConfig.SERVER_HOST = server;
        }

        String project = SharedPreferencesUtils.getStringSF(this, Constant.KEY_SERVER_PROJECT);
        if (!TextUtils.isEmpty(project)) {
            if (!project.endsWith("/")){
                project += "/";
            }
            BaseConfig.PROJECT_NAME = project;
        }

        BaseConfig.API_SERVER_URL = String.format("%s%s", BaseConfig.SERVER_HOST, BaseConfig.PROJECT_NAME);
    }

    /**
     * Debug、Log功能初始化
     */
    private void initLog() {
        int logLevel = SharedPreferencesUtils.getIntergerSF(this, Constant.KEY_LOG_LEVEL);
        if (logLevel >= Log.VERBOSE && logLevel <= Log.ASSERT) {
            BaseConfig.LOG_LEVEL = logLevel;

        }
    }

    /**
     * 定时检测电量
     */
    private void initBatteryAlarm() {
        Log.d("cgd", "initBatteryAlarm");
        mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.HOUR_OF_DAY, 0);
//        calendar.set(Calendar.MINUTE, 0);
//        calendar.set(Calendar.SECOND, 0);

        Intent intent = new Intent(AlarmReceiver.ALARM_ACTION_BATTERY);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, intent, 0);

        long intervalMillis = 1 * 60 * 1000;
        mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + intervalMillis, intervalMillis, pi);
    }

    /**
     * 定时检测位置
     */
    private void initLocationAlarm() {
        Log.d("cgd", "initLocationAlarm");
        mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(AlarmReceiver.ALARM_ACTION_LOCATION);
        PendingIntent pi = PendingIntent.getBroadcast(this, 1, intent, 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mAlarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 1000, pi);
        } else {
            long intervalMillis = 1 * 30 * 1000;
            mAlarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 1000, intervalMillis, pi);
        }
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        mActivityCount++;
        if (mActivityCount == 1) {
            startService(new Intent(this, BatteryService.class));
        }
        Log.d("cgd", "mActivityCount = " + mActivityCount);
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        //应用从后台回来，则检测新版本
        if (mIsAppGotoBackground) {
            mIsAppGotoBackground = false;
        }
        mActivityResumeCount++;
    }

    @Override
    public void onActivityPaused(Activity activity) {
        mActivityResumeCount--;
        //这里延迟1秒钟后mActivityResumeCount还是0，就说明app已经退到后台
        mHandler.sendEmptyMessageDelayed(MSG_CHECK_APP_STATUS, 1000);
    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        mActivityCount--;
        Log.d("cgd", "mActivityCount = " + mActivityCount);
    }

    public boolean isBackground() {
        return mActivityResumeCount == 0;
    }
}
