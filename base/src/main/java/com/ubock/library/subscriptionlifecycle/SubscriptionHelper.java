package com.ubock.library.subscriptionlifecycle;

import java.util.LinkedList;

import com.ubock.library.utils.LogUtils;
import rx.Subscription;

/**
 * RxJava的Subscription管理工具类，帮助管理网络Subscription
 * Created by chenguandu on 2017/6/18.
 */

public class SubscriptionHelper {
    public final static String TAG = "SubscriptionHelper";
    private LinkedList<SubscriptionItem> mSubscriptionList = new LinkedList<>();

    public void subscribe(Subscription subscription) {
        subscribe(subscription, SubscriptionManager.onDestroy);
    }

    public void subscribe(Subscription subscription, int lifeCycle) {
        switch (lifeCycle){
            case SubscriptionManager.onDestroy:
            case ActivitySubscriptionManager.onPause:
            case ActivitySubscriptionManager.onRestart:
            case ActivitySubscriptionManager.onResume:
            case ActivitySubscriptionManager.onStart:
            case ActivitySubscriptionManager.onStop:
            case FragmentSubscriptionManager.onUserVisibleHint:
            case FragmentSubscriptionManager.onUserUnVisibleHint:
                for (SubscriptionItem item : mSubscriptionList) {
                    if (item.subscription == subscription){
                        LogUtils.d(TAG, "subscription already added.");
                        return;
                    }
                }
                mSubscriptionList.add(new SubscriptionItem(lifeCycle, subscription));
                LogUtils.d(TAG, "after subscribe size:"+mSubscriptionList.size());
                break;
            default:
                throw new IllegalStateException("illegal lifecycle");
        }
    }

    public void unSubscribe(Subscription subscription){
        SubscriptionItem temp = null;
        for (SubscriptionItem item: mSubscriptionList) {
            if (subscription == item.subscription) {
                temp = item;
            }
        }
        if (!subscription.isUnsubscribed()){
            subscription.unsubscribe();
        }
        mSubscriptionList.remove(temp);
        LogUtils.d(TAG, "after unSubscribe size:"+mSubscriptionList.size());
    }

    public void unSubscribe(int lifeCycle){
        switch (lifeCycle){
            case SubscriptionManager.onDestroy:
                for (SubscriptionItem item: mSubscriptionList) {
                    if (!item.subscription.isUnsubscribed()) {
                        item.subscription.unsubscribe();
                    }
                }
                mSubscriptionList.clear();
                break;
            case ActivitySubscriptionManager.onPause:
            case ActivitySubscriptionManager.onRestart:
            case ActivitySubscriptionManager.onResume:
            case ActivitySubscriptionManager.onStart:
            case ActivitySubscriptionManager.onStop:
            case FragmentSubscriptionManager.onUserVisibleHint:
            case FragmentSubscriptionManager.onUserUnVisibleHint:
                LinkedList<SubscriptionItem> unSubscription = new LinkedList<>();
                for (SubscriptionItem item: mSubscriptionList) {
                    if (item.lifeCycle == lifeCycle) {
                        if (!item.subscription.isUnsubscribed()) {
                            item.subscription.unsubscribe();
                        }
                        unSubscription.add(item);
                    }
                }
                mSubscriptionList.removeAll(unSubscription);
                unSubscription.clear();
                unSubscription = null;
                break;
            default:
                throw new IllegalStateException("illegal lifecycle");
        }
//        LogUtils.d("after unSubscribe size:"+mSubscriptionList.size());
    }

    static class SubscriptionItem{
        protected int lifeCycle;
        protected Subscription subscription;
        public SubscriptionItem(int lifeCycle, Subscription subscription){
            this.lifeCycle = lifeCycle;
            this.subscription = subscription;
        }
    }
}
