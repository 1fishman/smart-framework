package org.smart.framework.util;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smart.framework.FrameworkConstant;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *  web 操作工具类
 */
public class WebUtil {
    private static final Logger logger = LoggerFactory.getLogger(WebUtil.class);


    /**
     *  将数据以 JSON 格式写入响应中
     */
    public static void writeJSON(HttpServletResponse response,Object data){
        try {
            //设置响应头
            //制定内容类型为 JSON 格式
            response.setContentType("application/json");
            //防止中文乱码
            response.setCharacterEncoding(FrameworkConstant.UTF_8);
            //向响应中写入数据
            PrintWriter writer = response.getWriter();
            // 将对象转为json字符串
            writer.write(JsonUtil.toJSON(data));
            writer.flush();
            writer.close();
        } catch (IOException e) {
            logger.error("在响应中写数据出错!",e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 将数据以 HTML 格式写入响应中(在 JS 中获取的是 JSON字符串, 而不是 JSON对象)
     *
     */
    public static void writeHTML(HttpServletResponse response, Object data){
        try {
            //设置响应头
            response.setContentType("text/html");
            response.setCharacterEncoding(FrameworkConstant.UTF_8);
            //向响应中写数据
            PrintWriter writer = response.getWriter();
            writer.write(JsonUtil.toJSON(data));
            writer.flush();
            writer.close();
        }catch (Exception e){
            logger.error("在响应中写数据出错!",e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 从请求中获取所有参数( 当参数名重复时候,后者覆盖前者)
     */
    public static Map<String,Object> getRequestParamMap(HttpServletRequest request){

        Map<String,Object> paramMap = new LinkedHashMap<>();
        try {
            //获取请求的方法
            String method = request.getMethod();
            if(method.equalsIgnoreCase("put") || method.equalsIgnoreCase("delete")){
                String queryString = CodecUtil.decodeURL(StreamUtil.getString(request.getInputStream()));
                if(StringUtil.isNotEmpty(method)){
                    String[] qsArray = StringUtil.splitString(queryString,"&");
                    if(ArrayUtil.isNotEmpty(qsArray)){
                        for(String qs : qsArray){
                            String[] array = StringUtil.splitString(qs,"=");
                            if(ArrayUtil.isNotEmpty(array) && array.length == 2){
                                String paramName = array[0];
                                String paramValue = array[1];
                                //这里是直接向重名的参数后加上此参数
                                if(checkParamName(paramName)){
                                    if(paramMap.containsKey(paramName)){
                                        paramValue = paramMap.get(paramName)+StringUtil.SEPARATOR+paramValue;
                                    }
                                    paramMap.put(paramName,paramValue);
                                }
                            }
                        }
                    }
                }
            }else{
                //先获得所有的参数名字
                Enumeration<String> paramNames = request.getParameterNames();
                while (paramNames.hasMoreElements()){
                    String paramName = paramNames.nextElement();
                    if(checkParamName(paramName)){
                        //从参数名字或的所有的此参数名的值
                        String[] paramValues = request.getParameterValues(paramName);
                        if(ArrayUtil.isNotEmpty(paramValues)){
                            if(paramValues.length==1){
                                paramMap.put(paramName,paramValues[0]);
                            }else{
                                StringBuilder paramValue = new StringBuilder("");
                                for (int i=0;i < paramValues.length ; i++){
                                    paramValue.append(paramValues[i]);
                                    if(i != paramValues.length-1){
                                        paramValue.append(StringUtil.SEPARATOR);
                                    }
                                }
                                paramMap.put(paramName,paramValue.toString());
                            }
                        }
                    }
                }
            }

        } catch (IOException e) {
            logger.error("获取请求参数出错!",e);
            throw new RuntimeException(e);
        }
        return paramMap;
    }

    /**
     * 检查参数名字是否符合,忽略jQuery的缓存参数
     */
    public static boolean checkParamName(String paramName){
        return !paramName.equals("_");
    }


    /**
     * 转发请求
     */

    public static void forwardRequest(String path,HttpServletRequest request, HttpServletResponse response){
        try {
            request.getRequestDispatcher(path).forward(request,response);
        }catch (Exception e){
            logger.error("转发请求出错!",e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 重定向请求
     */

    public static void redirectRequest(String path,HttpServletRequest request,HttpServletResponse response){
        try {
            System.out.println(request.getContextPath());
            response.sendRedirect(request.getContextPath()+path);
        }catch (Exception e){
            logger.error("重定向请求出错!",e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 发送错误代码
     */
    public static void sendError(int code,String message, HttpServletResponse response){
        try {
            response.sendError(code,message);
        }catch (Exception e){
            logger.error("发送错误代码出错!",e);
            throw new RuntimeException(e);
        }
    }
    /**
     * 判断是否为 AJAX 请求
     */
    public static boolean isAJAX(HttpServletRequest request){
        return request.getHeader("X-Requested_With") != null;
    }

    /**
     * 获取请求路径
     */

    public static String getRequestPath(HttpServletRequest request){
        String servletPath = request.getServletPath();
        String pathInfo = StringUtil.defaultIfEmpty(request.getPathInfo(),"");
        return servletPath+pathInfo;
    }

    /**
     * 从 Cookie 中获取数据
     */

    public static String getCookie(HttpServletRequest request,String name){
        String value = "";
            Cookie[] cookies = request.getCookies();
            if(cookies != null){
                for (Cookie cookie : cookies){
                    if(StringUtil.isNotEmpty(name) && name.equals(cookie.getName())){
                        value = CodecUtil.decodeURL(cookie.getValue());
                        break;
                    }
                }
            }
        return value;
    }

    /**
     * 下载文件
     */
    public static void downloadFile(HttpServletResponse response,String filePath){
        try {
            //获取要下载的文件名
            String originalFileName = FilenameUtils.getName(filePath);
            String downloadedFileName = new String(originalFileName.getBytes("GBK"),"ISO8859_1");

            // 设置请求头,为流输入,和文件名字
            response.setContentType("application/octet-stream");
            response.addHeader("Content-Disposition","attachment;filename=\""+downloadedFileName+"\"");
            //获取文件的输入流和网页输出流
            InputStream inputStream = new BufferedInputStream(new FileInputStream(filePath));
            OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
            //将输入流copy到输出流
            StreamUtil.copyStream(inputStream,outputStream);
        } catch (Exception e) {
            logger.error("下载文件出错!",e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 设置 Redirect URL到Session中
     * @param request
     * @param sessionKey
     */
    public static void setRedirectUrl(HttpServletRequest request,String sessionKey){
        if(isAJAX(request)){
            String requestPath = getRequestPath(request);
            request.getSession().setAttribute(sessionKey,requestPath);
        }
    }

    /**
     * 是否为IE 浏览器
     * @param request
     * @return
     */
    public boolean isIE(HttpServletRequest request){
        String agent = request.getHeader("User-Agent");
        return agent != null && agent.contains("MSIE");
    }
}
