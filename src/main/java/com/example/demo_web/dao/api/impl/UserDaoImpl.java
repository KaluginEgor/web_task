package com.example.demo_web.dao.api.impl;

import com.example.demo_web.builder.Builder;
import com.example.demo_web.builder.impl.UserBuilder;
import com.example.demo_web.dao.api.UserDao;
import com.example.demo_web.entity.User;
import com.example.demo_web.exception.DaoException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDaoImpl implements UserDao {
    private static final String SQL_CREATE = "INSERT INTO users (login, email, password) VALUES (?, ?, ?)";
    private static final String SQL_CHECK_USER_LOGIN = "SELECT users.login FROM users WHERE users.login LIKE ?";
    private static final String SQL_CHECK_USER_REGISTERED = "SELECT users.login, users.password FROM users WHERE users.login LIKE ? AND users.password LIKE ?";
    private static final String SQL_FIND_USER_BY_EMAIL_AND_PASSWORD = "SELECT login, email, password FROM users WHERE login = ? AND password = ?";

    private Connection connection;
    private static UserDao instance = new UserDaoImpl();

    private UserDaoImpl(){}

    public static UserDao getInstance() {
        return instance;
    }

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
            throw new DaoException("SQl exception: " + e);
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
            throw new DaoException("SQl exception: " + e);
        }
    }

    @Override
    public Optional<User> findByLoginAndPassword(String login, String password) throws DaoException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_USER_BY_EMAIL_AND_PASSWORD)) {
            preparedStatement.setString(1, login);
            preparedStatement.setString(2, password);
            try (ResultSet resultSet = preparedStatement.executeQuery()){
                if (resultSet.next()) {
                    Builder<User> builder = new UserBuilder();
                    return Optional.of(builder.build(resultSet));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public boolean setConnection(Connection connection) {
        this.connection = connection;
        return true;
    }
}
