package com.ubock.library.ui.dialog;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ubock.library.base.BaseApp;
import com.ubock.library.R;
import com.ubock.library.base.BaseConfig;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * created by ChenGD
 * 可以设置显示位置、字体颜色、显示时间、宽高等
 */
public class ToastExt extends Toast {

	private static ToastExt mToast;
	private Field mField;
	private Object mObj;
	private Method mShowMethod, mHideMethod;
    /**
     * 字体颜色
     */
	private static int mColor = 0xffffffff;
    /**
     * 显示时长
     */
    private static int mTime = Toast.LENGTH_SHORT;
    /**
     * 显示位置
     */
    private static int mGravity = Gravity.CENTER;
    /**
     *宽高设置:Gravity.FILL_HORIZONTAL,Gravity.FILL_VERTICAL,Gravity.NO_GRAVITY
     */
    private static int mSize = Gravity.NO_GRAVITY;

    private static ToastExt getInstance(Context context){
        if (null == mToast) {
            synchronized (ToastExt.class) {
                if (null == mToast) {
                    mToast = new ToastExt(context.getApplicationContext());
                }
            }
        }
        return mToast;
    }

	private ToastExt(Context context) {
		super(context);
	}

    public ToastExt(Context context, String text) {
		this(context);
		TextView tv = new TextView(context);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		tv.setLayoutParams(lp);
		tv.setTextSize(context.getResources().getDimensionPixelSize(R.dimen._15dp));
		tv.setText(text);
		tv.setBackgroundResource(R.drawable.alpha_corners_bg);
		mToast = new ToastExt(context);
		mToast.setGravity(Gravity.CENTER, 0, 0);
		mToast.setView(tv);
		reflectionTN();
	}

    public static ToastExt makeText(Context context, String text) {
        ToastExt toast = makeText(context, text, mTime);
        return toast;
    }

	public static ToastExt makeText(Context context, String text, int time) {
        ToastExt toast = makeText(context, text, time, mColor);
		return toast;
	}
	
	public static ToastExt makeText(Context context, String text, int time, int color) {
        ToastExt toast = makeText(context, text, time, color, mGravity, mSize);
		return toast;
	}

    public static ToastExt makeText(Context context, String text, int time, int color, int gravity, int size) {
        ToastExt toast = getInstance(context);
        mTime = time>0?time:mTime;
        mColor = color>0?color: mColor;
        mGravity = gravity;
        mSize = size;
        toast.setGravity(gravity, 0, 0);
        toast.setDuration(time);
        TextView tv = new TextView(context);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        tv.setLayoutParams(lp);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen._15dp));
        tv.setText(text);
        tv.setTextColor(color);
        int left = context.getResources().getDimensionPixelOffset(R.dimen._30dp);
        int top = context.getResources().getDimensionPixelOffset(R.dimen._15dp);
        tv.setPadding(left, top, left, top);
        tv.setGravity(gravity|size);
        tv.setBackgroundResource(R.drawable.alpha_corners_bg);
        toast.setView(tv);
        toast.reflectionTN();
        mToast = toast;
        return toast;
    }

    /**
     * 系统默认风格
     * @param text
     */
	public static void show(String text){
		Toast.makeText(BaseApp.getInstance().getApplicationContext(), text, mTime).show();
	}

    /**
     * 居中显示
     * @param text
     */
	public static void showExt(String text){
        show(BaseApp.getInstance().getApplicationContext(), text);
	}

    /**
     * 居中显示调试信息,只有{@link BaseConfig#LOG_LEVEL}级别小于或等于{@link Log#DEBUG}时才显示
     * @param text
     */
    public static void showDebug(String text){
        if (BaseConfig.LOG_LEVEL <= Log.DEBUG) {
            show(BaseApp.getInstance().getApplicationContext(), text);
        }
    }

    /**
     * 居中显示
     * @param view
     */
    public static void showExt(View view){
        showExt(view, Gravity.CENTER);
    }

    /**
     * 指定显示位置
     * @param view
     * @param gravity
     */
    public static void showExt(View view, int gravity){
        ToastExt toast = getInstance(BaseApp.getInstance().getApplicationContext());
        toast.setGravity(gravity, 0, 0);
        toast.setView(view);
        toast.show();
    }

    public static void showExt(String text, Drawable left, Drawable top, Drawable right, Drawable bottom) {
        showExt(text, left, top, right, bottom, 0, Gravity.CENTER);
    }

    public static void showExt(String text, Drawable left, Drawable top, Drawable right, Drawable bottom, int drawablePadding) {
        showExt(text, left, top, right, bottom, drawablePadding, Gravity.CENTER);
    }

    public static void showExt(String text, Drawable left, Drawable top, Drawable right, Drawable bottom, int drawablePadding, int gravity){
        Context context = BaseApp.getInstance().getApplicationContext();
        ToastExt toast = getInstance(context);
        toast.setGravity(gravity, 0, 0);
        TextView tv = new TextView(context);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        tv.setLayoutParams(lp);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimensionPixelOffset(R.dimen._20sp));
        tv.setText(text);
        tv.setTextColor(0xffffffff);
        int leftPadding = context.getResources().getDimensionPixelOffset(R.dimen._30dp);
        int topPadding = context.getResources().getDimensionPixelOffset(R.dimen._15dp);
        tv.setPadding(leftPadding, topPadding, leftPadding, topPadding);
        tv.setGravity(Gravity.CENTER| Gravity.NO_GRAVITY);
        tv.setBackgroundResource(R.drawable.alpha_corners_bg);
        tv.setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom);
        tv.setCompoundDrawablePadding(drawablePadding);
        toast.setView(tv);
        toast.show();
    }

    public static void show(Context context, String text){
        show(context, text, mTime);
    }

    public static void show(Context context, String text, int time){
        show(context, text, time, mColor);
    }

    public static void show(Context context, String text, int time, int color){
        show(context, text, time, color, mGravity);
    }

    public static void show(Context context, String text, int time, int color, int gravity){
        show(context, text, time, color, gravity, mSize);
    }

    public static void show(Context context, String text, int time, int color, int gravity, int size){
        makeText(context, text, time, color, gravity, size).show();
    }

	private void reflectionTN() {
		try {
			mField = mToast.getClass().getSuperclass().getDeclaredField("mTN");
			mField.setAccessible(true);
			mObj = mField.get(mToast);
			mShowMethod = mObj.getClass().getDeclaredMethod("show");
			mHideMethod = mObj.getClass().getDeclaredMethod("hide");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    /**
     * 一直显示，需要调用hide方法关闭
     */
	public void showAlways() {
		try {
			mShowMethod.invoke(mObj);// 调用TN对象的show()方法，显示toast
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void hide() {
		try {
			mHideMethod.invoke(mObj);// 调用TN对象的hide()方法，关闭toast
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
