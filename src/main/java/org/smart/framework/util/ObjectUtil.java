package org.smart.framework.util;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 对象操作工具类
 */

public class ObjectUtil {
    private static final Logger logger = LoggerFactory.getLogger(ObjectUtil.class);

    /**
     * 设置成员变量
     * @param obj
     * @param fieldName
     * @param fieldValue
     */
    public static void setField(Object obj,String fieldName,Object fieldValue){
        try {
            if(PropertyUtils.isWriteable(obj,fieldName)){
                PropertyUtils.setProperty(obj,fieldName,fieldValue);
            }
        } catch (Exception e) {
            logger.error("设置成员变量出错!",e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取成员变量
     * @param obj
     * @param fieldName
     * @return
     */
    public static Object getFieldValue(Object obj,String fieldName){
        Object propertyValue = null;
        try {
            if(PropertyUtils.isReadable(obj,fieldName)){
                propertyValue = PropertyUtils.getProperty(obj, fieldName);
            }
        }catch (Exception e){
            logger.error("获取成员变量出错!",e);
            throw new RuntimeException(e);
        }
        return propertyValue;
    }

    /**
     * 复制所有成员变量到target中
     * @param source
     * @param target
     */
    public static void copyFields(Object source,Object target){
        try {
            for (Field field:source.getClass().getDeclaredFields()){
                //若不为static成员变量,则进行复制操作
                if(!Modifier.isStatic(field.getModifiers())){
                    field.setAccessible(true);
                    field.set(target,field.get(source));
                }
            }
        }catch (Exception e){
            logger.error("复制成员出错!",e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 通过反射创建实例
     * @param className
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T newInstance(String className){
        T instance;
        try {
            Class<?> commandClass = ClassUtil.loadClass(className);
            instance = (T) commandClass.newInstance();
        }catch (Exception e){
            logger.error("创建实例"+ className+"出错!",e);
            throw new RuntimeException(e);
        }
        return instance;
    }

    /**
     * 获取对象的字段映射(字段名=>字段值),忽略static字段
     * @param obj
     * @return
     */
    public static Map<String,Object> getFieldMap(Object obj){
        return getFieldMap(obj,true);
    }

    public static Map<String,Object> getFieldMap(Object obj,boolean isStaticIgnored){
        Map<String,Object> fieldMap = new LinkedHashMap<>();
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field: fields){
            if(isStaticIgnored && Modifier.isStatic(field.getModifiers())){
                continue;
            }
            String fieldName = field.getName();
            Object fieldValue = ObjectUtil.getFieldValue(obj,fieldName);
            fieldMap.put(fieldName,fieldValue);

        }
        return fieldMap;
    }


}
