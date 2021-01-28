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
import java.util.List;

public class LoginService {
    private static final Logger logger = LogManager.getLogger(RegisterService.class);
    private UserDao userDao = UserDaoImpl.getInstance();

    public  LoginService() {
        Connection connection = MySqlDataSourceFactory.getConnection();
        userDao.setConnection(connection);
    }

    public boolean isRegistered(String login, String password) {
        boolean condition = true;
        try {
            String passwordHash = PasswordHash.generatePasswordHash(password);
            condition = userDao.isRegistered(login, passwordHash);
        } catch (DaoException e) {
            logger.error(e);
        } catch (NoSuchAlgorithmException e) {
            logger.error(e);
        } catch (InvalidKeySpecException e) {
            logger.error(e);
        }
        return condition;
    }

    public boolean isValidData(String login, String password) {
        UserValidator userValidator = new UserValidator();
        return userValidator.isValidLogin(login) && userValidator.isValidPassword(password);
    }
}
