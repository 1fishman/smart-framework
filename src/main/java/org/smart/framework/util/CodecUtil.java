package org.smart.framework.util;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smart.framework.FrameworkConstant;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.UUID;


public class CodecUtil {

    private static final Logger logger = LoggerFactory.getLogger(CodecUtil.class);

    /**
     * 将URL 编码
     * @param str
     * @return
     */
    public static String encodeURL(String str){
        String target;
        try {
            target = URLEncoder.encode(str, FrameworkConstant.UTF_8);
        } catch (UnsupportedEncodingException e) {
            logger.error("编码出错!",e);
            throw new RuntimeException();
        }
        return target;
    }

    /**
     * 将URL解码
     */
    public static String decodeURL(String str){
        String target;
        try{
            target = URLDecoder.decode(str,FrameworkConstant.UTF_8);

        } catch (UnsupportedEncodingException e) {
            logger.error("解码出错!",e);
            throw new RuntimeException(e);
        }
        return target;
    }

    /**
     * 将字符串Base64编码
     * @param str
     * @return
     */
    public static String encodeBASE64(String str){
        String target;
        try {
            target = Base64.encodeBase64URLSafeString(str.getBytes(FrameworkConstant.UTF_8));
        } catch (UnsupportedEncodingException e) {
            logger.error("编码出错!",e);
            throw new RuntimeException(e);
        }
        return target;
    }

    /**
     * 将字符串 Base64 解码
     */
    public static String decodeBASE64(String str) {
        String target;
        try {
            target = new String(Base64.decodeBase64(str), FrameworkConstant.UTF_8);
        } catch (UnsupportedEncodingException e) {
            logger.error("解码出错！", e);
            throw new RuntimeException(e);
        }
        return target;
    }

    /**
     * 将字符串MD5加密
     */
    public static String encryptMD5(String str){
        return DigestUtils.md5Hex(str);
    }


    /**
     * 将字符串SHA加密
     */

    public static String encryptSHA(String str){
        return DigestUtils.sha1Hex(str);
    }

    /**
     * 创建随机数
     * @param count
     * @return
     */
    public static String createRandom(int count){
        return RandomStringUtils.randomNumeric(count);
    }

    public static String createUUID(){
        return UUID.randomUUID().toString().replaceAll("-","");
    }







}
