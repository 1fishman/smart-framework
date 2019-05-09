package org.smart.framework.mvc.impl;

import org.smart.framework.FrameworkConstant;
import org.smart.framework.mvc.ViewResolver;
import org.smart.framework.mvc.bean.Result;
import org.smart.framework.mvc.bean.View;
import org.smart.framework.util.MapUtil;
import org.smart.framework.util.WebUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

//TODO
public class DefaultViewResolver implements ViewResolver {
    @Override
    public void resolveView(HttpServletRequest request, HttpServletResponse response, Object actionMethodResult){
        String jspPath = FrameworkConstant.JSP_PATH;
        if(actionMethodResult != null){
            // Action 返回值可以为 View 或 Result
            if(actionMethodResult instanceof View){
                View view = (View) actionMethodResult;
                if(view.isRedirect()){
                    // 获取路径
                    String path = view.getPath();
                    // 重定向请求
                    WebUtil.redirectRequest(path,request,response);
                }else{
                    //获取路径
                    String path =jspPath+ view.getPath();
                    // 初始化请求属性

                    Map<String,Object> data = view.getData();
                    if(MapUtil.isNotEmpty(data)){
                        //将数据写入到request请求中
                        for (Map.Entry<String,Object> entry : data.entrySet()){
                            request.setAttribute(entry.getKey(),entry.getValue());
                        }
                    }
                    WebUtil.forwardRequest(path,request,response);
                }
            }else{
                // 若为Result类型,则需要考虑两种请求类型(html页面请求 或者 普通数据请求)
                Result result = (Result) actionMethodResult;

                //将结果同意转换为JSON格式写入响应中.
                WebUtil.writeJSON(response,result);

            }
        }
    }
}
