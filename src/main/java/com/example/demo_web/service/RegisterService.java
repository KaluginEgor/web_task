package com.example.demo_web.service;

import com.example.demo_web.dao.api.UserDao;
import com.example.demo_web.dao.api.impl.UserDaoImpl;
import com.example.demo_web.entity.User;
import com.example.demo_web.exception.ConnectionException;
import com.example.demo_web.exception.DaoException;
import com.example.demo_web.connection.ConnectionPool;
import com.example.demo_web.validator.UserValidator;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;

public class RegisterService {
    private static final Logger logger = LogManager.getLogger(RegisterService.class);
    private UserDao userDao = UserDaoImpl.getInstance();

    public  RegisterService() {
        Connection connection = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
        } catch (ConnectionException e) {
            logger.error(e);
        }
        userDao.setConnection(connection);
    }

    public boolean isNotRegistered(String login) {
        boolean condition = true;
        try {
            condition = userDao.containsLogin(login);
        } catch (DaoException e) {
            e.printStackTrace();
        }
        return condition;
    }

    public boolean isValidData(String login, String email, String password) {
        return UserValidator.isValidLogin(login) && UserValidator.isValidEmail(email)
                && UserValidator.isValidPassword(password);
    }

    public boolean registerUser(String login, String email, String password) {
        try {
            String passwordHash = DigestUtils.md5Hex(password);
            userDao.create(new User(login, email, passwordHash));
            return true;
        } catch (DaoException e) {
            logger.info("error: " + e);
            return false;
        }
    }
}
