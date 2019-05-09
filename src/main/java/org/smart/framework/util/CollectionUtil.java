package org.smart.framework.util;

import org.apache.commons.collections4.CollectionUtils;

import java.util.Collection;

/**
 * 集合操作工具类
 */

public class CollectionUtil {

    public static boolean isNotEmpty(Collection<?> collection)
    {
        return CollectionUtils.isNotEmpty(collection);
    }

    public static boolean isEmpty(Collection<?> collection){
        return CollectionUtil.isEmpty(collection);
    }
}
