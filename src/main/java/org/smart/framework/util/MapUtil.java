package org.smart.framework.util;

import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smart.framework.mvc.bean.Params;
import org.smart.framework.orm.EntityHelper;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 映射操作工具类
 */
public class MapUtil {
    private static final Logger logger = LoggerFactory.getLogger(MapUtil.class);

    public static boolean validFieldMap(Class<?> entityClass, Params params){
        //获取到字段和列名的映射
        Map<String,String> fields = EntityHelper.getFieldMap(entityClass);
        // 获取参数中字段和值的映射
        Map<String,Object> fieldMap = params.getFieldMap();
        //链表中存储的是请求参数中不在要映射对象中的字段列表中的参数
        List<String> keys = new ArrayList<>();
        if(isNotEmpty(fields) && isNotEmpty(fieldMap)){
            //遍历参数中的键值对
            for (Map.Entry<String,Object> entry : fieldMap.entrySet()){
                // 如果参数中的key不在对象的字段列表中时就添加到keys链表中
                if(!fields.containsKey(entry.getKey())){
                    keys.add(entry.getKey());
                }
            }
        }
        if(keys.size() > 0){
            for (String key : keys){
                // 移除参数列表中不在对象参数的参数
                fieldMap.remove(key);
                logger.warn("Remove invalid field["+key+"]");
            }
            return true;
        }

        return false;
    }

    public static boolean isNotEmpty(Map<?,?> map){
        return MapUtils.isNotEmpty(map);
    }
    public static boolean isEmpty(Map<?,?> map){
        return MapUtils.isEmpty(map);
    }

    /**
     * 转置 Map
     */
    public static <K,V> Map<V,K> invert(Map<K,V> source){
        Map<V,K> target = null;
        if(isNotEmpty(source)){
            target = new LinkedHashMap<>(source.size());
            for (Map.Entry<K,V> entry : source.entrySet()){
                target.put(entry.getValue(),entry.getKey());
            }
        }
        return target;
    }
}









