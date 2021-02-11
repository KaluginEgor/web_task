package com.example.demo_web.service;

import com.example.demo_web.command.SessionRequestContent;
import com.example.demo_web.entity.User;
import com.example.demo_web.exception.ServiceException;

import java.util.Map;
import java.util.Optional;

public interface UserService {
    Optional<User> login(String login, String pass) throws ServiceException;

    Optional<User> register(String login, String email, String firstName, String secondName, String password) throws ServiceException;

    boolean isValid(String login, String email, String firstName, String secondName, String password) throws ServiceException;

    boolean activateUser(int id) throws ServiceException;

    void constructAndSendConfirmEmail(String locale, User user);

    Map<String, Boolean> defineIncorrectData(String login, String email, String firstName, String secondName, String password) throws ServiceException;

    void defineErrorMessageFromUsersDataValidations(SessionRequestContent sessionRequestContent,
                                                                  Map<String, Boolean> usersDataValidations);
}
