package com.orange.mybatisplus.demo.db;

import org.apache.ibatis.datasource.pooled.PooledDataSource;

import javax.sql.DataSource;

public class DataSourceUtil {

    private final static String driver = "com.mysql.cj.jdbc.Driver";
    private final static String url = "jdbc:mysql://47.104.96.20:3306/icip_test?useSSL=false&useUnicode=true&characterEncoding=utf-8&allowMultiQueries=true&useTimezone=true&serverTimezone=GMT%2B8&nullCatalogMeansCurrent=true";
    private final static String username = "85ido";
    private final static String password = "85ido@123";


    public static DataSource getDataSource() {
        DataSource dataSource = new PooledDataSource();
        ((PooledDataSource) dataSource).setDriver(driver);
        ((PooledDataSource) dataSource).setUrl(url);
        ((PooledDataSource) dataSource).setUsername(username);
        ((PooledDataSource) dataSource).setPassword(password);
        return dataSource;
    }
}
