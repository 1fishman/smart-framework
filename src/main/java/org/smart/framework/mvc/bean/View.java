package org.smart.framework.mvc.bean;

import java.util.HashMap;
import java.util.Map;

/**
 *  返回的是视图对象
 */
public class View {
    private static final long serialVersionUID = -9017853846630360550L;


    private String path ; //视图路径
    /**
     * 模型数据
     */
    private Map<String,Object> data;
    public View(String path){
        this.path = path;
        data = new HashMap<>();
    }

    /**
     * 向模型中添加数据
     * @param key
     * @param value
     * @return
     */
    public View addModel(String key, Object value){
        data.put(key,value);
        return this;
    }

    public boolean isRedirect(){
        return path.startsWith("redirect:");
    }

    public String getPath(){
        return path;
    }

    public void setPath(String path){
        this.path = path;
    }

    public Map<String,Object> getData(){
        return data;
    }

}
