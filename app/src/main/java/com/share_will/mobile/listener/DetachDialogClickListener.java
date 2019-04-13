package com.share_will.mobile.listener;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.view.ViewTreeObserver;

/**
 * Created by ChenGD on 2018/8/11.
 * 解决弹出框内存泄漏问题
 * @author chenguandu
 */

public class DetachDialogClickListener implements DialogInterface.OnClickListener {
    public static DetachDialogClickListener wrap(DialogInterface.OnClickListener delegate) {
        return new DetachDialogClickListener(delegate);
    }

    private DialogInterface.OnClickListener mDelegate;

    private DetachDialogClickListener(DialogInterface.OnClickListener delegate) {
        this.mDelegate = delegate;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (mDelegate != null) {
            mDelegate.onClick(dialog, which);
        }
    }

    public void release(){
        mDelegate = null;
    }

    public void clearOnDetach(Dialog dialog) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            dialog.getWindow()
                    .getDecorView()
                    .getViewTreeObserver()
                    .addOnWindowAttachListener(new ViewTreeObserver.OnWindowAttachListener() {
                        @Override
                        public void onWindowAttached() {
                        }

                        @Override
                        public void onWindowDetached() {
                            release();
                        }
                    });
        }
    }

}
