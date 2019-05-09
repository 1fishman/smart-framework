package org.smart.framework;


import org.smart.framework.core.ConfigHelper;

public interface FrameworkConstant {
    String UTF_8 = "UTF-8";

    String CONFIG_PROPS = "smart.properties";
    String SQL_PROPS = "smart-sql.properties";
    String PLUGIN_PACKAGE = "org.smart4j.plugin";

    String MYBATIS_CONFIG=ConfigHelper.getString("mybatis.configLocation", "mybatis-config.xml");
    // 是否使用 mybatis
    boolean MABATIS=ConfigHelper.getBoolean("mybatis.switch",false);
    String JSP_PATH = ConfigHelper.getString("smart.framework.app.jsp_path","/WEB-INF/jsp/");
    String WWW_PATH = ConfigHelper.getString("smart.framework.app.www_path","/www/");
    String HOME_PAGE = ConfigHelper.getString("smart.framework.app.home_page","/index.jsp");
    int UPLOAD_LIMIE = ConfigHelper.getInt("smart.framework.app.upload_limit",10);


    String PK_NAME = "id";
}
