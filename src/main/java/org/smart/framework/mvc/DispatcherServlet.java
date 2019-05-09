package org.smart.framework.mvc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smart.framework.FrameworkConstant;
import org.smart.framework.InstanceFactory;
import org.smart.framework.util.WebUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 前段控制器
 */
@WebServlet(urlPatterns = "/", loadOnStartup = 0)
public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1222846483886835263L;

    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);

    private HandlerMapping handlerMapping = InstanceFactory.getHandlerMapping();
    private HandlerInvoker handlerInvoker = InstanceFactory.getHandlerInvoker();
    private HandlerExceptionResolver handlerExceptionResolver = InstanceFactory.getHandlerExceptionResolver();

    @Override
    public void init(ServletConfig config) throws ServletException {
        //初始化相关配置
        ServletContext servletContext = config.getServletContext();
        UploadHelper.init(servletContext);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 设置请求编码方式
        req.setCharacterEncoding(FrameworkConstant.UTF_8);
        //获取当前请求相关数据
        String currentRequestMethod = req.getMethod();
        String currentRequestPath = WebUtil.getRequestPath(req);
        logger.debug("[Smart]{}:{}",currentRequestMethod,currentRequestPath);

        //将"/" 请求重定向到首页
        if(currentRequestPath.equals("/")){
            WebUtil.redirectRequest(FrameworkConstant.HOME_PAGE,req,resp);
        }

        //去掉当前请求路径的"/"
        if(currentRequestPath.endsWith("/")){
            currentRequestPath = currentRequestPath.substring(0,currentRequestPath.length()-1);
        }
        //获取 Handler
        Handler handler = handlerMapping.getHandler(currentRequestMethod,currentRequestPath);

        //如果没有找到对应的 Action ,则跳转到 404页面
        if(handler == null){
            WebUtil.sendError(HttpServletResponse.SC_NOT_FOUND,"",resp);
            return;
        }
        //初始化 DataContext
        DataContext.init(req,resp);
        try {
            // 这里实现安全检查,不用filter实现,只是让用户在发现没有权限的时候抛出异常,到这里调用
            // 调用 Handler的 处理方法, 处理方法中添加逻辑
            handlerInvoker.invokedHandler(req,resp,handler);
        } catch (Exception e) {
            // 处理 Action 异常
            handlerExceptionResolver.resolveHandlerException(req,resp,e);
        }finally {
            DataContext.destroy();
        }
    }
}
