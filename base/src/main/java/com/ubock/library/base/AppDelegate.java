package com.ubock.library.base;

import android.content.Context;

/**
 * Created by ChenGD on 2018-01-25.<br/>
 * 模块的app代理类，用来做模块的初始化工作，在应用启动时会自动回调{@link #onCreate}  <br/>
 * 用法：  <br/>
 * 在模块内继承此类，然后在模块的androidManifest.xml中的application标签内增加如下设置:<br/>
 * &lt;meta-data android:name="app_delegate_module_demo" android:value="com.ubock.demo.DemoApp" /&gt; <br/>
 * android:name必须以{@link com.ubock.library.base.BaseConfig#APP_DELEGATE_PREFIX}为前缀 <br/>
 * android:value为实现类的绝对路径，即包名+类名
 */

public abstract class AppDelegate {
    protected abstract void onCreate(Context context);
}
