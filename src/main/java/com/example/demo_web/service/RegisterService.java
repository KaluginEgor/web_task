package com.example.demo_web.service;

import com.example.demo_web.dao.UserDao;
import com.example.demo_web.dao.impl.UserDaoImpl;
import com.example.demo_web.entity.User;
import com.example.demo_web.exception.DaoException;
import com.example.demo_web.pool.MySqlDataSourceFactory;
import com.example.demo_web.util.PasswordHash;
import com.example.demo_web.validator.UserValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;

public class RegisterService {
    private static final Logger logger = LogManager.getLogger(RegisterService.class);
    private UserDao userDao = UserDaoImpl.getInstance();

    public  RegisterService() {
        Connection connection = MySqlDataSourceFactory.getConnection();
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
        UserValidator userValidator = new UserValidator();
        return userValidator.isValidLogin(login) && userValidator.isValidEmail(email)
                && userValidator.isValidPassword(password);
    }

    public boolean registerUser(String login, String email, String password) {
        try {
            String passwordHash = PasswordHash.generatePasswordHash(password);
            userDao.create(new User(login, email, passwordHash));
            return true;
        } catch (DaoException e) {
            logger.info("error: " + e);
            return false;
        } catch (NoSuchAlgorithmException e) {
            logger.info("error: " + e);
            return false;
        } catch (InvalidKeySpecException e) {
            logger.info("error: " + e);
            return false;
        }
    }
}
