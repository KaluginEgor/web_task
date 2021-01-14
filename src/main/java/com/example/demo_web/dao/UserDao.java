package com.example.demo_web.dao;

import com.example.demo_web.entity.User;
import com.example.demo_web.exception.DaoException;

public interface UserDao extends BaseDao<Integer, User> {
    boolean containsLogin(String login) throws DaoException;
    boolean isRegistered(String login, String password) throws DaoException;
}
