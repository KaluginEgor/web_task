package com.example.demo_web.model.dao;

import com.example.demo_web.model.entity.User;
import com.example.demo_web.exception.DaoException;
import com.example.demo_web.model.entity.UserState;

import java.util.List;
import java.util.Optional;

public abstract class AbstractUserDao extends AbstractBaseDao<Integer, User> {
    public abstract Optional<User> findUserByLogin(String login) throws DaoException;

    public abstract List<User> findAllBetween(int begin, int end) throws DaoException;

    public abstract int countUsers() throws DaoException;

    public abstract Optional<String> findPasswordByLogin(String login) throws DaoException;

    public abstract int create(User user, String encryptedPassword) throws DaoException;

    public abstract boolean activateUser(int id) throws DaoException;

    public abstract boolean blockUser(int id) throws DaoException;

    public abstract UserState detectStateById(int id) throws DaoException;

    public abstract boolean loginExists(String login) throws DaoException;

    public abstract void updateUserRating(int userId, int newUserRating) throws DaoException;
}
