package com.ubock.library.ui.dialog;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.TextView;

import com.ubock.library.R;
import com.ubock.library.base.BaseDialog;
import com.ubock.library.utils.UiUtils;

/**
 * Created by ChenGD on 2017/4/19.
 */

public class Loading extends BaseDialog {
    static Loading mLoading;
    protected TextView mMessage;
    private String mMsg;
    private Context mContext;

    public Loading(@NonNull Context context) {
        this(context, UiUtils.getString(R.string.loading));
    }

    public Loading(@NonNull Context context, String message) {
        this(context, R.style.TipDialogTheme);
        setContentView(R.layout.loading);
        mMessage = findViewById(R.id.tv_text);
        mContext = context;
        mMsg = TextUtils.isEmpty(message) ? "" : message;
        setCanceledOnTouchOutside(false);
        setMessage(mMsg);
    }

    protected Loading(@NonNull Context context, int theme) {
        super(context, theme);
    }

    public static Loading getInstance(Context context) {
        if (null == mLoading) {
            synchronized (Loading.class) {
                if (null == mLoading) {
                    mLoading = new Loading(context);
                }
            }
        }
        return mLoading;
    }

    public static Loading create(Context context) {
        return Loading.create(context, R.string.loading);
    }

    public static Loading create(Context context, String message) {
        Loading loading = getInstance(context);
        if (loading.getOwnerActivity()==null || loading.getOwnerActivity().isFinishing()){
            loading.dismiss();
            loading.release();
            loading = getInstance(context);
        }
        loading.mMsg = TextUtils.isEmpty(message) ? "" : message;
        loading.setMessage(loading.mMsg);
        return loading;
    }

    public static Loading create(Context context, int message) {
        return Loading.create(context, UiUtils.getString(message));
    }

    public void setMessage(String msg) {
        if (msg == null) {
            return;
        }
        mMessage.setText(msg);
    }

    public void setMessage(int msg) {
        mMessage.setText(msg);
    }

    @Override
    public boolean dispatchKeyEvent(@NonNull KeyEvent event) {
        if (mContext instanceof Activity){
            return ((Activity) mContext).dispatchKeyEvent(event);
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    protected void release() {
        Log.d("Loading","Loading release");
        if (mLoading != null) {
            if (mLoading.isShowing()){
                mLoading.dismiss();
            }
            mLoading = null;
        }
        mContext = null;
    }
}

