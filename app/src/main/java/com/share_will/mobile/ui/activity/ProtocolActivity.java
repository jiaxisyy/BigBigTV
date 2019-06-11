package com.share_will.mobile.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;

import com.share_will.mobile.Constant;
import com.share_will.mobile.R;
import com.ubock.library.base.BaseConfig;
import com.ubock.library.base.BaseFragmentActivity;
import com.ubock.library.utils.LogUtils;
import com.ubock.library.utils.SharedPreferencesUtils;

public class ProtocolActivity extends BaseFragmentActivity {

    private WebView mWebView;
    private View mTopBar;
    private Button mAccept;
    private boolean mShowTopBar = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_protocol;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setTitle("用户协议");
        mTopBar = findViewById(R.id.view_topbar);
        mAccept = findViewById(R.id.btn_accept);
        if (mShowTopBar = getIntent().getBooleanExtra("showTopBar", true)){
            mTopBar.setVisibility(View.VISIBLE);
            mAccept.setVisibility(View.GONE);
        }
        mWebView = findViewById(R.id.wv_protocol);
        WebSettings settings = mWebView.getSettings();
//        // 设置WebView支持JavaScript
        settings.setJavaScriptEnabled(true);
//        //支持自动适配
//        settings.setUseWideViewPort(true);
//        settings.setLoadWithOverviewMode(true);
//        http://services.smart-moss.com/static/protocol/protocol.html
//        mWebView.loadUrl(BaseConfig.SERVER_HOST + "protocol/protocol.html?hideAcceptButton=1");
        mWebView.loadUrl(BaseConfig.SERVER_HOST + "static/protocol/protocol.html");
        LogUtils.d(BaseConfig.SERVER_HOST + "static/protocol/protocol.html");
    }

    public void startAnt(View view){
        SharedPreferencesUtils.setBooleanSF(this, Constant.KEY_USER_PROTOCOL_READ, true);
        finish();
    }

//    @Override
//    public void onBackPressed() {
//        if (mShowTopBar){
//            super.onBackPressed();
//        }
//    }

    @Override
    protected void onDestroy() {
        destroyWebView();
        super.onDestroy();
    }

    private void destroyWebView() {
        if (mWebView != null) {
            ViewParent parent = mWebView.getParent();
            if (parent != null) {
                ((ViewGroup) parent).removeView(mWebView);
            }

            mWebView.stopLoading();
            // 退出时调用此方法，移除绑定的服务，否则某些特定系统会报错
            mWebView.getSettings().setJavaScriptEnabled(false);
            mWebView.clearHistory();
            mWebView.clearView();
            mWebView.removeAllViews();

            try {
                mWebView.destroy();
            } catch (Throwable ex) {

            }
        }
    }
}
