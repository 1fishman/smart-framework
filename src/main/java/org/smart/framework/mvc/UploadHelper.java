package org.smart.framework.mvc;

import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smart.framework.FrameworkConstant;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//TODO

/**
 * 封装文件上传相关操作
 */
public class UploadHelper {
    private static final Logger logger = LoggerFactory.getLogger(UploadHelper.class);

    /**
     * FileUpload 对象(用于解析所上传的文件)
     */
    private static ServletFileUpload fileUpload;

    /**
     * 初始化
     * @param servletContext
     */
    public static void init(ServletContext servletContext){
        //获取一个临时目录(使用Tomcat的work 目录)
        File repository = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
        //创建FileUpload 对象
        fileUpload = new ServletFileUpload(new DiskFileItemFactory(DiskFileItemFactory.DEFAULT_SIZE_THRESHOLD,repository));
        //设置上传限制
        int uploadLimit = FrameworkConstant.UPLOAD_LIMIE;
        if(uploadLimit != 0){
            fileUpload.setFileSizeMax(uploadLimit*1024*1024);//单位为M
        }
    }

    /**
     * 判断请求是否为multipart类型
     * @param request
     * @return
     */
    public static boolean isMultipart(HttpServletRequest request){
        //判断上传文件的内容是否为multipart 类型
        return ServletFileUpload.isMultipartContent(request);
    }

    public static List<Object> createMultipartParamList(HttpServletRequest request){
        //定义参数列表
        List<Object> paramList = new ArrayList<>();
        //创建两个对象,分别对应 普通字段 和 文件字段
        Map<String,Object> fieldMap = new HashMap<>();
        return paramList;
    }
}
