package com.epam.project.model.service;

import com.epam.project.exception.ServiceException;
import com.epam.project.model.entity.User;
import com.epam.project.model.entity.UserState;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserService {
    Map.Entry<Optional<User>, Optional<String>> login(String login, String pass) throws ServiceException;

    List<User> findAllBetween(int begin, int end) throws ServiceException;

    int countUsers() throws ServiceException;

    Map.Entry<Optional<User>, Optional<String>> register(String login, String email, String firstName, String secondName, String password) throws ServiceException;

    Map.Entry<Optional<User>, Optional<String>> findById(String stringUserId) throws ServiceException;

    Optional<String> activate(String stringId) throws ServiceException;

    Map.Entry<Optional<User>, Optional<String>> update(String stringId, String email, String firstName, String secondName, String picture) throws ServiceException;

    Optional<String> delete(String stringId) throws ServiceException;

    Optional<String> block(String stringId) throws ServiceException;

    UserState detectStateById(int id) throws ServiceException;

    boolean idExists(int id) throws ServiceException;

    void constructAndSendConfirmEmail(String locale, User user);

    Map.Entry<List<String>, List<String>> validateUpdateData(String email, String firstName, String secondName, String picture);

    Map.Entry<List<String>, List<String>> validateRegistrationData(String login, String email, String firstName,
                                                                   String secondName, String password);

    Map.Entry<List<String>, List<String>> validateLoginData(String login, String password);
}
