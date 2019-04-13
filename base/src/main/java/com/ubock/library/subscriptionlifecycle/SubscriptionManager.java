package com.ubock.library.subscriptionlifecycle;

import rx.Subscription;

/**
 * RxJava的Subscription管理器，统一管理网络Subscription
 * Created by chenguandu on 2017/6/18.
 */

public interface SubscriptionManager {
    int onDestroy = 5;

    void subscribe(Subscription subscription);
    void subscribe(Subscription subscription, int lifeCycle);
    void unSubscribe(Subscription subscription);
}
