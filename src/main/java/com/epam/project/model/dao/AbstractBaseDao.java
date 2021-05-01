package com.epam.project.model.dao;

import com.epam.project.exception.DaoException;
import com.epam.project.model.entity.Entity;
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

    protected boolean idExists(Integer value, String sqlQuery) throws DaoException {
        boolean result;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setInt(1, value);
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                resultSet.next();
                result = resultSet.getInt(1) != 0;
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return result;
    }

}
