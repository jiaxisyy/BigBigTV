package com.ubock.library.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.multidex.MultiDex;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.ubock.library.R;
import com.ubock.library.ui.activity.ExitAppActivity;
import com.ubock.library.ui.activity.MainActivity;
import com.ubock.library.ui.dialog.ToastExt;
import com.ubock.library.utils.LogUtils;
import com.ubock.library.utils.UiUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by ChenGD on 2017/11/05.
 */

public class BaseApp extends Application {

    private final List<Activity> mActivities = Collections.synchronizedList(new LinkedList<Activity>());
    private final HashMap<String, AppDelegate> mAppDelegates = new HashMap<>();

    private static BaseApp instance;
    private int mActivityResumeCount = 0;
    //检查状态消息
    private final static int MSG_CHECK_APP_STATUS = 0;

    @Override
    public void onCreate(){
        super.onCreate();
        instance = this;

        mRefWatcher = LeakCanary.install(this);

        managerActivity();

    }



    public String getToken(){
        return null;
    }

    protected Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            BaseApp.getInstance().handleMessage(msg);
        }
    };

    protected void handleMessage(Message msg){
        switch (msg.what){
            case MSG_CHECK_APP_STATUS:
                //这个消息是在onActivityPaused延迟1秒钟发送的，如果mActivityResumeCount还是0，就说明app已经退到后台
                //因为一般情况下界面切换是用不到1秒钟时间，如果超过1秒了，那说明界面有问题了，需要优化了
                //请注意：界面切换是先执行当前界面的onPause，才执行新界面的onResume的，
                //所以不在onResume里判断mActivityResumeCount == 0来判断应用是否在后台
                if (mActivityResumeCount == 0){
                    
                }
                break;
            default:
                break;
        }
    }

    private void managerActivity() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                addActivity(activity);
            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {
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
                delActivity(activity);
            }
        });
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

    public static BaseApp getContext() {
        return instance;
    }

    public static BaseApp getInstance() {
        return instance;
    }

    public BaseModel getGlobalModel(){
        return null;
    }

    private RefWatcher mRefWatcher;

    public static RefWatcher getRefWatcher(Context context) {
        BaseApp application = (BaseApp) context.getApplicationContext();
        return application.mRefWatcher;
    }


    private void addActivity(Activity act) {
        Activity a;
        for (int i = 0; i < mActivities.size(); i++) {
            a = mActivities.get(i);
            if (a == act) {
                return;
            }
        }
        mActivities.add(act);
        LogUtils.d(String.format("started %s , mActivities = %d", act.toString(), mActivities.size()));
    }


    private synchronized void delActivity(Activity act) {
        Iterator<Activity> iterator = mActivities.iterator();
        while (iterator.hasNext()) {
            if (act == iterator.next()) {
                iterator.remove();
                break;
            }
        }
        LogUtils.d(String.format("finished %s , mActivities = %d", act.toString(), mActivities.size()));
    }

    /**
     * 结束栈内的类名aClass的所有activity
     */
    public void delActivityByClass(Class<?> aClass) {
        Activity a;
        if (null != mActivities && mActivities.size() > 0) {
            for (int i = 0; i < mActivities.size(); i++) {
                a = mActivities.get(i);
                if (a != null && aClass.equals(a.getClass())) {
                    a.finish();
                }
            }
        }
    }

    /**
     * 清空activity
     */
    public synchronized void cleanActivities(boolean shipMainActivity) {
        Iterator<Activity> iterator = mActivities.iterator();
        Activity activity;
        while (iterator.hasNext()) {
            activity = iterator.next();
            if (shipMainActivity && (activity instanceof MainActivity)) {
                continue;
            }
            iterator.remove();
            if (activity != null) {
                activity.finish();
                activity = null;
            }
        }
    }

    private long time;

    /**
     * 退出应用
     *
     * @param forceExit 是否立即退出
     */
    public boolean exitApp(boolean forceExit) {
        boolean ret = false;
        if (forceExit) {
            ret = exit();
        } else {
            long secondTime = System.currentTimeMillis();
            LogUtils.d(String.format("preTime:%d, lastTime:%d, totalTime:%d", time, secondTime, secondTime - time));
            if (secondTime - time < BaseConfig.exitAppInterval) {
                ret = exit();
            } else {
                time = System.currentTimeMillis();
                ToastExt.showExt(UiUtils.getString(R.string.exit_app));
            }
        }
        return ret;
    }

    private boolean exit() {
        cleanActivities(false);
        Intent intent = new Intent(this, ExitAppActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        LogUtils.d(String.format("mActivities = %d", mActivities.size()));
        LogUtils.d("exit application");
//        System.exit(0);//这么写应用可能会重启，达不到退出应用的目的
//        android.os.Process.killProcess(android.os.Process.myPid());//这么写应用可能会重启，达不到退出应用的目的
        return true;
    }

}
