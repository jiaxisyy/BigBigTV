package com.ubock.library.ui.adapter;

import android.support.v4.app.FixedFragmentStatePagerAdapter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.ubock.library.base.BaseFragment;

import java.util.List;

/**
 * Created by ChenGD on 2017/4/18.
 */
public class BaseTabFragmentAdapter extends FixedFragmentStatePagerAdapter {
    private List<BaseFragment> mFragments;
    private List<String> mTitles;

    public BaseTabFragmentAdapter(FragmentManager fm, List<BaseFragment> fragments, List<String> titles) {
        super(fm);
        this.mFragments = fragments;
        this.mTitles = titles;
    }

    public void setFragments(List<BaseFragment> fragments){
        this.mFragments = fragments;
    }

    public void setTitles(List<String> titles){
        this.mTitles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments == null ? 0 : mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (mTitles.size() - 1 < position) return "";
        return mTitles.get(position);
    }

}
