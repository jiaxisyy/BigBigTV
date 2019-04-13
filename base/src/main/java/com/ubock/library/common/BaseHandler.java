package com.ubock.library.common;

import android.os.Handler;
import android.os.Message;

import com.ubock.library.base.BaseFragmentActivity;

/**
 * Created by Chenguandu on 2018/8/10.
 */
public class BaseHandler extends Handler {
    private BaseFragmentActivity mActivity;
    public BaseHandler(BaseFragmentActivity activity){
        mActivity = activity;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        if (mActivity != null && !mActivity.isFinishing()){
            mActivity.handleMessage(msg);
        }
    }

    public void detach(){
        removeCallbacksAndMessages(null);
        mActivity = null;
    }
}
