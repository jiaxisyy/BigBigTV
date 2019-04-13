package com.share_will.mobile.utils;

import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.share_will.mobile.ui.activity.DebugActivity;

/**
 * Created by ChenGD on 2018/4/17.
 * 件处理类
 */
public class PressUtils {
    private int[] queue = { KeyEvent.KEYCODE_VOLUME_UP,
                            KeyEvent.KEYCODE_VOLUME_UP,
                            KeyEvent.KEYCODE_VOLUME_DOWN,
                            KeyEvent.KEYCODE_VOLUME_DOWN,
                            KeyEvent.KEYCODE_VOLUME_UP,
                            KeyEvent.KEYCODE_VOLUME_DOWN};
    private int queueLen = queue.length;
    private int pop = 0;
    private long startTime = 0;
    private long intervalTime = 3000;
    private Context mContext;

    public PressUtils(Context context){
        mContext = context;
    }

    //事件处理
    public void press(KeyEvent event){
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            press(event.getKeyCode());
        }
    }

    private void press(int key) {
        if (key == queue[pop]) {
            if (pop == queueLen - 1) {
                pop = 0;
                startTime = System.currentTimeMillis();
                // 结束时间
                if (System.currentTimeMillis() - startTime < intervalTime) {
                    Intent intent = new Intent(mContext, DebugActivity.class);
                    mContext.startActivity(intent);
                }
            } else {
                pop++;
            }
        } else {
            pop = 0;
            startTime = System.currentTimeMillis();
            if (key == queue[pop]) {
                pop++;
            }
        }
    }
}
