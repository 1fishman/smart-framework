package org.smart.framework.ds.impl;

import org.apache.commons.dbcp2.BasicDataSource;

/**
 * 默认数据源工厂
 * 基于Apache DBCP实现
 */
public class DefaultDataSourceFactory extends AbstracDataSourceFactory<BasicDataSource> {
    @Override
    public BasicDataSource createDataSource() {
        return new BasicDataSource();
    }

    @Override
    public void setDriver(BasicDataSource ds, String driver) {
        ds.setDriverClassName(driver);
    }

    @Override
    public void setUrl(BasicDataSource ds, String url) {
        ds.setUrl(url);
    }

    @Override
    public void setUsername(BasicDataSource ds, String url) {
        ds.setUsername(username);
    }

    @Override
    public void setPassword(BasicDataSource ds, String password) {
        ds.setPassword(password);
    }

    @Override
    public void setAdvanceConfig(BasicDataSource ds) {
        //解决java.sql.SQLException: Already closed. 的问题(连接池会自动关闭长时间没有使用的连接)
        ds.setValidationQuery("select 1 from dual");
    }
}
