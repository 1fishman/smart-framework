package org.smart.framework.util;


import org.apache.commons.lang3.ArrayUtils;

/**
 * 类型转换操作工具类
 */

public class CastUtil {

    /**
     * object转换为String型
     */
    public static String castString(Object obj){
        return CastUtil.castString(obj,"");
    }

    public static String castString(Object obj, String defaultValue){
        return obj != null?String.valueOf(obj):defaultValue;
    }

    /**
     * 转换为long类型
     * @param obj
     * @return
     */
    public static double castDouble(Object obj){
        return castDouble(obj,0);
    }

    public static double castDouble(Object obj, double defaultValue){
        double doubleValue = defaultValue;
        if(obj!=null){
            String strVal = castString(obj);
            if(StringUtil.isNotEmpty(strVal)){
                try{
                    doubleValue = Double.parseDouble(strVal);
                }catch (NumberFormatException e){
                    doubleValue = defaultValue;
                }
            }
        }
        return doubleValue;
    }

    /**
     * 转换为long型
     * @param obj
     * @return
     */
    public static long castLong(Object obj){
        return CastUtil.castLong(obj,0);
    }

    public static long castLong(Object obj, long defaultValue){
        long longValue = defaultValue;
        if(obj!=null){
            String strVal = castString(obj);
            if(StringUtil.isNotEmpty(strVal)){
                try{
                    longValue = Long.parseLong(strVal);
                }catch (NumberFormatException e){
                    longValue = defaultValue;
                }
            }
        }
        return longValue;
    }

    /**
     * 转换为long型
     * @param obj
     * @return
     */
    public static int castInt(Object obj){
        return CastUtil.castInt(obj,0);
    }

    public static int castInt(Object obj, int defaultValue){
        int intValue = defaultValue;
        if(obj!=null){
            String strVal = castString(obj);
            if(StringUtil.isNotEmpty(strVal)){
                try{
                    intValue = Integer.parseInt(strVal);
                }catch (NumberFormatException e){
                    intValue = defaultValue;
                }
            }
        }
        return intValue;
    }

    /**
     * 转为 boolean 型
     */
    public static boolean castBoolean(Object obj) {
        return CastUtil.castBoolean(obj, false);
    }

    /**
     * 转为 boolean 型（提供默认值）
     */
    public static boolean castBoolean(Object obj, boolean defaultValue) {
        boolean booleanValue = defaultValue;
        if (obj != null) {
            booleanValue = Boolean.parseBoolean(castString(obj));
        }
        return booleanValue;
    }

    /**
     * 转换为String[]类型
     * @param objArray
     * @return
     */
    public static String[] castStringArray(Object[] objArray){
        if(objArray==null){
            objArray = new Object[0];
        }
        String[] strArray = new String[objArray.length];
        if(ArrayUtils.isNotEmpty(objArray)){
            for(int i=0;i<objArray.length;i++){
                strArray[i] = castString(objArray[i]);
            }
        }
        return strArray;
    }

    /**
     * 转换为double[]类型
     * @param objArray
     * @return
     */
    public static double[] castDoubleArray(Object[] objArray){
        if(objArray==null){
            objArray = new Object[0];
        }
        double[] doubleArray = new double[objArray.length];
        if(ArrayUtils.isNotEmpty(objArray)){
            for(int i=0;i<objArray.length;i++){
                doubleArray[i] = castDouble(objArray[i]);
            }
        }
        return doubleArray;
    }

    /**
     * 转换为int[]类型
     * @param objArray
     * @return
     */
    public static int[] castIntArray(Object[] objArray){
        if(objArray==null){
            objArray = new Object[0];
        }
        int[] intArray = new int[objArray.length];
        if(ArrayUtils.isNotEmpty(objArray)){
            for(int i=0;i<objArray.length;i++){
                intArray[i] = castInt(objArray[i]);
            }
        }
        return intArray;
    }

    /**
     * 转换为long[]类型
     * @param objArray
     * @return
     */
    public static long[] castLongArray(Object[] objArray){
        if(objArray==null){
            objArray = new Object[0];
        }
        long[] longArray = new long[objArray.length];
        if(ArrayUtil.isNotEmpty(objArray)){
            for(int i=0;i<objArray.length;i++){
                longArray[i] = castLong(objArray[i]);
            }
        }
        return longArray;
    }

    /**
     * 转为 boolean[] 型
     */
    public static boolean[] castBooleanArray(Object[] objArray) {
        if (objArray == null) {
            objArray = new Object[0];
        }
        boolean[] booleanArray = new boolean[objArray.length];
        if (!ArrayUtil.isEmpty(objArray)) {
            for (int i = 0; i < objArray.length; i++) {
                booleanArray[i] = castBoolean(objArray[i]);
            }
        }
        return booleanArray;
    }



}
