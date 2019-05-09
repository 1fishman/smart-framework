package org.smart.framework.core;

import org.smart.framework.FrameworkConstant;
import org.smart.framework.util.PropsUtil;

import java.util.Map;
import java.util.Properties;

public class ConfigHelper {

    private static final Properties CONFIG_PROPS = PropsUtil.loadProps(FrameworkConstant.CONFIG_PROPS);


    public static String getString(String key){
        return PropsUtil.getString(CONFIG_PROPS,key);
    }

    /**
     * 获取String类型的属性值(可指定默认值)
     * @param key
     * @param defaultValue
     * @return
     */
    public static String getString(String key,String defaultValue){
        return PropsUtil.getString(CONFIG_PROPS,key,defaultValue);
    }

    /**
     * 获取 int类型的属性值
     * @param key
     * @return
     */
    public static int getInt(String key){
        return PropsUtil.getNumber(CONFIG_PROPS,key);
    }

    /**
     * 获取int类型属性 (可指定默认值)
     * @param key
     * @param defaultValue
     * @return
     */
    public static int getInt(String key,int defaultValue){
        return PropsUtil.getNumber(CONFIG_PROPS,key,defaultValue);
    }

    /**
     * 获取boolean类型的值
     * @param key
     * @return
     */
    public static boolean getBoolean(String key){
        return PropsUtil.getBoolean(CONFIG_PROPS,key);
    }
    public static boolean getBoolean(String key,boolean defaultValue){
        return PropsUtil.getBoolean(CONFIG_PROPS,key,defaultValue);
    }


    public static Map<String,Object> getMap(String prefix){
        return PropsUtil.getMap(CONFIG_PROPS,prefix);
    }









}
