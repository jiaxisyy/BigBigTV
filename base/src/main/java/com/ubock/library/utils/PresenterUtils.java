package com.ubock.library.utils;

import com.ubock.library.annotation.Presenter;
import com.ubock.library.annotation.PresenterInjector;
import com.ubock.library.base.BaseModel;
import com.ubock.library.base.BasePresenter;
import com.ubock.library.base.BaseView;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ChenGD on 2018/6/10.
 *
 * @author chenguandu
 *
 * Presenter实例化工具类
 */

public class PresenterUtils {

    private Map<String, Object> mPresenterList = new HashMap<>();
    private Object mObject;
    private Class<?> mClass;
    private BaseView mBaseView;

    private PresenterUtils(){}

    public PresenterUtils(Object obj, BaseView baseView){
        mObject = obj;
        mClass = obj.getClass();
        mBaseView = baseView;
        resolvePresenter();
        resolvePresenterInjector();
    }

    public void release(){
        mObject = null;
        mClass = null;
        mBaseView = null;
        mPresenterList.clear();
    }

    public <P extends BasePresenter> P getPresenter(Class<P> cls){
        return (P)mPresenterList.get(cls.getCanonicalName());
    }

    /**
     * 根据类注解中的索引来获取presenter
     * <br/>
     * 注意：此方法只能获取@{@link Presenter}注解中的presenter
     * @param index 索引从0开始
     * @param <P> 返回的presenter类型
     * @return 返回的presenter类型
     */
    public <P extends BasePresenter> P getPresenter(int index){
        Presenter presenter = mClass.getAnnotation(Presenter.class);
        if (presenter == null) {
            return null;
        }
        if (presenter.presenter().length == 0) {
            return null;
        }
        if (index >= 0 && index < presenter.presenter().length) {
            P p = getPresenter((Class<P>)presenter.presenter()[index]);
            return p;
        } else {
            return null;
        }
    }

    /**
     * 构造presenter对象
     * @param cls 必须是继承BasePresenter的类
     * @param <P> presenter的真实类
     * @return 返回presenter对象
     */
    private <P extends BasePresenter> P newPresenter(Class<P> cls){
        P p = null;
        try {
            Class<? extends BaseModel> modelType = getRawType(cls, 0);
            Class<? extends BaseView> viewType = getRawType(cls, 1);
            Constructor c = cls.getDeclaredConstructor(new Class[]{modelType, viewType});
            p = (P)c.newInstance(new Object[]{modelType.newInstance(), mBaseView});
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e){
            LogUtils.e(e);
        } catch (InvocationTargetException e){
            LogUtils.e(e);
        }
        return p;
    }

    private <P extends BasePresenter> void resolvePresenter() {
        Presenter presenter = mClass.getAnnotation(Presenter.class);
        if (presenter != null) {

            Class<P>[] classes = (Class<P>[]) presenter.presenter();
            for (Class<P> clazz : classes) {
                String canonicalName = clazz.getCanonicalName();
                P p = newPresenter(clazz);
                if (p != null) {
                    mPresenterList.put(canonicalName, p);
                }
            }
        }
    }

    private <P extends BasePresenter> void resolvePresenterInjector() {
        for (Field field : mClass.getDeclaredFields()) {
            Annotation[] annotations = field.getDeclaredAnnotations();
            if (annotations == null || annotations.length < 1) {
                continue;
            }
            for (Annotation a : annotations) {
                if (a instanceof PresenterInjector) {
                    String canonicalName = field.getType().getCanonicalName();
                    P presenter = (P) mPresenterList.get(canonicalName);
                    try {
                        field.setAccessible(true);
                        if (presenter == null) {
                            Class<P> c = (Class<P>)field.getType();
                            presenter = newPresenter(c);
                        }
                        if (presenter != null) {
                            field.set(mObject, presenter);
                            mPresenterList.put(canonicalName, presenter);
                        }
                    } catch (IllegalAccessException e) {
                        LogUtils.e(e);
                    }
                    break;
                }
            }
        }
    }

    /**
     * 根据泛型生成presenter对象
     * @param obj
     * @param view
     * @return
     */
    public BasePresenter createPresenterByGenericSuperclass(Class obj, BaseView view){
        BasePresenter p = null;
        Class<? extends BasePresenter> cls = getPresenterType(obj);
        if (cls != null){
            //从注解中获取，如果没有再根据泛型生成
            p = getPresenter(cls);
            if (p == null) {
                try {
                    Class<? extends BaseModel> modelType = getRawType2(obj, 0);
                    Class<? extends BaseView> viewType = getRawType2(obj, 1);
                    Constructor c = cls.getDeclaredConstructor(new Class[]{modelType, viewType});
                    p = (BasePresenter) c.newInstance(new Object[]{modelType.newInstance(), view});
                    mPresenterList.put(cls.getCanonicalName(), p);
                } catch (InstantiationException e) {
                    LogUtils.e(e);
                } catch (IllegalAccessException e) {
                    LogUtils.e(e);
                } catch (NoSuchMethodException e) {
                    LogUtils.e(e);
                }catch (IllegalArgumentException e){
                    LogUtils.e(e);
                } catch (InvocationTargetException e) {
                    LogUtils.e(e);
                }
            }
        }
        return p;
    }

    /**
     * 获取presenter类型
     * @return
     */
    private Class<? extends BasePresenter> getPresenterType(Class<?> classz){
        Class<? extends BasePresenter> rawType = getRawType(classz, 0);
        return rawType;
    }

    /**
     * 获取mode和view的类型
     * @param classz
     * @param index 0为model 1为view
     * @param <T> 返回的类型
     * @return
     */
    private <T> T getRawType2(Class<?> classz, int index){
        T rawType = null;
        Type type = classz.getGenericSuperclass();//拿到带类型参数的泛型父类
        if(type instanceof ParameterizedType){//这个Type对象根据泛型声明
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();//获取泛型的类型参数数组
            if(actualTypeArguments != null && actualTypeArguments.length > 0){
                if(actualTypeArguments[0] instanceof Class){//类型参数也有可能不是Class类型
                    Class<? extends BasePresenter> cls = (Class<? extends BasePresenter>) actualTypeArguments[0];
                    Type type2 = cls.getGenericSuperclass();//拿到带类型参数的泛型父类
                    //泛型中的泛型
                    if(type2 instanceof ParameterizedType){//这个Type对象根据泛型声明
                        ParameterizedType parameterizedType2 = (ParameterizedType) type2;
                        Type[] actualTypeArguments2 = parameterizedType2.getActualTypeArguments();//获取泛型的类型参数数组
                        if(actualTypeArguments2 != null && actualTypeArguments2.length > index){
                            if(actualTypeArguments2[index] instanceof Class){//类型参数也有可能不是Class类型
                                rawType = (T) actualTypeArguments2[index];
                            }
                        }
                    }
                }
            }
        }
        return rawType;
    }

    /**
     * 获取mode和view的类型
     * @param classz
     * @param index 0为model 1为view
     * @param <T> 返回的类型
     * @return
     */
    private <T> T getRawType(Class<?> classz, int index){
        T rawType = null;
        Type type = classz.getGenericSuperclass();//拿到带类型参数的泛型父类
        if(type instanceof ParameterizedType){//这个Type对象根据泛型声明
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();//获取泛型的类型参数数组
            if(actualTypeArguments != null && actualTypeArguments.length > index){
                if(actualTypeArguments[index] instanceof Class){//类型参数也有可能不是Class类型
                    rawType = (T) actualTypeArguments[index];
                }
            }
        }
        return rawType;
    }

}
