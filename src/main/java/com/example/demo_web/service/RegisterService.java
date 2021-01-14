package com.example.demo_web.service;

import com.example.demo_web.dao.UserDao;
import com.example.demo_web.dao.impl.UserDaoImpl;
import com.example.demo_web.exception.DaoException;
import com.example.demo_web.pool.MySqlDataSourceFactory;
import com.example.demo_web.validator.UserValidator;

import java.sql.Connection;

public class RegisterService {
    private final UserDao userDao = new UserDaoImpl();

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
}
