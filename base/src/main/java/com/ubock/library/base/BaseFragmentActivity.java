package com.ubock.library.base;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.leakcanary.RefWatcher;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.ubock.library.R;
import com.ubock.library.annotation.Presenter;
import com.ubock.library.common.BaseHandler;
import com.ubock.library.subscriptionlifecycle.ActivitySubscriptionManager;
import com.ubock.library.subscriptionlifecycle.SubscriptionHelper;
import com.ubock.library.ui.dialog.Loading;
import com.ubock.library.ui.dialog.ToastExt;
import com.ubock.library.utils.LogUtils;
import com.ubock.library.utils.PresenterUtils;
import com.ubock.library.utils.StatusBarUtil;
import com.ubock.library.widgets.AutoRadioGroup;
import com.zhy.autolayout.AutoFrameLayout;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import rx.Subscription;

/**
 * Created by ChenGD on 2017/4/18.
 */
public abstract class BaseFragmentActivity<P extends BasePresenter> extends RxAppCompatActivity
        implements BaseView, ActivitySubscriptionManager {

    private TextView mStatusbarBg;
    private TextView mTitle;
    private TextView mTopRightMenuText;
    private ImageView mBack;
    protected ImageButton mTopRightMenu;

    protected final String TAG = this.getClass().getSimpleName();
    private P mPresenter;
    private Loading mLoading;
    private SubscriptionHelper mSubscriptionHelper = new SubscriptionHelper();
    private PresenterUtils mPresenterUtils;

    private static final String LAYOUT_LINEARLAYOUT = "LinearLayout";
    private static final String LAYOUT_FRAMELAYOUT = "FrameLayout";
    private static final String LAYOUT_RELATIVELAYOUT = "RelativeLayout";
    private static final String WIDGET_RADIOGROUP = "RadioGroup";

    /**
     * 用这个 公共的handler 消息
     * <p>
     * fragment的通用handler  和 activity基类的handler不是一个
     * (所以act和ft handler 共用一个消息号没问题，但应该尽量避免这种情况)
     */
    protected BaseHandler mBaseHandler = new BaseHandler(this);

    public BaseHandler getBaseHandler() {
        return mBaseHandler;
    }

    public void handleMessage(android.os.Message msg) {
    }

    public final void sendMessage(android.os.Message msg) {
        if (mBaseHandler != null) {
            mBaseHandler.sendMessage(msg);
        }
    }

    public final void sendMessageDelayed(Message msg, long delayMillis) {
        if (mBaseHandler != null) {
            mBaseHandler.sendMessageDelayed(msg, delayMillis);
        }
    }

    public final void sendEmptyMessage(int what) {
        if (mBaseHandler != null) {
            mBaseHandler.sendEmptyMessage(what);
        }
    }

    public final void sendEmptyMessageDelayed(int what, long delayMillis) {
        if (mBaseHandler != null) {
            mBaseHandler.sendEmptyMessageDelayed(what, delayMillis);
        }
    }

    private void initPresenter() {
        mPresenterUtils = new PresenterUtils(this, this);
        mPresenter = mPresenterUtils.getPresenter(0);
        P p = (P) mPresenterUtils.createPresenterByGenericSuperclass(this.getClass(), this);
        if (p != null && p != mPresenter) {
            mPresenter = p;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//        }
        initPresenter();
        super.onCreate(savedInstanceState);
        mViews = new SparseArray<>();
//        initStatusBar();
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        if (useDatabinding()) {
            ViewDataBinding binding = DataBindingUtil.setContentView(this, getLayoutId());
            initBinding(binding, savedInstanceState);
        } else {
            setContentView(getLayoutId());
        }
        mTitle = findViewById(R.id.toolbar_title);
        mBack = findViewById(R.id.top_back);
        mTopRightMenu = findViewById(R.id.btn_top_right_menu);
        mTopRightMenuText = findViewById(R.id.tv_top_right_menu);
        initView(savedInstanceState);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (mPresenter != null) {
            mPresenter.onPostCreate(savedInstanceState);
        }
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        View view = null;
        if (name.equals(LAYOUT_FRAMELAYOUT)) {
            view = new AutoFrameLayout(context, attrs);
        }

        if (name.equals(LAYOUT_LINEARLAYOUT)) {
            view = new AutoLinearLayout(context, attrs);
        }

        if (name.equals(LAYOUT_RELATIVELAYOUT)) {
            view = new AutoRelativeLayout(context, attrs);
        }
        if (name.equals(WIDGET_RADIOGROUP)) {
            view = new AutoRadioGroup(context, attrs);
        }

        if (view != null)
            return view;

        return super.onCreateView(name, context, attrs);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 使用databinding时要覆盖此方法,并且返回true,并且覆盖initBinding方法
     *
     * @return
     */
    protected boolean useDatabinding() {
        return false;
    }

    protected void initBinding(ViewDataBinding binding, Bundle savedInstanceState) {
    }

    protected abstract int getLayoutId();

    protected abstract void initView(Bundle savedInstanceState);

    public P getPresenter() {
        return mPresenter;
    }

    public <T extends BasePresenter> T getPresenter(Class<T> clazz) {
        return mPresenterUtils.getPresenter(clazz);
    }

    /**
     * 根据类注解中的索引来获取presenter
     * <br/>
     * 注意：此方法只能获取@{@link Presenter}注解中的presenter
     *
     * @param index 索引从0开始
     * @param <T>   返回的presenter类型
     * @return
     */
    public <T extends BasePresenter> T getPresenter(int index) {
        return mPresenterUtils.getPresenter(index);
    }

    protected <T extends View> T findView(int id) {
        return (T) findViewById(id);
    }

    @Override
    public void showLoading() {
        showLoading(null);
    }

    public void showLoading(int message) {
        showLoading(getResources().getString(message));
    }

    @Override
    public void showLoading(String message) {
        if (mLoading == null) {
            mLoading = new Loading(this);
        }
        mLoading.show();
        mLoading.setMessage(message);
    }

    @Override
    public void hideLoading() {
        if (mLoading != null && mLoading.isShowing()) {
            mLoading.dismiss();
        }
    }

    public void back(View view) {
        finish();
    }

    public void onClickTopRightMenu(View view) {

    }

    public void onClickTopRightMenuText(View view) {

    }

    public void setTitle(String title) {
        if (mTitle != null) {
            mTitle.setText(title);
        }
    }


    /**
     * 显示返回按钮
     *
     * @param show
     */
    public void showBackMenu(boolean show) {
        if (mBack != null) {
            mBack.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * 显示并设置文字
     *
     * @param s
     */
    public void setTopText(String s) {
        if (mTopRightMenuText != null) {
            mTopRightMenuText.setText(s);
            mTopRightMenuText.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 显示右上角菜单按钮
     *
     * @param show
     */
    public void showTopRightMenu(boolean show) {
        if (mTopRightMenu != null) {
            mTopRightMenu.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void showMessage(String message) {
        ToastExt.showExt(message);
    }

    @Override
    @CallSuper
    protected void onStart() {
        super.onStart();
        if (mPresenter != null) {
            mPresenter.onStart();
        }
        mSubscriptionHelper.unSubscribe(ActivitySubscriptionManager.onStart);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mSubscriptionHelper.unSubscribe(ActivitySubscriptionManager.onRestart);
    }

    @Override
    @CallSuper
    protected void onResume() {
        super.onResume();
        if (mPresenter != null) {
            mPresenter.onResume();
        }
        mSubscriptionHelper.unSubscribe(ActivitySubscriptionManager.onResume);
    }

    @Override
    protected void onPause() {
        closeKeyboard();
        super.onPause();
        mSubscriptionHelper.unSubscribe(ActivitySubscriptionManager.onPause);
        if (mPresenter != null) {
            mPresenter.onPause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mSubscriptionHelper.unSubscribe(ActivitySubscriptionManager.onStop);
        if (mPresenter != null) {
            mPresenter.onStop();
        }
    }

    @Override
    protected void onDestroy() {
        mSubscriptionHelper.unSubscribe(ActivitySubscriptionManager.onDestroy);
        if (mPresenter != null) {
            mPresenter.onDestroy();//释放资源
            mPresenter = null;
        }
        if (mLoading != null && mLoading.isShowing()) {
            mLoading.dismiss();
            mLoading = null;
        }
        if (mBaseHandler != null) {
            mBaseHandler.detach();
            mBaseHandler = null;
        }
        if (mPresenterUtils != null) {
            mPresenterUtils.release();
            mPresenterUtils = null;
        }
        super.onDestroy();
        RefWatcher refWatcher = BaseApp.getInstance().getRefWatcher(this);
        refWatcher.watch(this);
    }

    @Override
    final public void subscribe(Subscription subscription) {
        mSubscriptionHelper.subscribe(subscription);
    }

    @Override
    final public void subscribe(Subscription subscription, int lifeCycle) {
        mSubscriptionHelper.subscribe(subscription, lifeCycle);
    }

    @Override
    final public void unSubscribe(Subscription subscription) {
        mSubscriptionHelper.unSubscribe(subscription);
    }

    //关闭键盘
    private void closeKeyboard() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    protected int getStatusBarResource() {
        return R.color.colorPrimaryDark;
    }

    public void setStatusBarStyle(int resId) {
        if (mStatusbarBg != null) {
            mStatusbarBg.setBackgroundResource(resId);
        }
    }

    protected void initStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            ViewGroup rootView = (ViewGroup) window.getDecorView();
            ViewGroup firstView = (ViewGroup) rootView.getChildAt(0);
            if (firstView == null) {
                return;
            }
            int height = StatusBarUtil.getStatusBarHeight(this);
            LogUtils.d("StatusBar Height = " + height);
            if (height <= 0) {
                return;
            }
            mStatusbarBg = new TextView(this);
            mStatusbarBg.setBackgroundResource(getStatusBarResource());
            mStatusbarBg.setId(R.id.statusbar_id);
            ViewGroup.LayoutParams lp = null;
            if (firstView instanceof LinearLayout) {
                lp = new LinearLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, height);
            } else if (firstView instanceof RelativeLayout) {
                lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, height);
            } else if (firstView instanceof FrameLayout) {
                lp = new FrameLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, height);
            }
            if (lp != null) {
                translucentStatusBar(this);
                firstView.addView(mStatusbarBg, 0, lp);
            }
        }
    }

    protected void translucentStatusBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.setStatusBarColor(Color.TRANSPARENT);
//                window.setNavigationBarColor(Color.TRANSPARENT);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            } else {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            }
        }
    }

    /**
     * 通过ID找到view
     *
     * @param viewId
     * @param <E>
     * @return
     */
    public <E extends View> E getView(int viewId) {
        E view = (E) mViews.get(viewId);
        if (view == null) {
            view = findViewById(viewId);
            mViews.put(viewId, view);
        }
        return view;
    }

    private SparseArray<View> mViews;

    protected Dialog getLoading() {
        return mLoading;
    }

    /**
     * 点击输入框外面时关闭输入法
     */
    private void hideSoftInputWhenTouchOutside(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View focusView = getCurrentFocus();
            if (focusView != null) {
                int[] screen = new int[2];
                int[] screen2 = new int[2];
                focusView.getLocationOnScreen(screen);
                getWindow().getDecorView().getLocationOnScreen(screen2);//当有弹出键盘时屏幕上移，event.getY()会增加
                Rect rect = new Rect(screen[0], screen[1], screen[0] + focusView.getMeasuredWidth(), screen[1] + focusView.getMeasuredHeight());
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (!rect.contains((int) event.getX(), (int) event.getY() + screen2[1]) && focusView.getWindowToken() != null) {
                    imm.hideSoftInputFromWindow(focusView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        hideSoftInputWhenTouchOutside(event);
        return super.dispatchTouchEvent(event);
    }
}
