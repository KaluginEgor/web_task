package com.example.demo_web.pool;

import com.mysql.cj.jdbc.MysqlDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class MySqlDataSourceFactory {
    public static MysqlDataSource createMysqlDataSource(){
        MysqlDataSource dataSource = null;
        Properties prop = new Properties();
        prop.put("url", "jdbc:mysql://localhost:3306/db_test");
        prop.put("user", "root");
        prop.put("password", "root");
        prop.put("autoReconnect", "true");
        prop.put("characterEncoding", "UTF-8");
        prop.put("useUnicode", "true");
        dataSource = new MysqlDataSource();
        dataSource.setURL(prop.getProperty("url"));
        dataSource.setUser(prop.getProperty("user"));
        dataSource.setPassword(prop.getProperty("password"));
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
