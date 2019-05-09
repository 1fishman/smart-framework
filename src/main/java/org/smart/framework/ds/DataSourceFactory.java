package org.smart.framework.ds;

import javax.sql.DataSource;

public interface DataSourceFactory {
    /**
     * 获取数据源
     * @return 数据源
     */
    DataSource getDataSource();
}
