package com.ubock.library.base;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.ubock.library.R;


public class BaseDialog extends Dialog {
    final String TAG = this.getClass().getSimpleName();
    int mThemeId = R.style.DialogBaseTheme;
    private static BaseDialog mDialog;
    private Context mContext;

    public BaseDialog(@NonNull Context context) {
        super(context);
        mContext = context;
        setCanceledOnTouchOutside(false);
        closeInputMethod();
    }

    public BaseDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        mContext = context;
        setCanceledOnTouchOutside(false);
        closeInputMethod();
    }

    protected BaseDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        mContext = context;
        setCanceledOnTouchOutside(false);
        closeInputMethod();
    }

    public Activity getActivity() {
        if (mContext instanceof Activity) {
            return (Activity) mContext;
        } else {
            return getOwnerActivity();
        }
    }

    /**
     * 默认不弹出输入法
     */
    private void closeInputMethod(){
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
    }

    @Override
    public void setContentView(@NonNull View view) {
        super.setContentView(view);
    }

    @Override
    public void setContentView(@NonNull View view, @Nullable ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
    }

    @Override
    public void addContentView(@NonNull View view, @Nullable ViewGroup.LayoutParams params) {
        super.addContentView(view, params);
    }

    private static BaseDialog getInstance(Context context, int resId, int theme) {
        if (null == mDialog) {
            synchronized (BaseDialog.class) {
                if (null == mDialog) {
                    mDialog = new BaseDialog(context, theme);
                }
            }
        }
        if (resId > 0) {
            mDialog.setContentView(resId);
        }
        return mDialog;
    }

    public static BaseDialog create(Context context, int resId) {
        return BaseDialog.create(context, resId, R.style.DialogBaseTheme);
    }

    public static BaseDialog create(Context context, int resId, int theme) {
        return BaseDialog.getInstance(context, resId, theme);
    }

    @Override
    public void show() {
        super.show();
    }



    @Override
    public void onDetachedFromWindow() {
        release();
        super.onDetachedFromWindow();
    }

    protected void release() {
        if (mDialog != null) {
            if (mDialog.isShowing()){
                mDialog.dismiss();
            }
            mDialog = null;
        }
        mContext = null;
    }
}
