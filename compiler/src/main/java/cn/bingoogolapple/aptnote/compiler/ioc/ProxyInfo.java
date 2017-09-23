package cn.bingoogolapple.aptnote.compiler.ioc;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:2017/9/21
 * 描述:
 */
public class ProxyInfo {
    public static final String PROXY = "ViewInject";
    public Map<Integer, VariableElement> injectVariables = new HashMap<>();

    private String packageName;
    private String proxyClassName;
    private TypeElement hostClassElement;

    public ProxyInfo(Elements elementUtils, TypeElement hostClassElement) {
        this.hostClassElement = hostClassElement;
        PackageElement packageElement = elementUtils.getPackageOf(hostClassElement);
        String packageName = packageElement.getQualifiedName().toString();
        String className = ClassValidator.getClassName(hostClassElement, packageName);
        this.packageName = packageName;
        this.proxyClassName = className + "$$" + PROXY;
    }

    public void generateJavaCodeWithJavaPoet(Filer filer) throws IOException {
        ClassName hostClass = ClassName.get(packageName, hostClassElement.getSimpleName().toString());

        MethodSpec.Builder injectMethodBuilder = MethodSpec.methodBuilder("inject")
                .addAnnotation(Override.class)
                .addJavadoc("我是 method Javadoc")
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class)
                .addParameter(hostClass, "host")
                .addParameter(ClassName.get("java.lang", "Object"), "source")
                .addComment("我是 Comment");

        for (int id : injectVariables.keySet()) {
            VariableElement element = injectVariables.get(id);
            String name = element.getSimpleName().toString();
            String type = element.asType().toString();

            injectMethodBuilder.beginControlFlow("if (source instanceof android.app.Activity)")
                    .addStatement("host." + name + " = (" + type + ")(((android.app.Activity)source).findViewById(" + id + "))")
                    .nextControlFlow("else")
                    .addStatement("host." + name + " = (" + type + ")(((android.view.View)source).findViewById(" + id + "))")
                    .endControlFlow();
        }

        TypeSpec proxyType = TypeSpec.classBuilder(proxyClassName)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addJavadoc("我是 class Javadoc")
                .addSuperinterface(ParameterizedTypeName.get(ClassName.get("cn.bingoogolapple.aptnote.api", "ViewInject"), hostClass))
                .addMethod(injectMethodBuilder.build())
                .build();

        JavaFile javaFile = JavaFile.builder(packageName, proxyType)
                .addFileComment("Generated code. Do not modify!")
                .build();
        javaFile.writeTo(filer);
    }

    public String generateJavaCode() {
        StringBuilder builder = new StringBuilder();
        builder.append("// Generated code. Do not modify!\n");
        builder.append("package ").append(packageName).append(";\n\n");
        builder.append("import cn.bingoogolapple.aptnote.api.*;\n");
        builder.append('\n');

        builder.append("public class ").append(proxyClassName).append(" implements " + ProxyInfo.PROXY + "<" + hostClassElement.getQualifiedName() + ">");
        builder.append(" {\n");

        generateMethods(builder);
        builder.append('\n');

        builder.append("}\n");
        return builder.toString();
    }

    private void generateMethods(StringBuilder builder) {
        builder.append("    @Override\n");
        builder.append("    public void inject(" + hostClassElement.getQualifiedName() + " host, Object source) {\n");

        for (int id : injectVariables.keySet()) {
            VariableElement element = injectVariables.get(id);
            String name = element.getSimpleName().toString();
            String type = element.asType().toString();
            builder.append("        if(source instanceof android.app.Activity){\n");
            builder.append("            host." + name).append(" = ");
            builder.append("(" + type + ")(((android.app.Activity)source).findViewById(" + id + "));\n");
            builder.append("        } else {\n");
            builder.append("            host." + name).append(" = ");
            builder.append("(" + type + ")(((android.view.View)source).findViewById(" + id + "));\n");
            builder.append("        }\n");
        }

        builder.append("    }\n");
    }

    public String getProxyClassFullName() {
        return packageName + "." + proxyClassName;
    }

    public TypeElement getHostClassElement() {
        return hostClassElement;
    }
}
