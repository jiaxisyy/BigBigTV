package com.ubock.library.subscriptionlifecycle;

/**
 * RxJava的Subscription管理器，统一管理网络Subscription
 * Created by chenguandu on 2017/6/18.
 */

public interface FragmentSubscriptionManager extends ActivitySubscriptionManager {
    int onUserVisibleHint = 20;
    int onUserUnVisibleHint = 21;
}
