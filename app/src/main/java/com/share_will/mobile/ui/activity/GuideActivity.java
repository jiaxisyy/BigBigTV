package com.share_will.mobile.ui.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;

import com.share_will.mobile.Constant;
import com.share_will.mobile.R;
import com.share_will.mobile.ui.adapter.GlideImageGuideLoader;
import com.ubock.library.base.BaseFragmentActivity;
import com.ubock.library.utils.SharedPreferencesUtils;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.List;

public class GuideActivity extends BaseFragmentActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_guide;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        Banner banner = findViewById(R.id.bannerGuide);
        View start = findViewById(R.id.iv_guide_start);
        start.setOnClickListener(view -> finish());
        List<Integer> guides = new ArrayList<>();
        guides.add(R.drawable.guide1);
        guides.add(R.drawable.guide2);
        guides.add(R.drawable.guide3);
        banner.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 2) {
                    start.setVisibility(View.VISIBLE);
                } else {
                    start.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        banner.setImages(guides).setImageLoader(new GlideImageGuideLoader());
        banner.isAutoPlay(false);
        banner.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferencesUtils.setBooleanSF(this, Constant.USER_ISFIRST, false);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            return true;
        } else {
            return super.dispatchKeyEvent(event);
        }
    }

}
