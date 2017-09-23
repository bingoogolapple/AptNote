package cn.bingoogolapple.aptnote.compiler.ioc;

import com.google.auto.service.AutoService;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

import cn.bingoogolapple.aptnote.annotation.ioc.BindView;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:2017/9/21
 * 描述:
 */
@SuppressWarnings("unused")
@AutoService(Processor.class)
public class ViewInjectProcessor extends AbstractProcessor {
    private Filer mFileUtils;
    private Elements mElementUtils;
    private Messager mMessager;
    private Map<String, ProxyInfo> mProxyMap = new HashMap<>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mFileUtils = processingEnv.getFiler();
        mElementUtils = processingEnv.getElementUtils();
        mMessager = processingEnv.getMessager();
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    /**
     * 告知 Processor 哪些注解需要处理
     *
     * @return 返回一个 Set 集合，集合内容为自定义注解的包名+类名
     */
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        final Set<String> annotationTypes = new LinkedHashSet<>();
        annotationTypes.add(BindView.class.getCanonicalName());
        return annotationTypes;
    }

    /**
     * 所有的注解处理都是从这个方法开始的，当 APT 找到所有需要处理的注解后，会回调这个方法。当没有属于该 Processor 处理的注解被使用时，不会回调该方法
     *
     * @param set              所有的由该 Processor 处理，并待处理的 Annotations「属于该 Processor 处理的注解，但并未被使用，不存在与这个集合里」
     * @param roundEnvironment 表示当前或是之前的运行环境，可以通过该对象查找到注解
     * @return 表示这组 Annotation 是否被这个 Processor 消费，如果消费「返回 true」后续子的 Processor 不会再对这组 Annotation 进行处理
     */
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        mProxyMap.clear();
        collectProxyInfo(roundEnvironment);
//        generateJavaCode();
        generateJavaCodeWithJavaPoet();
        return true;
    }

    private void collectProxyInfo(RoundEnvironment roundEnvironment) {
        for (Element element : roundEnvironment.getElementsAnnotatedWith(BindView.class)) {
            checkAnnotationValid(element, BindView.class);

            VariableElement variableElement = (VariableElement) element;

            TypeElement hostClassElement = (TypeElement) variableElement.getEnclosingElement();
            String className = hostClassElement.getSimpleName().toString();
            String fullClassName = hostClassElement.getQualifiedName().toString();

            ProxyInfo proxyInfo = mProxyMap.get(fullClassName);
            if (proxyInfo == null) {
                proxyInfo = new ProxyInfo(mElementUtils, hostClassElement);
                mProxyMap.put(fullClassName, proxyInfo);
            }

            BindView bindViewAnnotation = variableElement.getAnnotation(BindView.class);
            int id = bindViewAnnotation.value();
            proxyInfo.injectVariables.put(id, variableElement);
        }
    }

    private void generateJavaCodeWithJavaPoet() {
        for (String key : mProxyMap.keySet()) {
            ProxyInfo proxyInfo = mProxyMap.get(key);
            try {
                proxyInfo.generateJavaCodeWithJavaPoet(mFileUtils);
            } catch (IOException e) {
                error(proxyInfo.getHostClassElement(), "Unable to write injector for type %s: %s", proxyInfo.getHostClassElement(), e.getMessage());
            }
        }
    }

    private void generateJavaCode() {
        for (String key : mProxyMap.keySet()) {
            ProxyInfo proxyInfo = mProxyMap.get(key);
            try {
                JavaFileObject javaFileObject = mFileUtils.createSourceFile(proxyInfo.getProxyClassFullName(), proxyInfo.getHostClassElement());
                Writer writer = javaFileObject.openWriter();
                writer.write(proxyInfo.generateJavaCode());
                writer.flush();
                writer.close();
            } catch (IOException e) {
                error(proxyInfo.getHostClassElement(), "Unable to write injector for type %s: %s", proxyInfo.getHostClassElement(), e.getMessage());
            }
        }
    }

    private boolean checkAnnotationValid(Element annotatedElement, Class clazz) {
        if (annotatedElement.getKind() != ElementKind.FIELD) {
            error(annotatedElement, "%s must be declared on field.", clazz.getSimpleName());
            return false;
        }
        if (ClassValidator.isPrivate(annotatedElement)) {
            error(annotatedElement, "%s() must can not be private.", annotatedElement.getSimpleName());
            return false;
        }

        return true;
    }

    private void error(Element element, String message, Object... args) {
        if (args.length > 0) {
            message = String.format(message, args);
        }
        mMessager.printMessage(Diagnostic.Kind.ERROR, message, element);
    }
}
