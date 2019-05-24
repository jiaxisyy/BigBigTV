package android.support.v4.app;

/**
 * Created by Chenguandu on 2019/5/24.
 *
 * 解决问题ClassNotFoundException when unmarshalling: android.support.v4.view.ViewPager$SavedState
 *
 * 这种报错有一种原因是与Android的classloader机制有关。
 * Android有两种不同的classloaders：framework classloader和Apk classloader，
 * 应用启动时默认的classloader是Apk classloader，
 * 可以加载Parcelable解组所需的类。当内存不足时，
 * 默认classloader将变为framework classloader，
 * 它不知道如何加载自己定义的类，因而会报错。
 * 解决方法：在解组数据前，加上bundle.setClassLoader(getClass().getClassLoader())，
 * 将恢复Apk classloader方式。
 */
import android.os.Bundle;
import android.view.ViewGroup;

public abstract class FixedFragmentStatePagerAdapter extends FragmentStatePagerAdapter {

    public FixedFragmentStatePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment f = (Fragment)super.instantiateItem(container, position);
        Bundle savedFragmentState = f.mSavedFragmentState;
        if (savedFragmentState != null) {
            savedFragmentState.setClassLoader(f.getClass().getClassLoader());
        }
        return f;
    }

}