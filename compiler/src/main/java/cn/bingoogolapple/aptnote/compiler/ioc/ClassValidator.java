package cn.bingoogolapple.aptnote.compiler.ioc;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:2017/9/21
 * 描述:
 */
final class ClassValidator {

    static boolean isPrivate(Element annotatedClass) {
        return annotatedClass.getModifiers().contains(Modifier.PRIVATE);
    }

    static String getClassName(TypeElement type, String packageName) {
        int packageLen = packageName.length() + 1;
        return type.getQualifiedName().toString().substring(packageLen).replace('.', '$');
    }
}
