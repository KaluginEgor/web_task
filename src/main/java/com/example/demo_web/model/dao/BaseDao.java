package com.example.demo_web.model.dao;

import com.example.demo_web.model.entity.Entity;
import com.example.demo_web.exception.DaoException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public interface BaseDao <K, T extends Entity> {
    List<T> findAll() throws DaoException;
    T findEntityById(K id) throws DaoException;
    boolean delete(K id) throws DaoException;
    T create(T t) throws DaoException;
    T update(T t)  throws DaoException;
    default void close(Statement statement) {
        final Logger logger = LogManager.getLogger(BaseDao.class.getName());
        try {
            if (statement != null) {
                statement.close();
            }
        } catch (SQLException e) {
            logger.error("Cannot close statement.");
        }
    }
    default void close(Connection connection) {
        final Logger logger = LogManager.getLogger(BaseDao.class.getName());
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            logger.error("Cannot close connection.");
        }
    }
}
