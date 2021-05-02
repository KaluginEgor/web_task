package com.epam.project.model.service;

import com.epam.project.exception.ServiceException;
import com.epam.project.model.entity.User;
import com.epam.project.model.entity.UserState;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * The interface User service.
 */
public interface UserService {
    /**
     * Login map . entry.
     *
     * @param login the login
     * @param pass  the pass
     * @return the map . entry
     * @throws ServiceException the service exception
     */
    Map.Entry<Optional<User>, Optional<String>> login(String login, String pass) throws ServiceException;

    /**
     * Find all between list.
     *
     * @param begin the begin
     * @param end   the end
     * @return the list
     * @throws ServiceException the service exception
     */
    List<User> findAllBetween(int begin, int end) throws ServiceException;

    /**
     * Count users int.
     *
     * @return the int
     * @throws ServiceException the service exception
     */
    int countUsers() throws ServiceException;

    /**
     * Register map . entry.
     *
     * @param login      the login
     * @param email      the email
     * @param firstName  the first name
     * @param secondName the second name
     * @param password   the password
     * @return the map . entry
     * @throws ServiceException the service exception
     */
    Map.Entry<Optional<User>, Optional<String>> register(String login, String email, String firstName, String secondName, String password) throws ServiceException;

    /**
     * Find by id map . entry.
     *
     * @param stringUserId the string user id
     * @return the map . entry
     * @throws ServiceException the service exception
     */
    Map.Entry<Optional<User>, Optional<String>> findById(String stringUserId) throws ServiceException;

    /**
     * Activate optional.
     *
     * @param stringId the string id
     * @return the optional
     * @throws ServiceException the service exception
     */
    Optional<String> activate(String stringId) throws ServiceException;

    /**
     * Update map . entry.
     *
     * @param stringId   the string id
     * @param email      the email
     * @param firstName  the first name
     * @param secondName the second name
     * @param picture    the picture
     * @return the map . entry
     * @throws ServiceException the service exception
     */
    Map.Entry<Optional<User>, Optional<String>> update(String stringId, String email, String firstName, String secondName, String picture) throws ServiceException;

    /**
     * Delete optional.
     *
     * @param stringId the string id
     * @return the optional
     * @throws ServiceException the service exception
     */
    Optional<String> delete(String stringId) throws ServiceException;

    /**
     * Block optional.
     *
     * @param stringId the string id
     * @return the optional
     * @throws ServiceException the service exception
     */
    Optional<String> block(String stringId) throws ServiceException;

    /**
     * Detect state by id user state.
     *
     * @param id the id
     * @return the user state
     * @throws ServiceException the service exception
     */
    UserState detectStateById(int id) throws ServiceException;

    /**
     * Id exists boolean.
     *
     * @param id the id
     * @return the boolean
     * @throws ServiceException the service exception
     */
    boolean idExists(int id) throws ServiceException;

    /**
     * Construct and send confirm email.
     *
     * @param locale the locale
     * @param user   the user
     */
    void constructAndSendConfirmEmail(String locale, User user);

    /**
     * Validate update data map . entry.
     *
     * @param email      the email
     * @param firstName  the first name
     * @param secondName the second name
     * @param picture    the picture
     * @return the map . entry
     */
    Map.Entry<List<String>, List<String>> validateUpdateData(String email, String firstName, String secondName, String picture);

    /**
     * Validate registration data map . entry.
     *
     * @param login      the login
     * @param email      the email
     * @param firstName  the first name
     * @param secondName the second name
     * @param password   the password
     * @return the map . entry
     */
    Map.Entry<List<String>, List<String>> validateRegistrationData(String login, String email, String firstName,
                                                                   String secondName, String password);

    /**
     * Validate login data map . entry.
     *
     * @param login    the login
     * @param password the password
     * @return the map . entry
     */
    Map.Entry<List<String>, List<String>> validateLoginData(String login, String password);
}
