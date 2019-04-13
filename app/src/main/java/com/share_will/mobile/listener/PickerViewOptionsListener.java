package com.share_will.mobile.listener;

import android.view.View;

import com.bigkoo.pickerview.listener.OnOptionsSelectChangeListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;

/**
 * Created by ChenGD on 2018/8/11.
 *
 * @author chenguandu
 */

public class PickerViewOptionsListener {
    public static OptionsSelectListener select(OnOptionsSelectListener delegate) {
        return new OptionsSelectListener(delegate);
    }

    public static OptionsSelectChangeListener selectChange(OnOptionsSelectChangeListener delegate) {
        return new OptionsSelectChangeListener(delegate);
    }

    public static class OptionsSelectListener implements OnOptionsSelectListener {
        private OnOptionsSelectListener mDelegate;

        public OptionsSelectListener(OnOptionsSelectListener delegate) {
            this.mDelegate = delegate;
        }

        public void release() {
            mDelegate = null;
        }

        @Override
        public void onOptionsSelect(int options1, int options2, int options3, View v) {
            if (mDelegate != null) {
                mDelegate.onOptionsSelect(options1, options2, options3, v);
            }
        }
    }

    public static class OptionsSelectChangeListener implements OnOptionsSelectChangeListener {
        private OnOptionsSelectChangeListener mDelegate;

        public OptionsSelectChangeListener(OnOptionsSelectChangeListener delegate) {
            this.mDelegate = delegate;
        }

        public void release() {
            mDelegate = null;
        }

        @Override
        public void onOptionsSelectChanged(int options1, int options2, int options3) {
            if (mDelegate != null) {
                mDelegate.onOptionsSelectChanged(options1, options2, options3);
            }
        }
    }
}
