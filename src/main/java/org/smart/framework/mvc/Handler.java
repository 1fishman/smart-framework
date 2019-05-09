package org.smart.framework.mvc;

import java.lang.reflect.Method;
import java.util.regex.Matcher;

/**
 * 封装 Action 方法相关信息
 */
public class Handler {
    // handler 类型信息,备注了注解的类
    private Class<?> actionClass;
    // 对应的 Action 方法
    private Method actionMethod;
    private Matcher requestPathMatcher;

    public Handler(Class<?> actionClass,Method actionMethod){
        this.actionClass = actionClass;
        this.actionMethod = actionMethod;
    }

    public Class<?> getActionClass() {
        return actionClass;
    }

    public Method getActionMethod(){
        return actionMethod;
    }

    public Matcher getRequestPathMatcher(){
        return requestPathMatcher;
    }

    public void setRequestPathMatcher(Matcher requestPathMatcher){
        this.requestPathMatcher = requestPathMatcher;
    }

}
