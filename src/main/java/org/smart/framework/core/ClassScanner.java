package org.smart.framework.core;

import java.lang.annotation.Annotation;
import java.util.List;

public interface ClassScanner {

    /**
     * 获取包名下的所有类
     * @param packageName
     * @return
     */
    List<Class<?>>  getClassList(String packageName);

    /**
     * 获取包名指定注解的相关类
     * @param packageName
     * @param annotationClass
     * @return
     */

    List<Class<?>> getClassListByAnnotation(String packageName, Class<? extends Annotation> annotationClass);


    /**
     * 获取包名指定父类或接口的相关类
     * @param packageName
     * @param superClass
     * @return
     */
    List<Class<?>> getClassListBySuper(String packageName, Class<?> superClass);
}
