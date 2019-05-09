package org.smart.framework.mvc.impl;

import org.smart.framework.mvc.ActionHelper;
import org.smart.framework.mvc.Handler;
import org.smart.framework.mvc.HandlerMapping;
import org.smart.framework.mvc.Requester;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DefaultHandlerMapping implements HandlerMapping {
    @Override
    public Handler getHandler(String currentRequestMethod, String currentRequestPath) {
        Handler handler = null;
        //获取 并且 遍历Action映射
        Map<Requester,Handler> actionMap = ActionHelper.getActionMap();
        for(Map.Entry<Requester,Handler> actionEntry : actionMap.entrySet()){
            //从Requester中获取Request 相关属性
            Requester requester = actionEntry.getKey();
            // 获取请求的方法
            String requestMethod = requester.getRequestMethod();
            //获取请求路径
            String requestPath = requester.getRequestPath();
            // 获取请求路径匹配器( 使用正则表达式匹配请求路径并从其中获取出响应的参数)
            Matcher requestPathMatcher = Pattern.compile(requestPath).matcher(currentRequestPath);
            //判断请求方法与路径是否同时匹配
            if(requestMethod.equalsIgnoreCase(currentRequestMethod) &&
                    requestPathMatcher.matches()){
                // 获取Handler 及其相关属性
                handler = actionEntry.getValue();
                if(handler != null){
                    handler.setRequestPathMatcher(requestPathMatcher);
                }
                //若匹配成功,则终止循环
            }
        }
        //返回该 handler
        return handler;
    }
}
