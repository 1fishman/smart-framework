package org.smart.framework.mvc;

import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smart.framework.FrameworkConstant;
import org.smart.framework.HelperLoader;
import org.smart.framework.util.StringUtil;
import org.smart.framework.util.WebUtil;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebListener;

/**
 * 容器监听器
 */
@WebListener
public class ContainerListener implements ServletContextListener {
    private static final Logger logger = LoggerFactory.getLogger(ContainerListener.class);
    /**
     * 初始化时候使用
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        BasicConfigurator.configure();
        logger.debug("ContainerListerer 启动");
        //获取 ServletContext
        ServletContext servletContext = sce.getServletContext();
        // 初始化相关Helper
        HelperLoader.init();
        //添加 Servlet 映射
        addServletMapping(servletContext);
        //注册WebPlugin
        registerWebPlugin(servletContext);

    }

    /**
     * 当容器销毁时调用
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }

    private void addServletMapping(ServletContext context){
        //用 DefaultServlet 映射所有静态资源
        registerDefaultServlet(context);
        // 用 JspServlet 映射所有 JSP请求
        registerJspServlet(context);
    }

    // 注册一个默认的Servlet,添加一些默认的页面路径,一些静态资源可以通过默认 Servlet获得.不需要在代码中配置太多.
    private void registerDefaultServlet(ServletContext context){
        ServletRegistration defaultServlet = context.getServletRegistration("default");
        defaultServlet.addMapping("/index.jsp");
        defaultServlet.addMapping("/favicon.ico");
        defaultServlet.addMapping("/static/*");
        // www_path 不用,映射都到一个完整的文件里就可以了.
        /*String wwwPath = FrameworkConstant.WWW_PATH;
        if(StringUtil.isNotEmpty(wwwPath)){
            defaultServlet.addMapping(wwwPath+"*");
        }*/
    }


    /**
     * 注册jsp Servlet
     * @param context
     */
    private void registerJspServlet(ServletContext context){
        ServletRegistration jspServlet = context.getServletRegistration("jsp");
        jspServlet.addMapping("/index.jsp");
        String jspPath = FrameworkConstant.JSP_PATH;
        if(StringUtil.isNotEmpty(jspPath)){
            jspServlet.addMapping(jspPath+"*");
        }
    }

    /**
     * 注册 web plugin.
     * @param servletContext
     */
    //TODO 先不写plugin
    private void registerWebPlugin(ServletContext servletContext){

    }

    //TODO 销毁 plugin 先不写
    private void destroyPlugin(){

    }

}
