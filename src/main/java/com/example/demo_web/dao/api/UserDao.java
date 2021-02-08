package com.example.demo_web.dao.api;

import com.example.demo_web.dao.api.BaseDao;
import com.example.demo_web.entity.User;
import com.example.demo_web.exception.DaoException;

import java.util.Optional;

public interface UserDao extends BaseDao<Integer, User> {
    boolean containsLogin(String login) throws DaoException;
    boolean isRegistered(String login, String password) throws DaoException;
    Optional<User> findByLoginAndPassword(String email, String password) throws DaoException;
}
