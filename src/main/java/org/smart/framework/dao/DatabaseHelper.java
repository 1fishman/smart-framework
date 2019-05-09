package org.smart.framework.dao;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smart.framework.InstanceFactory;
import org.smart.framework.core.ConfigHelper;
import org.smart.framework.ds.DataSourceFactory;
import org.smart.framework.util.ClassUtil;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class DatabaseHelper {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseHelper.class);

    /**
     * 定义一个局部线程变量(使每个线程都拥有自己的连接)
     */
    private static final ThreadLocal<Connection> connContainer = new ThreadLocal<>();

    /**
     * 定义一个数据源工厂
     */
    private static final DataSourceFactory dataSourceFactory = InstanceFactory.getDataSourceFactory();


    /**
     * 定义一个数据访问器
     */
    private static final DataAccessor dataAccessor= InstanceFactory.getDataAccessor();


    /**
     * 数据库类型
     */
    private static final String databaseType = ConfigHelper.getString("smart.framework.jdbc.type");

    public static String getDatabaseType(){
        return databaseType;
    }

    /**
     * 获取数据源
     * @return
     */
    public static DataSource getDataSource(){
        return dataSourceFactory.getDataSource();
    }

    /**
     * 获取数据库连接
     * @return
     */
    public static Connection getConnection(){
        Connection conn;
        try {
            //首先从ThreadLocal中获取
            conn = connContainer.get();
            if(conn == null){
                //不存在从DataSource中获取
                conn = getDataSource().getConnection();
            }
            if(conn != null){
                connContainer.set(conn);
            }
        } catch (SQLException e) {
            logger.error("获取数据库连接出错!",e);
            throw new RuntimeException(e);
        }
        return conn;
    }

    /**
     * 开启事务
     */
    public static void beginTransaction(){
        Connection conn = getConnection();
        if(conn!=null){
            try {
                conn.setAutoCommit(false);
            } catch (SQLException e) {
                logger.error("开启事务出错!",e);
                throw new RuntimeException(e);
            }finally {
                connContainer.set(conn);
            }
        }
    }

    /**
     * 提交事务
     */
    public static void commitTransaction(){
        Connection conn = getConnection();
        if(conn != null){
            try {
                conn.commit();
                conn.close();
            } catch (SQLException e) {
                logger.error("提交事务出错!",e);
                throw new RuntimeException(e);
            }finally {
                connContainer.remove();
            }
        }
    }

    /**
     * 回滚事务
     */
    public static void rollbackTransaction(){
        Connection conn = getConnection();
        if(conn !=null){
            try {
                conn.rollback();
                conn.close();
            } catch (SQLException e) {
                logger.error("回滚事务出错!",e);
                throw  new RuntimeException(e);
            } finally {
                connContainer.remove();
            }
        }
    }

    /**
     * 初始化SQL脚本
     */
    //TODO 可能改变更新操作
    public static void initSQL(String sqlPath){
        try {
            File sqlFile = new File(ClassUtil.getClassPath()+sqlPath);
            List<String> sqlList = FileUtils.readLines(sqlFile);
            for(String sql : sqlList){
                update(sql);
            }
        } catch (IOException e) {
            logger.error("初始化 SQL 脚本出错!",e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 根据SQL语句查询Entity
     */
    public static <T> T queryEntity(Class<T> entityClass,String sql,Object... params){
        return dataAccessor.queryEntity(entityClass,sql,params);
    }

    /**
     * 根据SQL语句查询Entity列表
     */
    public static <T> List<T> queryEntityList(Class<?> entityclass,String sql,Object... params){
        return (List<T>) dataAccessor.queryEntityList(entityclass,sql,params);
    }

    /**
     * 根绝 SQL 语句查询Entity 映射(Field Name => Field Value)
     */
    public static <K, V> Map<K, V> queryEntityMap(Class<V> entityClass, String sql, Object... params) {
        return dataAccessor.queryEntityMap(entityClass, sql, params);
    }

    /**
     * 根据 SQL 语句查询 Array 格式的字段（单条记录）
     */
    public static Object[] queryArray(String sql, Object... params) {
        return dataAccessor.queryArray(sql, params);
    }

    /**
     * 根据 SQL 语句查询 Array 格式的字段列表（多条记录）
     */
    public static List<Object[]> queryArrayList(String sql, Object... params) {
        return dataAccessor.queryArrayList(sql, params);
    }


    /**
     * 根据 SQL 语句查询 Map 格式的字段（单条记录）
     */
    public static Map<String, Object> queryMap(String sql, Object... params) {
        return dataAccessor.queryMap(sql, params);
    }


    /**
     * 根据 SQL 语句查询 Map 格式的字段列表（多条记录）
     */
    public static List<Map<String, Object>> queryMapList(String sql, Object... params) {
        return dataAccessor.queryMapList(sql, params);
    }

    /**
     * 根据 SQL 语句查询指定字段（单条记录）
     */
    public static <T> T queryColumn(String sql, Object... params) {
        return dataAccessor.queryColumn(sql, params);
    }

    /**
     * 根据 SQL 语句查询指定字段列表（多条记录）
     */
    public static <T> List<T> queryColumnList(String sql, Object... params) {
        return dataAccessor.queryColumnList(sql, params);
    }
    /**
     * 根据 SQL 语句查询指定字段映射（多条记录）
     */
    public static <T> Map<T, Map<String, Object>> queryColumnMap(String column, String sql, Object... params) {
        return dataAccessor.queryColumnMap(column, sql, params);
    }


    /**
     * 根据 SQL 语句查询记录条数
     */
    public static long queryCount(String sql, Object... params) {
        return dataAccessor.queryCount(sql, params);
    }

    /**
     * 执行更新语句（包括：update、insert、delete）
     */
    public static int update(String sql, Object... params) {
        return dataAccessor.update(sql, params);
    }

    /**
     * 执行插入语句，返回插入后的主键
     */
    public static Serializable insertReturnPK(String sql, Object... params) {
        return dataAccessor.insertReturnPK(sql, params);
    }
}
