package com.share_will.mobile.services;

import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

import com.share_will.mobile.App;
import com.share_will.mobile.MessageEvent;
import com.share_will.mobile.R;
import com.share_will.mobile.presenter.BatteryServicePresenter;
import com.share_will.mobile.ui.views.BatteryServiceView;
import com.ubock.library.base.BaseEntity;
import com.ubock.library.base.BaseService;
import com.ubock.library.utils.SharedPreferencesUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

/**
 * Created by ChenGD on 2018/3/6.
 *
 * @author chenguandu
 */

public class BatteryService extends BaseService<BatteryServicePresenter> implements BatteryServiceView {
    private SoundPool mSoundPool;
    private int mSoundID;
    private int mStreamID;
    /**
     * 电池电量不足时提醒值，百分比
     */
    private final int mBatteryLowWarm = 20;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("cgd", "Battery Service onCreate");
        initSound();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("cgd", "Battery Service onDestroy");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        getBatteryInfo();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onLoadBattery(BaseEntity<Map<String, String>> ret) {
        if (ret != null && ret.getCode() == 0) {
            Map<String, String> data = ret.getData();
            if (data != null) {
                int num = 0;
                if (!TextUtils.isEmpty(data.get("current"))) {
                    num = Integer.parseInt(data.get("current"));
                }
                int use = 0;
                if (!TextUtils.isEmpty(data.get("current1"))) {
                    use = Integer.parseInt(data.get("current1"));
                }
                int sop = -1;
                if (!TextUtils.isEmpty(data.get("sop"))) {
                    sop = Integer.parseInt(data.get("sop"));
                }
                boolean online = true;
                if (!TextUtils.isEmpty(data.get("online"))) {
                    online = Boolean.parseBoolean(data.get("online"));
                }
                long time = 0;
                if (!TextUtils.isEmpty(data.get("time"))) {
                    time = Long.parseLong(data.get("time"));
                }
                if (!online) {
                    long l = time;
                    long min = l / (1000 * 60);
                    String oldSop = String.valueOf(sop);
                    float minPP = 100 / 120f;//跑1分钟消耗电量百分比
                    float consume = min * minPP;
                    float v = Float.parseFloat(oldSop) - consume;
                    sop = (int) v;
                }
                if (sop > 100) {
                    sop = 100;
                }
                if (sop > mBatteryLowWarm) {
                    SharedPreferencesUtils.setIntergerSF(BatteryService.this, "warm_level", mBatteryLowWarm);
                }
                int lowWarm = SharedPreferencesUtils.getIntergerSF(BatteryService.this, "warm_level");

                String sn = data.get("sn");
                Log.d("cgd", String.format("剩余能量:%d, SN:%s", num, sn));
                Log.d("cgd", String.format("在线状态:%s",online));
                EventBus.getDefault().postSticky(new MessageEvent.BatteryInfo(num, sn, use, sop, online, time));
                if (sop >= 0) {
                    if (sop < lowWarm) {
                        if (lowWarm <= 5) {//<5%报警一次
                            lowWarm = 0;
                        } else if (lowWarm <= 10) {//<10%报警一次
                            lowWarm = 5;
                        } else {//<20%报警一次
                            lowWarm = 10;
                        }
                        SharedPreferencesUtils.setIntergerSF(BatteryService.this, "warm_level", lowWarm);
                        playSound();
                    }
                }
            } else {
                Log.d("cgd", "获取电量信息失败！");
                EventBus.getDefault().postSticky(new MessageEvent.BatteryInfo(0, null, 0, -2, false, 0));
            }
        } else {
            Log.d("cgd", "获取电量信息失败！");
            EventBus.getDefault().postSticky(new MessageEvent.BatteryInfo(0, null, 0, -2, false, 0));
        }
    }

    private void getBatteryInfo() {
        //未登录
        if (TextUtils.isEmpty(App.getInstance().getToken())) {
            return;
        }
        Log.d("cgd", "getBatteryInfo...");
        getPresenter().getBattery(App.getInstance().getUserId());
    }

    private void initSound() {
        mSoundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        mSoundID = mSoundPool.load(getApplicationContext(), R.raw.bettery_low, 1);
        mSoundPool.setOnLoadCompleteListener(new LoadListener(this));
    }

    private void playSound() {
        if (mSoundPool == null || mSoundID == 0) {
            initSound();
        }
        mStreamID = mSoundPool.play(mSoundID, 1f, 1f, 10, 1, 1f);
    }

    static class LoadListener implements SoundPool.OnLoadCompleteListener {
        BatteryService batteryService;

        public LoadListener(BatteryService service) {
            batteryService = service;
        }

        @Override
        public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
            if (status == 0) {
                if (batteryService.mStreamID != 0) {
                    batteryService.playSound();
                }
            }
        }
    }
}
