package com.share_will.mobile.listener;

import android.view.View;

/**
 * Created by ChenGD on 2018/8/11.
 * 解决Onclick内存泄漏问题
 * @author chenguandu
 */

public class DetachViewClickListener implements View.OnClickListener {
    public static DetachViewClickListener wrap(View.OnClickListener delegate) {
        return new DetachViewClickListener(delegate);
    }

    private View.OnClickListener mDelegate;

    private DetachViewClickListener(View.OnClickListener delegate) {
        this.mDelegate = delegate;
    }

    @Override
    public void onClick(View v) {
        if (mDelegate != null) {
            mDelegate.onClick(v);
        }
    }

    public void release(){
        mDelegate = null;
    }

}
