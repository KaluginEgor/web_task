package com.epam.project.model.dao;

import com.epam.project.exception.DaoException;
import com.epam.project.model.entity.User;
import com.epam.project.model.entity.UserState;

import java.util.List;

/**
 * The type Abstract user dao.
 */
public abstract class AbstractUserDao extends AbstractBaseDao<Integer, User> {
    /**
     * Find user by login user.
     *
     * @param login the login
     * @return the user
     * @throws DaoException the dao exception
     */
    public abstract User findUserByLogin(String login) throws DaoException;

    /**
     * Find all between list.
     *
     * @param begin the begin
     * @param end   the end
     * @return the list
     * @throws DaoException the dao exception
     */
    public abstract List<User> findAllBetween(int begin, int end) throws DaoException;

    /**
     * Count users int.
     *
     * @return the int
     * @throws DaoException the dao exception
     */
    public abstract int countUsers() throws DaoException;

    /**
     * Find password by login string.
     *
     * @param login the login
     * @return the string
     * @throws DaoException the dao exception
     */
    public abstract String findPasswordByLogin(String login) throws DaoException;

    /**
     * Create user.
     *
     * @param user              the user
     * @param encryptedPassword the encrypted password
     * @return the user
     * @throws DaoException the dao exception
     */
    public abstract User create(User user, String encryptedPassword) throws DaoException;

    /**
     * Activate user boolean.
     *
     * @param id the id
     * @return the boolean
     * @throws DaoException the dao exception
     */
    public abstract boolean activateUser(int id) throws DaoException;

    /**
     * Block user boolean.
     *
     * @param id the id
     * @return the boolean
     * @throws DaoException the dao exception
     */
    public abstract boolean blockUser(int id) throws DaoException;

    /**
     * Detect state by id user state.
     *
     * @param id the id
     * @return the user state
     * @throws DaoException the dao exception
     */
    public abstract UserState detectStateById(int id) throws DaoException;

    /**
     * Login exists boolean.
     *
     * @param login the login
     * @return the boolean
     * @throws DaoException the dao exception
     */
    public abstract boolean loginExists(String login) throws DaoException;

    /**
     * Id exists boolean.
     *
     * @param id the id
     * @return the boolean
     * @throws DaoException the dao exception
     */
    public abstract boolean idExists(int id) throws DaoException;

    /**
     * Find role by id string.
     *
     * @param id the id
     * @return the string
     * @throws DaoException the dao exception
     */
    public abstract String findRoleById(int id) throws DaoException;

    /**
     * Update user rating.
     *
     * @param userId        the user id
     * @param newUserRating the new user rating
     * @throws DaoException the dao exception
     */
    public abstract void updateUserRating(int userId, int newUserRating) throws DaoException;
}
