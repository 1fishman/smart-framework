package org.smart.framework.core.impl;

import org.smart.framework.core.ClassScanner;
import org.smart.framework.core.impl.support.AnnotationClassTemplate;
import org.smart.framework.core.impl.support.ClassTemplate;
import org.smart.framework.core.impl.support.SupperClassTemplate;

import java.lang.annotation.Annotation;
import java.util.List;

public class DefaultClassScanner implements ClassScanner {

    /**
     * 获取指定包名下的所有类
     * @param packageName
     * @return
     */
    @Override
    public List<Class<?>> getClassList(String packageName) {
        return new ClassTemplate(packageName){

            //处理是否能够加入类列表
            @Override
            public boolean checkAddClass(Class<?> cls) {
                String className = cls.getName();
                String pkgName = className.substring(0,className.lastIndexOf("."));
                return pkgName.startsWith(packageName);
            }
        }.getClassList();
    }


    /**
     * 获取指定包名下的所有注解类
     * @param packageName
     * @param annotationClass
     * @return
     */
    @Override
    public List<Class<?>> getClassListByAnnotation(String packageName, Class<? extends Annotation> annotationClass) {
        return new AnnotationClassTemplate(packageName, annotationClass){
            @Override
            public boolean checkAddClass(Class<?> cls) {
                return cls.isAnnotationPresent(annotationClass);
            }
        }.getClassList();
    }

    @Override
    public List<Class<?>> getClassListBySuper(String packageName, Class<?> superClass) {
        return new SupperClassTemplate(packageName,superClass){

            @Override
            public boolean checkAddClass(Class<?> cls) {
                return superClass.isAssignableFrom(cls) && !superClass.equals(cls);
            }
        }.getClassList();
    }
}
