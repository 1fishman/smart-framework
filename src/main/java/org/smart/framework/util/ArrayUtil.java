package org.smart.framework.util;

import org.apache.commons.lang3.ArrayUtils;

/**
 * 数组操作工具类
 */
public class ArrayUtil {

    /**
     * 判断数组是否非空
     */
    public static boolean isNotEmpty(Object[] array){

        return !ArrayUtils.isEmpty(array);
    }

    /**
     * 判断是否为空
     * @param array
     * @return
     */
    public static boolean isEmpty(Object[] array){
        return ArrayUtils.isEmpty(array);
    }

    /**
     * 判断对象是否在数组中
     * @param array
     * @param obj
     * @param <T>
     * @return
     */
    public static <T> boolean contains(T[] array,T obj){
        return ArrayUtils.contains(array,obj);
    }

    /**
     * 连接数组
     * @param arr1
     * @param arr2
     * @return
     */
    public static Object[] concat(Object[] arr1, Object[] arr2){
        return ArrayUtils.addAll(arr1,arr2);
    }




}
