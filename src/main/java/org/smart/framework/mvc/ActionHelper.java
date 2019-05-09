package org.smart.framework.mvc;

import org.smart.framework.core.ClassHelper;
import org.smart.framework.mvc.annotation.Action;
import org.smart.framework.mvc.annotation.Request;
import org.smart.framework.util.ArrayUtil;
import org.smart.framework.util.CollectionUtil;
import org.smart.framework.util.StringUtil;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 初始化 Action 配置
 */
public class ActionHelper {
    /**
     * Action Map(Http请求与 Action 方法的映射)
     */
    private static final Map<Requester,Handler> actionMap = new LinkedHashMap<>();

    static {
        //获取所有的 标注了 Action 类
        List<Class<?>> actionClassList = ClassHelper.getclassListByAnnotation(Action.class);
        if(CollectionUtil.isNotEmpty(actionClassList)){
            // 定义两个 Action 类
            //普通 Action 类
            Map<Requester,Handler> commonActionMap = new HashMap<>();
            // 带有占位符的url路径map映射,比如在{}这个样子的参数
            Map<Requester,Handler> regexpActionMap = new HashMap<>();
            for(Class<?> actionClass : actionClassList){
                // 获取并遍历该 Action 方法
                Method[] actionMethods = actionClass.getDeclaredMethods();
                if(ArrayUtil.isNotEmpty(actionMethods)){
                    for(Method method: actionMethods){
                        //处理 Action 方法 将有注解的方法添加进去
                        handleActionMethod(actionClass,method,commonActionMap,regexpActionMap);
                    }
                }
            }
            actionMap.putAll(commonActionMap);
            actionMap.putAll(regexpActionMap);
        }
    }

    private static void handleActionMethod(Class<?> actionClass, Method actionMethod,Map<Requester, Handler> commonActionMap, Map<Requester, Handler> regexpActionMap){
        //判断当前 Action 方法是否带有 Request 注解
        if(actionMethod.isAnnotationPresent(Request.Get.class)){
            String requestPath = actionMethod.getAnnotation(Request.Get.class).value();
            putActionMap("GET",requestPath,actionClass,actionMethod,commonActionMap,regexpActionMap);
        }else if(actionMethod.isAnnotationPresent(Request.POST.class)){
            String requestPath = actionMethod.getAnnotation(Request.POST.class).value();
            putActionMap("POST",requestPath,actionClass,actionMethod,commonActionMap,regexpActionMap);
        }//TODO 至于delete put 方法暂时没有写
    }


    private static void putActionMap(String requestMethod, String requestPath, Class<?> actionClass, Method actionMethod, Map<Requester, Handler> commonActionMap, Map<Requester, Handler> regexpActionMap){
        //判断 Request Path 中是否带有占位符
        if(requestPath.matches(".+\\{\\w+\\}.*")){
            requestPath = StringUtil.replaceAll(requestPath,"\\{\\w+\\}", "(\\\\w+)");
            regexpActionMap.put(new Requester(requestMethod,requestPath),new Handler(actionClass,actionMethod));
        }else{
            commonActionMap.put(new Requester(requestMethod,requestPath),new Handler(actionClass,actionMethod));
        }
    }

    public static Map<Requester,Handler> getActionMap(){
        return actionMap;
    }

}
