package org.smart.framework.core;

import org.smart.framework.InstanceFactory;

import java.lang.annotation.Annotation;
import java.util.List;

public class ClassHelper {

    /**
     * 获取基础包名
     */
    private static final String basePackage = ConfigHelper.getString("smart.framework.app.base_package");

    /**
     * 获取classScanner
     */
    private static final ClassScanner classScanner = InstanceFactory.getClassScanner();

    /**
     * 获取基础包名中的所有类
     * @return
     */
    public static List<Class<?>> getClassList(){
        return classScanner.getClassList(basePackage);
    }

    /**
     * 获取基础包名中制定父类或接口的相关类
     * @param superClass
     * @return
     */
    public static List<Class<?>> getClassListBySuper(Class<?> superClass){
        return classScanner.getClassListBySuper(basePackage,superClass);
    }

    public static List<Class<?>> getclassListByAnnotation(Class<? extends Annotation> annotationClass){
        return classScanner.getClassListByAnnotation(basePackage,annotationClass);
    }





}
