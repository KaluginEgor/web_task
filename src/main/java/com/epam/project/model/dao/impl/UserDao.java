package com.epam.project.model.dao.impl;

import com.epam.project.exception.DaoException;
import com.epam.project.model.dao.AbstractUserDao;
import com.epam.project.model.dao.column.UsersColumn;
import com.epam.project.model.entity.User;
import com.epam.project.model.entity.UserRole;
import com.epam.project.model.entity.UserState;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class UserDao extends AbstractUserDao {
    private static final String SQL_INSERT_USER = "INSERT INTO users (user_login, user_email, user_password, user_first_name, user_second_name,  user_role_id, user_state_id, user_rating, user_picture) VALUES (?, ?, ?, ?, ?, ?, ?);";

    private static final String SQL_SELECT_USER_BY_LOGIN = "SELECT U.user_id, U.user_login, U.user_email, U.user_first_name, U.user_second_name, U.user_picture, UR.user_role_name, US.user_state_name FROM users U INNER JOIN user_roles UR ON U.user_role_id = UR.user_role_id INNER JOIN user_states US ON U.user_state_id = US.user_state_id WHERE U.user_login = ?;";

    private static final String SQL_SELECT_USER_BY_ID = "SELECT U.user_id, U.user_login, U.user_email, U.user_first_name, U.user_second_name, U.user_picture, UR.user_role_name, US.user_state_name FROM users U INNER JOIN user_roles UR ON U.user_role_id = UR.user_role_id INNER JOIN user_states US ON U.user_state_id = US.user_state_id WHERE U.user_id = ?;";

    private static final String SQL_SELECT_PASSWORD_BY_LOGIN = "SELECT user_password FROM users WHERE user_login = ?;";

    private static final String SQL_LOGIN_EXISTS = "SELECT EXISTS(SELECT user_login FROM users WHERE user_login = ?) AS user_existence;";

    private static final String SQL_ACTIVATE_USER = "UPDATE users SET user_state_id = 0 WHERE user_id = ?;";

    private static final String SQL_UPDATE_USER_RATING = "UPDATE users SET user_rating = ? WHERE user_id = ?";

    private static final String SQL_UPDATE_USER = "UPDATE users U SET U.user_login = ?, U.user_email = ?, U.user_first_name = ?, U.user_second_name = ?, U.user_picture = ?, U.user_role_id = ?, U.user_state_id = ?, U.user_rating = ? WHERE U.user_id = ?;";

    private static final String SQL_DELETE_USER = "DELETE FROM users U WHERE U.user_id = ?;";

    private static final String SQL_COUNT_USERS = "SELECT COUNT(*) AS users_count FROM users;";

    private static final String SQL_SELECT_ALL_USERS_WITH_LIMIT = "SELECT U.user_id, U.user_login, U.user_email, U.user_first_name, U.user_second_name, U.user_picture, UR.user_role_name, US.user_state_name FROM users U INNER JOIN user_roles UR ON U.user_role_id = UR.user_role_id INNER JOIN user_states US ON U.user_state_id = US.user_state_id LIMIT ?,?;";

    private static final String SQL_BLOCK_USER = "UPDATE users SET user_state_id = 2 WHERE user_id = ?;";

    private static final String SQL_SELECT_USER_STATUS_BY_ID = "SELECT US.user_state_name FROM users U INNER JOIN user_states US ON U.user_state_id = US.user_state_id WHERE U.user_id = ?;";

    private static final String SQL_SELECT_ROLE_BY_ID = "SELECT UR.user_role_name FROM users U INNER JOIN user_roles UR ON U.user_role_id = UR.user_role_id WHERE U.user_id = ?;";

    private static final String SQL_EXISTS_ID = "SELECT EXISTS (SELECT user_id FROM users WHERE user_id = ?) AS user_existence;";

    private static final AbstractUserDao instance = new UserDao();

    private UserDao(){}

    public static AbstractUserDao getInstance() {
        return instance;
    }

    @Override
    public User findEntityById(Integer id) throws DaoException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_USER_BY_ID)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                resultSet.next();
                return buildUser(resultSet);
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public List<User> findAllBetween(int begin, int end) throws DaoException {
        List<User> userList = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_ALL_USERS_WITH_LIMIT)) {
            preparedStatement.setInt(1, begin);
            preparedStatement.setInt(2, end);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                User user;
                while (resultSet.next()) {
                    user = buildUser(resultSet);
                    userList.add(user);
                }
            }
            return userList;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public int countUsers() throws DaoException {
        int usersCount = 0;
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_COUNT_USERS)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    usersCount = resultSet.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return usersCount;
    }

    @Override
    public boolean delete(Integer id) throws DaoException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE_USER)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public User create(User user, String encryptedPassword) throws DaoException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT_USER, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, user.getLogin());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, encryptedPassword);
            preparedStatement.setString(4, user.getFirstName());
            preparedStatement.setString(5, user.getSecondName());
            preparedStatement.setInt(6, user.getState().ordinal());
            preparedStatement.setInt(7, user.getRole().ordinal());
            preparedStatement.setInt(8, user.getRating());
            preparedStatement.setString(9, user.getPicture());
            int id = executeUpdateAndGetGeneratedId(preparedStatement);
            user.setId(id);
            return user;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public User update(User user) throws DaoException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE_USER)) {
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
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public UserState detectStateById(int id) throws DaoException {
        UserState userState;
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_USER_STATUS_BY_ID)) {
            preparedStatement.setInt(1, id);
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                resultSet.next();
                userState = UserState.valueOf(resultSet.getString(UsersColumn.STATE_ID));
            }
        } catch (SQLException e) {
            //logger.error(e);
            throw new DaoException(e);
        }
        return userState;
    }

    @Override
    public User findUserByLogin(String login) throws DaoException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_USER_BY_LOGIN)) {
            preparedStatement.setString(1, login);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                resultSet.next();
                return buildUser(resultSet);
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public String findPasswordByLogin(String login) throws DaoException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_PASSWORD_BY_LOGIN)) {
            preparedStatement.setString(1, login);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                resultSet.next();
                return resultSet.getString(1);
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public boolean loginExists(String login) throws DaoException {
        boolean result = false;
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_LOGIN_EXISTS)) {
            preparedStatement.setString(1, login);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    result = resultSet.getInt(1) != 0;
                }
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return result;
    }

    @Override
    public boolean blockUser(int id) throws DaoException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_BLOCK_USER)) {
            preparedStatement.setInt(1, id);
            return (preparedStatement.executeUpdate() == 1);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public boolean activateUser(int id) throws DaoException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_ACTIVATE_USER)) {
            preparedStatement.setInt(1, id);
            return (preparedStatement.executeUpdate() == 1);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public boolean idExists(int id) throws DaoException {
        return idExists(id, SQL_EXISTS_ID);
    }

    @Override
    public void updateUserRating(int userId, int newUserRating) throws DaoException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE_USER_RATING)) {
            preparedStatement.setInt(1, newUserRating);
            preparedStatement.setInt(2, userId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public String findRoleById(int id) throws DaoException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_ROLE_BY_ID)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                resultSet.next();
                return resultSet.getString(1);
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public List<User> findAll()  throws DaoException {
        throw new UnsupportedOperationException();
    }

    @Override
    public User create(User user) throws DaoException {
        throw new UnsupportedOperationException();
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
