package com.ubock.library.base;


import android.content.Context;
import android.support.v4.widget.PopupWindowCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.ubock.library.R;
import com.ubock.library.utils.UiUtils;

/**
 * Created by ChenGD on 2017-5-5.
 */

public abstract class BasePopupWindow extends PopupWindow implements PopupWindow.OnDismissListener {

    public interface OnShowListener {
        void onShow(BasePopupWindow popupWindow, View anchor, boolean show);
    }

    private OnShowListener mOnShowListener;
    private OnDismissListener mOnPopupWindowDismissListener;
    View mAnchor;

    public void setOnShowListener(OnShowListener listener) {
        this.mOnShowListener = listener;
    }

    @Override
    public void setOnDismissListener(OnDismissListener onDismissListener) {
        mOnPopupWindowDismissListener = onDismissListener;
    }

    public BasePopupWindow(final View contentView, int width, int height, boolean focusable) {
        super(contentView, width, height, focusable);
        super.setOnDismissListener(this);
        setOutsideTouchable(true);
//        setSplitTouchEnabled(true);
        setBackgroundDrawable(UiUtils.getResources().getDrawable(R.drawable.popwindow_bg));
    }

    public BasePopupWindow(final View contentView, Context context) {
        super(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        super.setOnDismissListener(this);
        setOutsideTouchable(true);
//        setSplitTouchEnabled(true);
        setBackgroundDrawable(UiUtils.getResources().getDrawable(R.drawable.popwindow_bg));
    }

    protected void onShow(boolean show){}

    public void showBelow(View anchor, int xoff, int yoff, int gravity) {
        mAnchor = anchor;
        if (isShowing()){
            onShow(false);
            if (mOnShowListener != null) {
                mOnShowListener.onShow(this, mAnchor, false);
            }
            dismiss();
            return;
        }
        PopupWindowCompat.showAsDropDown(this, anchor, xoff, yoff, gravity);
        onShow(this.isShowing());
        if (mOnShowListener != null) {
            mOnShowListener.onShow(this, mAnchor, this.isShowing());
        }
    }

    @Override
    public void onDismiss() {
        onShow(false);
        if (mOnPopupWindowDismissListener != null) {
            mOnPopupWindowDismissListener.onDismiss();
        }
        if (mOnShowListener != null) {
            mOnShowListener.onShow(this, mAnchor, false);
        }
    }


}
