package org.smart.framework.mvc.bean;

import org.smart.framework.util.CastUtil;

import java.util.Map;

/**
 * 封装请求的参数
 */
public class Params {
    private static final long serialversionUID = 8278186045251987772L;


    private final Map<String,Object> fieldMap;

    public Params(Map<String,Object> fieldMap){
        this.fieldMap = fieldMap;
    }

    public Map<String,Object> getFieldMap(){
        return fieldMap;
    }

    public String getString(String name){
        return CastUtil.castString(get(name));
    }

    public double getDouble(String name){
        return CastUtil.castDouble(get(name));
    }

    public long getLong(String name){
        return CastUtil.castLong(get(name));
    }

    public long getInt(String name){
        return CastUtil.castInt(get(name));
    }


    private Object get(String name){
        return fieldMap.get(name);
    }
}
