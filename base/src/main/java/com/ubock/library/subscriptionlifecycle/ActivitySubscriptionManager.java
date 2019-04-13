package com.ubock.library.subscriptionlifecycle;

/**
 * RxJava的Subscription管理器，统一管理网络Subscription
 * Created by chenguandu on 2017/6/18.
 */

public interface ActivitySubscriptionManager extends SubscriptionManager {
    int onStart = 0;
    int onResume = 1;
    int onRestart = 2;
    int onPause = 3;
    int onStop = 4;
}
