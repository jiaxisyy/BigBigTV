package com.ubock.library.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

import com.ubock.library.R;
import com.ubock.library.ui.adapter.BaseTabFragmentAdapter;
import com.ubock.library.widgets.ScrollViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ChenGD on 2017/4/18.<br/>
 * 通用的tablayout和viewPager组合<br/>
 * 只需要实现getTitles()和getFragment(int position)方法<br/>
 * 如果是异步数据，则需要在数据加载完成后调用updateView()刷新界面<br/>
 *
 * 需要图标需要重写getIconList()返回图标id列表<br/>
 *
 * 自定义菜单需要重写onCreateCustomView(TabLayout tabLayout)方法并返回true<br/>
 */

public abstract class BaseTabContainerActivity<P extends BasePresenter> extends BaseFragmentActivity<P> implements
        TabLayout.OnTabSelectedListener, ViewPager.OnPageChangeListener {
    protected TabLayout mTabContainer;
    protected ViewPager mViewPager;
    private List<String> mTitles = new ArrayList<>();
    private List<BaseFragment> mFragments = new ArrayList<>();
    protected int pos;
    protected BaseTabFragmentAdapter mFragmentAdapter;

    @CallSuper
    @Override
    protected void initView(Bundle savedInstanceState) {
        if (null != mTabContainer) {
            mTabContainer.addOnTabSelectedListener(this);
        }
        if (mViewPager != null) {
            mViewPager.addOnPageChangeListener(this);
        }
        setTitles(mTabContainer);
        setTitleAttr(mTabContainer);
        setFragments();
        initTitleAndViewPager();
        if (!onCreateCustomView(mTabContainer)){
            setIcons();
        }
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        View view = null;
        if ("android.support.design.widget.TabLayout".equals(name)){
            mTabContainer = new TabLayout(context, attrs);
            if (mTabContainer.getId() == View.NO_ID){
                mTabContainer.setId(R.id.base_tab_container);
            }
            view = mTabContainer;
        } else if ("android.support.v4.view.ViewPager".equals(name)){
            mViewPager = new ViewPager(context, attrs);
            if (mViewPager.getId() == View.NO_ID){
                mViewPager.setId(R.id.base_view_pager);
            }
            view = mViewPager;
            mViewPager.setOffscreenPageLimit(3);
        } else if ("com.ubock.library.widgets.ScrollViewPager".equals(name)){
            mViewPager = new ScrollViewPager(context, attrs);
            if (mViewPager.getId() == View.NO_ID){
                mViewPager.setId(R.id.base_view_pager);
            }
            view = mViewPager;
            mViewPager.setOffscreenPageLimit(3);
        } else {

        }
        if (view != null){
            return view;
        }
        return super.onCreateView(name, context, attrs);
    }

    public void setOffscreenPageLimit(int limit){
        if (mViewPager != null) {
            mViewPager.setOffscreenPageLimit(limit);
        }
    }

    private void setTitles(TabLayout tabLayout) {
        if (null != tabLayout) {
            if (mTitles.size() > 0) {
                mTitles.clear();
            }
            mTitles.addAll(getTitleList());
        }
    }

    private void setFragments() {
        mFragments.clear();
        if (mTitles != null && mTitles.size() > 0) {
            int size = mTitles.size();
            for (int i = 0; i < size; i++) {
                mFragments.add(getFragment(i));
            }
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

    public void setSelect(int position){
        mViewPager.setCurrentItem(position);
    }

    /**
     * 该方法在初始化title数据后必须调用
     */
    private void initTitleAndViewPager() {
        mFragmentAdapter = new BaseTabFragmentAdapter(getSupportFragmentManager(), mFragments, mTitles);
        //给ViewPager设置适配器
        mViewPager.setAdapter(mFragmentAdapter);
        //将TabLayout和ViewPager关联起来。
        mTabContainer.setupWithViewPager(mViewPager, true);
        mViewPager.setCurrentItem(setCurrentTab());
    }

    private void setIcons(){
        List<Integer> iconList = getIconList();
        if (iconList != null && iconList.size() > 0){
            for (int i = 0; i < mTabContainer.getTabCount(); i++) {
                TabLayout.Tab tab = mTabContainer.getTabAt(i);
                tab.setIcon(iconList.get(i));
            }
        }
    }

    /**
     * 更新title内容
     */
    public void notifyTitle() {
        int size = getTitleList().size();
        for (int i = 0; i < size; i++) {
            TabLayout.Tab tab = mTabContainer.getTabAt(i);
            if (tab != null)
                tab.setText(getTitleList().get(i));
        }
    }

    /**
     * 更新ViewPager内容
     */
    public void notifyViewPager(){
        if (mFragmentAdapter != null){
            mFragmentAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 更新内容
     */
    public void updateView(){
        setTitles(mTabContainer);
        mFragmentAdapter.setTitles(mTitles);
        mFragmentAdapter.setFragments(mFragments);
        notifyTitle();
        if (!onCreateCustomView(mTabContainer)){
            setIcons();
        }
        setTitleAttr(mTabContainer);
        setFragments();
        notifyViewPager();
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

    protected List<Integer> getIconList(){return null;}

    protected abstract List<String> getTitleList();

    protected abstract BaseFragment getFragment(int position);

    /**
     * 自定义菜单，请重写此方法,并返回true
     * <br/><br/>
     * int size = mTabContainer.getTabCount();<br/>
     * View view;<br/>
     * for (int i = 0; i < size; i++) {<br/>
     *   view = LayoutInflater.from(this).inflate(customViewId, null);<br/>
     *   mTabContainer.getTabAt(i).setCustomView(view);<br/>
     * }<br/><br/>
     * @param tabLayout 菜单栏
     * @return 如果自定义必须返回true,否则自定义无效
     *
     */
    protected boolean onCreateCustomView(TabLayout tabLayout){
        return false;
    }

}
