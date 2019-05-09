package org.smart.framework.ioc;

import org.smart.framework.core.ClassHelper;
import org.smart.framework.core.fault.InitializationError;
import org.smart.framework.ioc.annotation.Impl;
import org.smart.framework.ioc.annotation.Inject;
import org.smart.framework.util.ArrayUtil;
import org.smart.framework.util.CollectionUtil;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public class IocHelper {
    static {
        try {
            //获取并遍历所有的Bean类
            Map<Class<?>,Object> beanMap = BeanHelper.getBeanMap();
            for (Map.Entry<Class<?>,Object> beanEntry: beanMap.entrySet()){
                //获取 Bean类与Bean类的实例
                Class<?> beanCls = beanEntry.getKey();
                Object beanInstance = beanEntry.getValue();
                Field[] beanFields = beanCls.getDeclaredFields();
                if(ArrayUtil.isNotEmpty(beanFields)){
                    //遍历所有的Bean字段
                    for(Field beanField : beanFields){
                        if(beanField.isAnnotationPresent(Inject.class)){
                            //获取 bean 字段对应的接口
                            Class<?> interfaceClass = beanField.getType();
                            //获取Bean字段对应的实现类
                            Class<?> implementClass = findImplementClass(interfaceClass);
                            if(implementClass != null){
                                // 获取代理实例
                                Object implementInstance = beanMap.get(implementClass);
                                if(implementInstance != null){
                                    beanField.setAccessible(true);
                                    beanField.set(beanInstance,implementInstance);
                                }else{
                                    throw new InitializationError("依赖注入失败!类名错误"+beanCls.getSimpleName());
                                }
                            }
                        }


                    }
                }

            }
        } catch (IllegalAccessException e) {
            throw new InitializationError("初始化 Ioc 容器出错",e);
        }
    }
    /**
     * 查找实现类
     */

    public static Class<?> findImplementClass(Class<?> interfaceClass){
        Class<?> implementClass = interfaceClass;
        //判断接口上是否标注了 Impl 注解
        if(interfaceClass.isAnnotationPresent(Impl.class)){
            //获取强制指定的实现类
            implementClass = interfaceClass.getAnnotation(Impl.class).value();
        }else{
            //获取该接口所有的实现类
            List<Class<?>> implementClassList = ClassHelper.getClassListBySuper(interfaceClass);
            if(CollectionUtil.isNotEmpty(implementClassList)){
                //获取第一个实现类
                implementClass = implementClassList.get(0);
            }
        }
        return implementClass;

    }
}
