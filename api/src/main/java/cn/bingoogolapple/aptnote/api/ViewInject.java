package cn.bingoogolapple.aptnote.api;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:2017/9/21
 * 描述:
 */
public interface ViewInject<T> {
    void inject(T host, Object source);
}