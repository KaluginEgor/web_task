package com.example.demo_web.dao.impl;

import com.example.demo_web.dao.BaseDao;
import com.example.demo_web.dao.UserDao;
import com.example.demo_web.entity.Entity;
import com.example.demo_web.entity.User;
import com.example.demo_web.exception.DaoException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl implements UserDao {
    private static final String SQL_CREATE = "INSERT INTO users (login, email, password) VALUES (?, ?, ?)";
    private final static String SQL_CHECK_USER_LOGIN = "SELECT users.login FROM users WHERE users.login LIKE ?";
    private final static String SQL_CHECK_USER_REGISTERED = "SELECT users.login, users.password FROM users WHERE users.login LIKE ? AND users.password LIKE ?";
    private Connection connection;


    @Override
    public List<User> findAll()  throws DaoException {
        return new ArrayList<>();
    }

    @Override
    public User findEntityById(Integer id) {
        return null;
    }

    @Override
    public boolean delete(Integer id) {
        return false;
    }

    @Override
    public boolean delete(User user) {
        return true;
    }

    @Override
    public boolean create(User user) throws DaoException{
        PreparedStatement statement;
        try {
            statement = connection.prepareStatement(SQL_CREATE);
            statement.setString(1, user.getLogin());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPassword());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("SQl exception " + e);
        }
        return true;
    }

    @Override
    public User update(User user) {
        return null;
    }

    @Override
    public boolean containsLogin(String login) throws DaoException {
        PreparedStatement statement;
        try {
            statement = connection.prepareStatement(SQL_CHECK_USER_LOGIN);
            statement.setString(1, login);
            ResultSet resultSet = statement.executeQuery();
            return !resultSet.next();
        } catch (SQLException e) {
            throw new DaoException("SQl exception " + e);
        }
    }

    @Override
    public boolean isRegistered(String login, String password) throws DaoException {
        PreparedStatement statement;
        try {
            statement = connection.prepareStatement(SQL_CHECK_USER_REGISTERED);
            statement.setString(1, login);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            throw new DaoException("SQl exception " + e);
        }
    }

    @Override
    public boolean setConnection(Connection connection) {
        this.connection = connection;
        return true;
    }
}
