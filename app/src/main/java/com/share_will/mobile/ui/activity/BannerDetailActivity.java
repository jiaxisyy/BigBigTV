package com.share_will.mobile.ui.activity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.share_will.mobile.R;
import com.ubock.library.base.BaseFragmentActivity;

public class BannerDetailActivity extends BaseFragmentActivity {

    private WebView mWebView;

    @Override
    protected int getLayoutId() {
        return R.layout.banner_detail_activity;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setTitle("关闭");
        mWebView = findViewById(R.id.webView_banner_detail);

        WebSettings webSettings = mWebView.getSettings();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        // 为图片添加放大缩小功能
        webSettings.setUseWideViewPort(true);
        webSettings.setBlockNetworkImage(false);
        //设置缓存模式
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        String banner_detail_url = getIntent().getStringExtra("banner_detail_url");
        mWebView.loadUrl(banner_detail_url);
    }

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
