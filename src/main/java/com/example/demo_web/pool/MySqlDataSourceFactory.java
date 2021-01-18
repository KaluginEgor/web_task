package com.example.demo_web.pool;

import com.mysql.cj.jdbc.MysqlDataSource;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class MySqlDataSourceFactory {
    public static MysqlDataSource createMysqlDataSource(){
        MysqlDataSource dataSource = null;
        Properties props = new Properties();
        dataSource = new MysqlDataSource();
        dataSource.setURL("jdbc:mysql://localhost:3306/db_test");
        dataSource.setUser("root");
        dataSource.setPassword("root");
        return dataSource;
    }
    public static Connection getConnection() {
        Connection connection = null;
        try{
            connection = createMysqlDataSource().getConnection();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return connection;
    }
}
