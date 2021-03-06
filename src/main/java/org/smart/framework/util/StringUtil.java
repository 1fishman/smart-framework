package org.smart.framework.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

    /**
     * 字符串分隔符
     */
    public static final String SEPARATOR = String.valueOf((char)29);

    /**
     * 判断字符是否为空
     */
    public static boolean isNotEmpty(String s){
        return StringUtils.isNotEmpty(s);
    }

    public static boolean isEmpty(String s){
        return StringUtils.isEmpty(s);
    }

    /**
     * 若字符串为空,则为默认值
     * @param str
     * @param defaultValue
     * @return
     */
    public static String defaultIfEmpty(String str,String defaultValue){
        return StringUtils.defaultString(str,defaultValue);
    }


    /**
     * 替换固定格式的字符串(支持正则表达式)
     * @param str
     * @param regex
     * @param replacement
     * @return
     */
    public static String replaceAll(String str,String regex,String replacement){
        //匹配模式
        Pattern p = Pattern.compile(regex);
        //匹配好的字符窜
        Matcher m = p.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (m.find()){

            m.appendReplacement(sb,replacement);
        }
        m.appendTail(sb);

        return sb.toString();
    }

    /**
     * 判断是否为数字(整数或小数)
     * @param str
     * @return
     */
    public static boolean isNumber(String str){
        return NumberUtils.isNumber(str);
    }

    /**
     * 是否为十进制数(整数)
     * @param str
     * @return
     */
    public static boolean isDigits(String str){
        return NumberUtils.isDigits(str);
    }

    /**
     * 将驼峰风格替换为下划线风格
     * @param str
     * @return
     */
    public static String camelhumpToUnderline(String str){
        Matcher matcher = Pattern.compile("[A-Z]").matcher(str);
        StringBuilder builder = new StringBuilder(str);
        for(int i=0;matcher.find();i++){
            builder.replace(matcher.start()+i,matcher.end()+i,"_"+matcher.group().toLowerCase());

        }
        if(builder.charAt(0)=='_'){
            builder.deleteCharAt(0);
        }
        return builder.toString();
    }

    /**
     * 将下划线风格转为驼峰风格
     * @param str
     * @return
     */
    public static String underlineToCamelhump(String str){
        Matcher matcher = Pattern.compile("_[a-z]").matcher(str);
        StringBuilder builder = new StringBuilder();
        for(int i=0;matcher.find();i++){
            builder.replace(matcher.start()-i,matcher.end()-i,matcher.group().substring(1).toUpperCase());

        }
        if (Character.isUpperCase(builder.charAt(0))) {
            builder.replace(0,1,String.valueOf(Character.toLowerCase(builder.charAt(0))));
        }
        return builder.toString();
    }

    /**
     * 分割固定格式的字符串
     * @param str
     * @param separator
     * @return
     */
    public static String[] splitString(String str,String separator){
        return StringUtils.splitByWholeSeparator(str,separator);
    }

    /**
     * 将首字符大写
     * @param str
     * @return
     */
    public static String firstToUpper(String str){
        return Character.toUpperCase(str.charAt(0))+str.substring(1);
    }

    /**
     * 将首字符小写
     * @param str
     * @return
     */
    public static String firstToLower(String str){
        return Character.toLowerCase(str.charAt(0))+str.substring(1);
    }

    /**
     * 转为帕斯卡命名方式(如:FooBar)
     * @param str
     * @param seperator
     * @return
     */
    public static String toPascalStyle(String str,String seperator){
        return StringUtil.firstToUpper(toCamelhumpStyle(str,seperator));
    }

    /**
     * 将指定的分隔符转为驼峰命名方式(如fooBar)
     * @param str
     * @param seperator
     * @return
     */
    public static String toCamelhumpStyle(String str,String seperator){
        return StringUtil.underlineToCamelhump(toUnderlineStyle(str,seperator));
    }

    /**
     * 将指定的分隔符转为下划线命名方式(foo_var)
     * @param str
     * @param seperator
     * @return
     */
    public static String toUnderlineStyle(String str,String seperator){
        str = str.trim().toLowerCase();
        if(str.contains(seperator)){
            str = str.replace(seperator,"_");
        }
        return str;
    }

    public static String toDisplayStyle(String str,String seperator){
        StringBuilder builder= new StringBuilder();
        String display = "";
        str = str.trim().toLowerCase();
        if(str.contains(seperator)){
            String[] words = StringUtil.splitString(str,seperator);
            for(String word: words){
                builder.append(StringUtil.firstToUpper(word)+" ");
            }
            display = builder.toString();
            display = display.trim();
        }else {
            display = StringUtil.firstToUpper(str);
        }
        return display;
    }









}
