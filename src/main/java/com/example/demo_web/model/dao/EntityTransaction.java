package com.example.demo_web.model.dao;

import com.example.demo_web.exception.ConnectionException;
import com.example.demo_web.exception.DaoException;
import com.example.demo_web.model.pool.ConnectionPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;

public class EntityTransaction {
    private Connection connection;
    private static Logger logger = LogManager.getLogger(EntityTransaction.class.getName());

    public EntityTransaction(){}

    public void initTransaction(AbstractBaseDao ... daos) {
        try {
            if (connection == null) {
                connection = ConnectionPool.getInstance().getConnection();
            }
            connection.setAutoCommit(false);
            for(AbstractBaseDao dao : daos){
                dao.setConnection(connection);
            }
        } catch (SQLException | ConnectionException e) {
            logger.error(e);
        }
    }

    public void endTransaction() {
        try {
            if (connection != null) {
                connection.setAutoCommit(true);
                connection.close();
            }
        } catch (SQLException e) {
            logger.error(e);
        }
        connection = null;
    }

    public void commit() {
        try {
            connection.commit();
        } catch (SQLException e) {
            logger.error(e);
        }
    }

    public void rollback() {
        try {
            connection.rollback();
        } catch (SQLException e) {
            logger.error(e);
        }
    }

    public void init(AbstractBaseDao dao) {
        if (connection == null) {
            try {
                connection = ConnectionPool.getInstance().getConnection();
            } catch (ConnectionException e) {
                logger.error(e);
            }
        }
        dao.setConnection(connection);
    }
    public void end() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                logger.error(e);
            }
        }
        connection = null;
    }
}
