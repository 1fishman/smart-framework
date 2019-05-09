package org.smart.framework.mybatis;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smart.framework.FrameworkConstant;
import java.io.IOException;
import java.io.Reader;

public class MybatisHelper {
    private static final Logger log = LoggerFactory.getLogger(MybatisHelper.class);
    private static Reader reader;
    private static SqlSessionFactory sqlSessionFactory;
    static {
        try {
            reader = Resources.getResourceAsReader(FrameworkConstant.MYBATIS_CONFIG+"/mybatis-config.xml");
        } catch (IOException e) {
            System.out.println("配置文件读取出错");
            log.error("配置文件读取出错");
            e.printStackTrace();
        }
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
    }

    public static SqlSessionFactory getSqlSessionFactory(){
        return sqlSessionFactory;
    }
}
