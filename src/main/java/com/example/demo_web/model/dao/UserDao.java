package com.example.demo_web.model.dao;

import com.example.demo_web.model.entity.User;
import com.example.demo_web.exception.DaoException;

import java.util.Optional;

public interface UserDao extends BaseDao<Integer, User> {
    Optional<User> findUserByLogin(String login) throws DaoException;

    Optional<String> findPasswordByLogin(String login) throws DaoException;

    int create(User user, String encryptedPassword) throws DaoException;

    boolean activateUser(int id) throws DaoException;

    boolean loginExists(String login) throws DaoException;

    void updateUserRating(int userId, int newUserRating) throws DaoException;
}
