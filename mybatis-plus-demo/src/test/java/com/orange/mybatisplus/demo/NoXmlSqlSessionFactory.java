package com.orange.mybatisplus.demo;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.Transaction;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.junit.Test;

import javax.sql.DataSource;
import java.lang.reflect.Proxy;

import static com.orange.mybatisplus.demo.db.DataSourceUtil.getDataSource;


public class NoXmlSqlSessionFactory {


    @Test
    public void noXml() {

        TransactionFactory transactionFactory = new JdbcTransactionFactory();
        Environment environment = new Environment("development", transactionFactory, getDataSource());

        MybatisConfiguration configuration = new MybatisConfiguration(environment);

        configuration.addMapper(SqlMapper.class);

        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);

        SqlSession session = sqlSessionFactory.openSession();

        SqlMapper sqlMapper = session.getMapper(SqlMapper.class);
        System.out.println(sqlMapper.helloWorld());
        System.out.println(sqlMapper.version());



//        try {
//            Transaction transaction = transactionFactory.newTransaction(dataSource.getConnection());
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }

    @Test
    public void proxy() {
//        MBeanServerInvocationHandler.newProxyInstance()
//        Proxy.newProxyInstance();
    }


}
