package com.ubock.library.base;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.ubock.library.R;
import com.ubock.library.ui.adapter.BaseTabFragmentAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ChenGD on 2017/4/18.
 * 通用的tablayout和viewPager组合
 */

public abstract class BaseTabContainerFragment extends BaseFragment implements
        TabLayout.OnTabSelectedListener, ViewPager.OnPageChangeListener {
    protected TabLayout mTabContainer;
    protected ViewPager mViewPager;
    private List<String> mTitles;
    private List<BaseFragment> mFragments;
    protected int pos;
    protected BaseTabFragmentAdapter mFragmentAdapter;

    @Override
    protected int getLayoutId() {
        return getLayout();
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        mTabContainer = view.findViewById(R.id.base_tab_container);
        mViewPager = view.findViewById(R.id.base_view_pager);
        if (null != mTabContainer) mTabContainer.addOnTabSelectedListener(this);
        mViewPager.addOnPageChangeListener(this);
        mFragments = new ArrayList<>();
        mTitles = new ArrayList<>();
        initWidget();
    }

    /**
     * 定制title 重写该方法
     */
    private void setTitles(TabLayout tabLayout) {
        if (null != tabLayout) {
            if (mTitles.size() > 0) {
                mTitles.clear();
            }
            mTitles.addAll(getTitles());
            int length = mTitles.size();
            for (int i = 0; i < length; i++) {
                //设置标题
                tabLayout.addTab(tabLayout.newTab().setText(mTitles.get(i)));
            }
        }
    }

    protected void setFragments() {
        int size = mTitles.size();
        for (int i = 0; i < size; i++) {
            mFragments.add(getFragment(i));
        }
    }

    public BaseFragment getCurrentFragment(){
        return mFragments.get(mViewPager.getCurrentItem());
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        pos = position;
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * 初始化调用该方法
     */
    public void initTitleAndViewPager() {
        mFragments.clear();
        setTitles(mTabContainer);
        setTitleAttr(mTabContainer);
        setFragments();
        mFragmentAdapter =
                new BaseTabFragmentAdapter(getChildFragmentManager(), mFragments, mTitles);
        //给ViewPager设置适配器
        mViewPager.setAdapter(mFragmentAdapter);
        //将TabLayout和ViewPager关联起来。
        mTabContainer.setupWithViewPager(mViewPager, true);
        mViewPager.setCurrentItem(setCurrentTab());
    }


    /**
     * 更新title内容
     */
    public void notifyTitle() {
        int size = getTitles().size();
        for (int i = 0; i < size; i++) {
            TabLayout.Tab tab = mTabContainer.getTabAt(i);
            if (tab != null)
                tab.setText(getTitles().get(i));
        }
    }

    /**
     * 设置tab默认选中哪一个
     *
     * @return 默认0为第一个页面
     */
    protected int setCurrentTab() {
        return 0;
    }

    /**
     * 设置title属性值 分割线 选中状态属性等
     */
    protected void setTitleAttr(TabLayout layout) {
    }

    protected abstract List<String> getTitles();

    protected abstract BaseFragment getFragment(int position);

    protected abstract int getLayout();

    protected abstract void initWidget();
}
