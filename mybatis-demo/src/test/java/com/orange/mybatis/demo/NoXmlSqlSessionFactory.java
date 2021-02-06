package com.orange.mybatis.demo;

import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.log4j.Log4jImpl;
import org.apache.ibatis.logging.log4j2.Log4j2Impl;
import org.apache.ibatis.logging.stdout.StdOutImpl;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.junit.Test;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

public class NoXmlSqlSessionFactory {

    private final static String driver = "com.mysql.cj.jdbc.Driver";
    private final static String url = "jdbc:mysql://47.104.96.20:3306/icip_test?useSSL=false&useUnicode=true&characterEncoding=utf-8&allowMultiQueries=true&useTimezone=true&serverTimezone=GMT%2B8&nullCatalogMeansCurrent=true";
    private final static String username = "85ido";
    private final static String password = "85ido@123";

    @Test
    public void noXml() {
        DataSource dataSource = new PooledDataSource();
        ((PooledDataSource) dataSource).setDriver(driver);
        ((PooledDataSource) dataSource).setUrl(url);
        ((PooledDataSource) dataSource).setUsername(username);
        ((PooledDataSource) dataSource).setPassword(password);

        TransactionFactory transactionFactory = new JdbcTransactionFactory();
        Environment environment = new Environment("development", transactionFactory, dataSource);
        Configuration configuration = new Configuration(environment);

        configuration.setMapUnderscoreToCamelCase(true);
        configuration.setLogImpl(Log4jImpl.class);

        configuration.addMapper(SqlMapper.class);

        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);

        SqlSession session = sqlSessionFactory.openSession();

        SqlMapper sqlMapper = session.getMapper(SqlMapper.class);

        List<Map<String, Object>> list = sqlMapper.selectGroupWithUsers();

        System.out.println(list.size());
    }


}
