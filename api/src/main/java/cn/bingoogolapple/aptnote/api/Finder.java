package cn.bingoogolapple.aptnote.api;

import android.app.Activity;
import android.view.View;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:2017/9/21
 * 描述:
 */
public enum Finder {
    VIEW {
        @Override
        public View findView(Object source, int id) {
            return ((View) source).findViewById(id);
        }
    },
    ACTIVITY {
        @Override
        public View findView(Object source, int id) {
            return ((Activity) source).findViewById(id);
        }
    };

    public abstract View findView(Object source, int id);
}