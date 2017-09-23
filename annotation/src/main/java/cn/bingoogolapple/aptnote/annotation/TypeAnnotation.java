package cn.bingoogolapple.aptnote.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:2017/9/20
 * 描述:
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface TypeAnnotation {
    String value() default "default";
}