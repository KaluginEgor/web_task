package com.example.demo_web.service;

import com.example.demo_web.dao.UserDao;
import com.example.demo_web.dao.impl.UserDaoImpl;
import com.example.demo_web.entity.User;
import com.example.demo_web.exception.DaoException;
import com.example.demo_web.pool.MySqlDataSourceFactory;
import com.example.demo_web.validator.UserValidator;

import java.sql.Connection;
import java.util.List;

public class LoginService {
    private UserDao userDao = UserDaoImpl.getInstance();

    public  LoginService() {
        Connection connection = MySqlDataSourceFactory.getConnection();
        userDao.setConnection(connection);
    }

    public boolean isRegistered(String login, String password) {
        boolean condition = true;
        try {
            condition = userDao.isRegistered(login, password);
        } catch (DaoException e) {
            e.printStackTrace();
        }
        return condition;
    }

    public boolean isValidData(String login, String password) {
        UserValidator userValidator = new UserValidator();
        return userValidator.isValidLogin(login) && userValidator.isValidPassword(password);
    }
}
