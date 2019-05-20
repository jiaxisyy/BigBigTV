package com.share_will.mobile.ui.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.share_will.mobile.R;
import com.share_will.mobile.ui.adapter.GlideImageGuideLoader;
import com.ubock.library.base.BaseFragmentActivity;
import com.ubock.library.utils.LogUtils;
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

        List<Integer> guides = new ArrayList<>();
        guides.add(R.drawable.guide1);
        guides.add(R.drawable.guide2);
        guides.add(R.drawable.guide3);

        banner.setImages(guides).setImageLoader(new GlideImageGuideLoader());
        banner.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                LogUtils.d("onPageScrolled->"+position);
            }

            @Override
            public void onPageSelected(int position) {
                LogUtils.d("onPageSelected->"+position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        banner.isAutoPlay(false);
        banner.start();
    }
}
