package org.smart.framework.ioc;

import org.smart.framework.aop.annotation.Aspect;
import org.smart.framework.core.ClassHelper;
import org.smart.framework.core.fault.InitializationError;
import org.smart.framework.ioc.annotation.Bean;
import org.smart.framework.mvc.annotation.Action;
import org.smart.framework.tx.annotation.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BeanHelper {
    private static final Map<Class<?>,Object> beanMap = new HashMap<>();

    static {
        try {
            List<Class<?>> classList = ClassHelper.getClassList();
            for(Class<?> cls: classList){
                //TODO 将实现了注解的类放入BeanMap实例中
                /**
                 * cls.isAnnotationPresent(Bean.class)
                 */
                if(cls.isAnnotationPresent(Bean.class)||
                    cls.isAnnotationPresent(Service.class)||
                    cls.isAnnotationPresent(Action.class)||
                    cls.isAnnotationPresent(Aspect.class)) {

                    Object beanInstance = cls.newInstance();
                    beanMap.put(cls,beanInstance);
                }
            }
        } catch (Exception e) {
            throw new InitializationError("初始化 BeanHelper 出错!",e);
        }
    }

    /**
     * 获取BeanMap
     * @return
     */
    public static Map<Class<?>,Object> getBeanMap(){
        return beanMap;
    }

    /**
     * 获取bean实例
     * @param cls
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(Class<?> cls){
        if(!beanMap.containsKey(cls)){
            throw new RuntimeException("无法根据类名获取实例"+cls);
        }
        return (T) beanMap.get(cls);
    }

    /**
     * 设置bean实例
     */
    public static void setBean(Class<?> cls,Object obj){
        beanMap.put(cls,obj);
    }


}
