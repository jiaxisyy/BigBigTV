package com.ubock.library.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by chengd on 17/3/8.
 */
public interface Presenter {
    void onCreate();
    void onPostCreate(@Nullable Bundle savedInstanceState);
    void onStart();
    void onResume();
    void onPause();
    void onStop();
    void onDestroy();

}
