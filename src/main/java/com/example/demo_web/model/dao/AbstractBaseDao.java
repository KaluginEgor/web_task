package com.example.demo_web.model.dao;

import com.example.demo_web.exception.DaoException;
import com.example.demo_web.model.entity.Entity;
import com.example.demo_web.model.pool.ProxyConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.List;

public abstract class AbstractBaseDao<K, T extends Entity> {
    protected Connection connection;
    private static final int ONE_ROW_COUNT = 1;
    private static final int GENERATED_ID_ROW_NUMBER = 1;

    public abstract List<T> findAll() throws DaoException;
    public abstract T findEntityById(K id) throws DaoException;
    public abstract boolean delete(K id) throws DaoException;
    public abstract T create(T t) throws DaoException;
    public abstract T update(T t)  throws DaoException;

    public void setConnection(Connection connection) {
        if (this.connection != null) {
            close(this.connection);
        }
        this.connection = connection;
    }

    protected void close(Statement statement) {
        final Logger logger = LogManager.getLogger(AbstractBaseDao.class.getName());
        try {
            if (statement != null) {
                statement.close();
            }
        } catch (SQLException e) {
            logger.error("Cannot close statement.");
        }
    }

    protected void close(Connection connection) {
        final Logger logger = LogManager.getLogger(AbstractBaseDao.class.getName());
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            logger.error("Cannot close connection.");
        }
    }

    protected int executeUpdateAndGetGeneratedId(PreparedStatement statement) throws DaoException {
        try {
            int updatedRowCount = statement.executeUpdate();
            if (updatedRowCount == ONE_ROW_COUNT) {
                try (ResultSet generatedId = statement.getGeneratedKeys()) {
                    if (generatedId.next()) {
                        return generatedId.getInt(GENERATED_ID_ROW_NUMBER);
                    }
                }
            }
            throw new DaoException("Updating row error: 0 rows were updated");
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    protected boolean updateEntity(PreparedStatement statement) throws DaoException {
        try {
            int updatedRows = statement.executeUpdate();
            if (updatedRows != 1) {
                return false;
            }
        } catch (SQLException e) {
            throw new DaoException("Updating entity error", e);
        }
        return true;
    }

    protected boolean updateEntityById(int id, String sqlQuery) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            statement.setInt(1, id);
            int updatedRows = statement.executeUpdate();
            if (updatedRows != 1) {
                return false;
            }
        } catch (SQLException e) {
            throw new DaoException("Updating entity by id error", e);
        }
        return true;
    }

}
