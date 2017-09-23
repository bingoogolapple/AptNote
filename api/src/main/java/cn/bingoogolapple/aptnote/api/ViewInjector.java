package cn.bingoogolapple.aptnote.api;

import android.app.Activity;
import android.view.View;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:2017/9/21
 * 描述:
 */
public class ViewInjector {
    private static final String SUFFIX = "$$ViewInject";

    public static void injectView(Activity activity) {
        ViewInject proxyActivity = findProxyActivity(activity);
        proxyActivity.inject(activity, activity);
    }

    public static void injectView(Object object, View view) {
        ViewInject proxyActivity = findProxyActivity(object);
        proxyActivity.inject(object, view);
    }

    private static ViewInject findProxyActivity(Object activity) {
        try {
            Class clazz = activity.getClass();
            Class injectorClazz = Class.forName(clazz.getName() + SUFFIX);
            return (ViewInject) injectorClazz.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new RuntimeException(String.format("can not find %s , something when compiler.", activity.getClass().getSimpleName() + SUFFIX));
    }
}