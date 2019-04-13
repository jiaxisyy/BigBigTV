package com.ubock.library.utils;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.ubock.library.R;

import java.util.List;

/**
 * Created by ChenGD on 17/3/9.
 */
public class FragmentUtils extends Fragment {

    public static <T extends Fragment> T newFragment(Class<T> tClass) {
        T t = null;
        try {
            if(tClass!=null){
                t = tClass.newInstance();
            }
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return t;
    }

    /**
     * 查看源码发现Fragment.getFragmentManager()与
     * FragmentActivity.getSupportFragmentManager()获取的为同一对象
     * @param fragmentManager 传入Fragment.getFragmentManager()或FragmentActivity.getSupportFragmentManager()效果相同
     */
    public static <T extends Fragment> Fragment show(FragmentManager fragmentManager, int containerViewId, String tag, Class<T> tClass) {
        return show(fragmentManager, containerViewId, tag, tClass, null, null);
    }

    /**
     * @see #show(FragmentManager, int, String, Class)
     */
    public static <T extends Fragment> Fragment show(FragmentManager fragmentManager, int containerViewId, String tag, Class<T> tClass, Activity activity, String title) {
        Fragment fragment = fragmentManager.findFragmentByTag(tag);
        if (fragment == null) {
            fragment = newFragment(tClass);
            fragmentManager.beginTransaction().add(containerViewId, fragment, tag).commitAllowingStateLoss();
        }

        List<Fragment> list=  fragmentManager.getFragments();
        if (list != null) {
            for (Fragment f : list) {
                if (f != fragment) {
                    if (!f.isHidden()){
                        fragmentManager.beginTransaction().hide(f).commitAllowingStateLoss();
                    }
                }
            }
        }

        fragmentManager.beginTransaction().show(fragment).commitAllowingStateLoss();
        if (!fragment.isHidden()) {
            fragment.onHiddenChanged(false);
        }

        if (title != null && activity != null) {
            activity.setTitle(title);
        }
        return fragment;
    }
    /**
     * @see #show(FragmentManager, int, String, Class)
     *
     * @param invertAnimations  反转动画（方向反转）
     */
    public static <T extends Fragment> void show(FragmentManager fragmentManager, int containerViewId, String tag, Class<T> tClass, boolean invertAnimations) {
        Fragment fragment = fragmentManager.findFragmentByTag(tag);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (invertAnimations) {
            fragmentTransaction.setCustomAnimations(R.anim.slide_left_in, R.anim.slide_left_out);
        }
        else {
            fragmentTransaction.setCustomAnimations(R.anim.slide_right_in, R.anim.slide_right_out);
        }
        if (fragment == null) {
            fragmentTransaction.add(containerViewId, newFragment(tClass), tag).commitAllowingStateLoss();
        } else {
            fragmentTransaction.show(fragment).commitAllowingStateLoss();
        }
    }

    public static void hide(FragmentManager fragmentManager, String tag) {
        Fragment fragment = fragmentManager.findFragmentByTag(tag);
        if (fragment != null) {
            fragmentManager.beginTransaction().hide(fragment).commitAllowingStateLoss();
        }
    }

    public static boolean isHidden(FragmentManager fragmentManager, String tag){
    	Fragment fragment = fragmentManager.findFragmentByTag(tag);
    	if(fragment == null){
    		return true;
    	}
    	
    	return fragment.isHidden();
    }
    
    /**
     * 左右滑动
     * @param fragmentManager
     * @param tag
     * @param invertAnimations
     */
    public static void hide(FragmentManager fragmentManager, String tag, boolean invertAnimations) {
        Fragment fragment = fragmentManager.findFragmentByTag(tag);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (invertAnimations) {
            fragmentTransaction.setCustomAnimations(R.anim.slide_left_in, R.anim.slide_left_out);
        } else {
            fragmentTransaction.setCustomAnimations(R.anim.slide_right_in, R.anim.slide_right_out);
        }
        if (fragment != null) {
            fragmentTransaction.hide(fragment).commitAllowingStateLoss();
        }
    }

    /**
     * 上下滑动
     * @param fragmentManager
     * @param tag
     * @param invertAnimations
     */
    public static void hide1(FragmentManager fragmentManager, String tag, boolean invertAnimations) {
        Fragment fragment = fragmentManager.findFragmentByTag(tag);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (invertAnimations) {
            fragmentTransaction.setCustomAnimations(R.anim.slide_top_in, R.anim.slide_top_out);
        } else {
            fragmentTransaction.setCustomAnimations(R.anim.slide_down_in, R.anim.slide_down_out);
        }
        if (fragment != null) {
            fragmentTransaction.hide(fragment).commitAllowingStateLoss();
        }
    }

    public static <T extends Fragment> void attachFragment(FragmentManager fragmentManager, int containerViewId, String tag, Class<T> tClass) {
        attachFragment(fragmentManager, containerViewId, tag, tClass, null, null);
    }

    public static <T extends Fragment> void attachFragment(FragmentManager fragmentManager, int containerViewId, String tag, Class<T> tClass, Activity activity, String title) {
        Fragment fragment = fragmentManager.findFragmentByTag(tag);
        if (fragment == null) {
            fragmentManager.beginTransaction().add(containerViewId, newFragment(tClass), tag).commitAllowingStateLoss();
        } else {
            fragmentManager.beginTransaction().attach(fragment).commitAllowingStateLoss();
        }
        if (title != null && activity != null) {
            activity.setTitle(title);
        }
    }

    public static void detachFragment(FragmentManager fragmentManager, String tag) {
        Fragment fragment = fragmentManager.findFragmentByTag(tag);
        if (fragment != null) {
            fragmentManager.beginTransaction().detach(fragment).commitAllowingStateLoss();
        }
    }
    
    public static boolean isDetach(FragmentManager fragmentManager, String tag){
    	Fragment fragment = fragmentManager.findFragmentByTag(tag);
        return fragment == null || fragment.isDetached();

    }
}
