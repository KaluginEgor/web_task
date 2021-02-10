package com.example.demo_web.dao.api;

import com.example.demo_web.dao.api.BaseDao;
import com.example.demo_web.entity.User;
import com.example.demo_web.exception.DaoException;

import java.util.Optional;

public interface UserDao extends BaseDao<Integer, User> {
    Optional<User> findUserByLogin(String login) throws DaoException;

    boolean isRegistered(String login, String password) throws DaoException;

    Optional<String> findPasswordByLogin(String login) throws DaoException;

    int create(User user, String encryptedPassword) throws DaoException;

    boolean activateUser(int id) throws DaoException;

    boolean loginExists(String login) throws DaoException;
}
