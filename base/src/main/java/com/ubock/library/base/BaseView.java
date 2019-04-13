package com.ubock.library.base;

/**
 * Created by chengd on 17/3/8.
 */
public interface BaseView {

    /**
     * 显示加载
     */
    void showLoading();
    /**
     * 显示加载
     */
    void showLoading(String message);

    /**
     * 隐藏加载
     */
    void hideLoading();

    /**
     * 显示信息
     */
    void showMessage(String message);

}
