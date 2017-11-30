package cn.bingoogolapple.aptnote.api;

import android.util.Log;

import cn.bingoogolapple.aptnote.test.Test;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:2017/11/30
 * 描述:
 */
public class TestWrapper {
    public static String test(String arg) {
        Log.d("TestWrapper", "测试包装 " + arg);
        return Test.test(arg);
    }
}
