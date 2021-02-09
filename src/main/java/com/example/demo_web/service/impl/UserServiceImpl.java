package com.example.demo_web.service.impl;

import com.example.demo_web.builder.BaseBuilder;
import com.example.demo_web.builder.impl.UserBuilder;
import com.example.demo_web.connection.ConnectionPool;
import com.example.demo_web.dao.api.UserDao;
import com.example.demo_web.dao.api.impl.UserDaoImpl;
import com.example.demo_web.entity.User;
import com.example.demo_web.exception.ConnectionException;
import com.example.demo_web.exception.DaoException;
import com.example.demo_web.exception.ServiceException;
import com.example.demo_web.service.UserService;
import com.example.demo_web.validator.UserValidator;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.util.Optional;

public class UserServiceImpl implements UserService {
    private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);
    private final UserDao userDao = UserDaoImpl.getInstance();

    public UserServiceImpl() {
        Connection connection = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
        } catch (ConnectionException e) {
            logger.error(e);
        }
        userDao.setConnection(connection);
    }

    @Override
    public Optional<User> login(String login, String password) throws ServiceException {
        Optional<User> foundUser = Optional.empty();
        if (UserValidator.isValidLogin(login) && UserValidator.isValidPassword(password)) {
            try {
                String encryptedPassword = DigestUtils.md5Hex(password);
                if (userDao.loginExists(login)) {
                    Optional<String> passwordFromDB = userDao.findPasswordByLogin(login);
                    if (passwordFromDB.isPresent()) {
                        if (passwordFromDB.get().equals(encryptedPassword)) {
                            foundUser = userDao.findUserByLogin(login);
                        }
                    }
                } else {
                    throw new ServiceException("No such login");
                }
            } catch (DaoException e) {
                throw new ServiceException(e.getMessage(), e);
            }
        } else {
            logger.error("Login or password is not valid.");
            throw new ServiceException("Login or password is not valid.");
        }
        return foundUser;
    }

    @Override
    public Optional<User> register(String login, String email, String firstName, String secondName, String password) throws ServiceException {
        Optional<User> registeredUser = Optional.empty();
        User user = new User(login, email, firstName, secondName);

        if (isValid(login, email, firstName, secondName, password)) {
            try {
                String encryptedPassword = DigestUtils.md5Hex(password);
                if (userDao.loginExists(login)) {
                    logger.info("This login already exists: ", login);
                    throw new ServiceException("Login already exists.");
                } else {
                    BaseBuilder<User> builder = new UserBuilder();
                    builder.setDefaultFields(user);
                    int userId = userDao.create(user, encryptedPassword);
                    user.setId(userId);
                    registeredUser = Optional.of(user);
                    logger.info("User {} created.", registeredUser);
                }
            } catch (DaoException e) {
                throw new ServiceException(e.getMessage(), e);
            }
        } else {
            logger.error("Entered data is not valid.");
            throw new ServiceException("Entered data is not valid.");
        }
        return registeredUser;
    }

    @Override
    public boolean isValid(String login, String email, String firstName, String secondName, String password) throws ServiceException {
        return UserValidator.isValidLogin(login) && UserValidator.isValidEmail(email)
                && UserValidator.isValidName(firstName) && UserValidator.isValidName(secondName)
                && UserValidator.isValidPassword(password);
    }
}
