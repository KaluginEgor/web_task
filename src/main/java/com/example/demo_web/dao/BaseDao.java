package com.example.demo_web.dao;

import com.example.demo_web.entity.Entity;
import com.example.demo_web.exception.DaoException;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public interface BaseDao <K, T extends Entity> {
    List<T> findAll() throws DaoException;
    T findEntityById(K id) throws DaoException;
    boolean delete(K id) throws DaoException;
    boolean delete(T t) throws DaoException;
    boolean create(T t) throws DaoException;
    T update(T t)  throws DaoException;
    boolean setConnection(Connection connection);
    default void close(Statement statement) {
        try {
            if (statement != null) {
                statement.close();
            }
        } catch (SQLException e) {
            //log
        }
    }
    default void close(Connection connection) {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            //log
        }
    }
}
