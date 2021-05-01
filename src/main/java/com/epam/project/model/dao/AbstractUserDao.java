package com.epam.project.model.dao;

import com.epam.project.exception.DaoException;
import com.epam.project.model.entity.User;
import com.epam.project.model.entity.UserState;

import java.util.List;

public abstract class AbstractUserDao extends AbstractBaseDao<Integer, User> {
    public abstract User findUserByLogin(String login) throws DaoException;

    public abstract List<User> findAllBetween(int begin, int end) throws DaoException;

    public abstract int countUsers() throws DaoException;

    public abstract String findPasswordByLogin(String login) throws DaoException;

    public abstract User create(User user, String encryptedPassword) throws DaoException;

    public abstract boolean activateUser(int id) throws DaoException;

    public abstract boolean blockUser(int id) throws DaoException;

    public abstract UserState detectStateById(int id) throws DaoException;

    public abstract boolean loginExists(String login) throws DaoException;

    public abstract boolean idExists(int id) throws DaoException;

    public abstract String findRoleById(int id) throws DaoException;

    public abstract void updateUserRating(int userId, int newUserRating) throws DaoException;
}
