package org.smart.framework.util;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JSON 数据操作工具类
 */
public class JsonUtil {
    private static final Logger logger = LoggerFactory.getLogger(JsonUtil.class);

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 将 java 对象转为 JSON 字符串
     * @param obj
     * @param <T>
     * @return
     */
    public static <T> String toJSON(T obj){
        String jsonStr;
        try {
            jsonStr = objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            logger.error("java 转 JSON 出错!",e);
            throw new RuntimeException(e);
        }
        return jsonStr;
    }

    /**
     * 将 JSON字符串 转为 java对象
     * @param json
     * @param type
     * @param <T>
     * @return
     */
    public static <T> T fromJson(String json,Class<T> type){
        T obj;
        try{
            obj = objectMapper.readValue(json,type);
        }catch (Exception e){
            logger.error("JSON 转 Java"+ type.getSimpleName()+"对象 出错");
            throw new RuntimeException(e);
        }
        return obj;
    }






}
