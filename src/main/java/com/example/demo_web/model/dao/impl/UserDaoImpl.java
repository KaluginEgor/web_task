package com.example.demo_web.model.dao.impl;

import com.example.demo_web.model.dao.column.UsersColumn;
import com.example.demo_web.model.entity.UserRole;
import com.example.demo_web.model.entity.UserState;
import com.example.demo_web.model.pool.ConnectionPool;
import com.example.demo_web.model.dao.UserDao;
import com.example.demo_web.model.entity.User;
import com.example.demo_web.exception.ConnectionException;
import com.example.demo_web.exception.DaoException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class UserDaoImpl implements UserDao {
    private static final String SQL_CREATE_USER = "INSERT INTO users (user_login, user_email, user_password, user_first_name, user_second_name,  user_role_id, user_state_id) VALUES (?, ?, ?, ?, ?, ?, ?);";

    private static final String SQL_SELECT_USER_BY_LOGIN = "SELECT U.user_id, U.user_login, U.user_email, U.user_first_name, U.user_second_name, U.user_picture, UR.user_role_name, US.user_state_name FROM users U INNER JOIN user_roles UR ON U.user_role_id = UR.user_role_id INNER JOIN user_states US ON U.user_state_id = US.user_state_id WHERE U.user_login = ?;";

    private static final String SQL_SELECT_USER_BY_ID = "SELECT U.user_id, U.user_login, U.user_email, U.user_first_name, U.user_second_name, U.user_picture, UR.user_role_name, US.user_state_name FROM users U INNER JOIN user_roles UR ON U.user_role_id = UR.user_role_id INNER JOIN user_states US ON U.user_state_id = US.user_state_id WHERE U.user_id = ?;";

    private static final String SQL_SELECT_PASSWORD_BY_LOGIN = "SELECT user_password FROM users WHERE user_login = ?;";

    private static final String SQL_LOGIN_EXISTS = "SELECT EXISTS(SELECT user_login FROM users WHERE user_login = ?) AS user_existence;";

    private static final String SQL_ACTIVATE_USER = "UPDATE users SET user_state_id = 1 WHERE user_id = ?;";

    private static final String SQL_UPDATE_USER_RATING = "UPDATE users SET user_rating = ? WHERE user_id = ?";

    private static final String SQL_UPDATE_USER = "UPDATE users U SET U.user_login = ?, U.user_email = ?, U.user_first_name = ?, U.user_second_name = ?, U.user_picture = ?, U.user_role_id = ?, U.user_state_id = ?, U.user_rating = ? WHERE U.user_id = ?;";

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
    public User findEntityById(Integer id) throws DaoException {
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_USER_BY_ID)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return buildUser(resultSet);
                }
            }
            return null;
        } catch (SQLException | ConnectionException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public boolean delete(Integer id) {
        return false;
    }

    @Override
    public User create(User user) throws DaoException {
        return null;
    }

    @Override
    public int create(User user, String encryptedPassword) throws DaoException {
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_CREATE_USER, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, user.getLogin());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, encryptedPassword);
            preparedStatement.setString(4, user.getFirstName());
            preparedStatement.setString(5, user.getSecondName());
            preparedStatement.setInt(6, user.getState().ordinal());
            preparedStatement.setInt(7, user.getRole().ordinal());
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            resultSet.next();
            return resultSet.getInt(1);
        } catch (SQLException | ConnectionException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public User update(User user) throws DaoException {
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE_USER)) {
            preparedStatement.setString(1, user.getLogin());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getFirstName());
            preparedStatement.setString(4, user.getSecondName());
            preparedStatement.setString(5, user.getPicture());
            preparedStatement.setInt(6, user.getRole().ordinal());
            preparedStatement.setInt(7, user.getState().ordinal());
            preparedStatement.setInt(8, user.getRating());
            preparedStatement.setInt(9, user.getId());
            preparedStatement.executeUpdate();
            return user;
        } catch (SQLException | ConnectionException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Optional<User> findUserByLogin(String login) throws DaoException {
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_USER_BY_LOGIN)) {
            preparedStatement.setString(1, login);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(buildUser(resultSet));
                }
            }
            return Optional.empty();
        } catch (SQLException | ConnectionException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Optional<String> findPasswordByLogin(String login) throws DaoException {
        Optional<String> foundPassword = Optional.empty();
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_PASSWORD_BY_LOGIN)) {
            preparedStatement.setString(1, login);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                foundPassword = Optional.ofNullable(resultSet.getString("user_password"));
            }
        } catch (SQLException | ConnectionException e) {
            throw new DaoException(e);
        }
        return foundPassword;
    }

    @Override
    public boolean loginExists(String login) throws DaoException {
        boolean result = false;
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_LOGIN_EXISTS)) {
            preparedStatement.setString(1, login);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                result = resultSet.getInt(1) != 0;
            }
        } catch (SQLException | ConnectionException e) {
            throw new DaoException(e);
        }
        return result;
    }

    @Override
    public boolean activateUser(int id) throws DaoException {
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_ACTIVATE_USER)) {
            statement.setInt(1, id);
            return (statement.executeUpdate() == 1);
        } catch (SQLException | ConnectionException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void updateUserRating(int userId, int newUserRating) throws DaoException {
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE_USER_RATING)) {
            preparedStatement.setInt(1, newUserRating);
            preparedStatement.setInt(2, userId);
            preparedStatement.executeUpdate();
        } catch (SQLException | ConnectionException e) {
            throw new DaoException(e);
        }
    }

    private User buildUser(ResultSet resultSet) throws SQLException {
        User user = new User();
        if (resultSet.wasNull()) {
            return null;
        }
        Integer userId = resultSet.getInt(UsersColumn.ID);
        user.setId(userId);
        String userLogin = resultSet.getString(UsersColumn.LOGIN);
        user.setLogin(userLogin);
        String userEmail = resultSet.getString(UsersColumn.EMAIL);
        user.setEmail(userEmail);
        String userFirstName = resultSet.getString(UsersColumn.FIRST_NAME);
        user.setFirstName(userFirstName);
        String userSecondName = resultSet.getString(UsersColumn.SECOND_NAME);
        user.setSecondName(userSecondName);
        String picture = resultSet.getString(UsersColumn.PICTURE);
        user.setPicture(picture);
        UserRole userRole = UserRole.valueOf(resultSet.getString(UsersColumn.ROLE_ID).toUpperCase(Locale.ROOT));
        user.setRole(userRole);
        UserState userState = UserState.valueOf(resultSet.getString(UsersColumn.STATE_ID).toUpperCase(Locale.ROOT));
        user.setState(userState);
        return user;
    }
}
