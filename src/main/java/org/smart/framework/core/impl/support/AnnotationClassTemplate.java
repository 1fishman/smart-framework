package org.smart.framework.core.impl.support;

import java.lang.annotation.Annotation;

/**
 * 获取注解类的模板类
 */
public abstract class AnnotationClassTemplate extends ClassTemplate{
    protected final Class<? extends Annotation> annotationClass;


    public AnnotationClassTemplate(String packageName ,Class<? extends Annotation> annotationClass) {
        super(packageName);
        this.annotationClass = annotationClass;
    }

}
