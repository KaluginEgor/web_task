package com.example.demo_web.model.service;

import com.example.demo_web.controller.command.SessionRequestContent;
import com.example.demo_web.model.entity.User;
import com.example.demo_web.exception.ServiceException;

import java.util.Map;
import java.util.Optional;

public interface UserService {
    Optional<User> login(String login, String pass) throws ServiceException;

    Optional<User> register(String login, String email, String firstName, String secondName, String password) throws ServiceException;

    Optional<User> findById(int id) throws ServiceException;

    boolean activateUser(int id) throws ServiceException;

    Optional<User> update(int id, String login, String email, String firstName, String secondName, String picture, String role, String state, String rating) throws ServiceException;

    void constructAndSendConfirmEmail(String locale, User user);

    Map<String, Boolean> defineIncorrectLoginData(String login, String password) throws ServiceException;

    Map<String, Boolean> defineIncorrectRegistrationData(String login, String email, String firstName, String secondName, String password) throws ServiceException;

    void defineErrorMessageFromDataValidations(SessionRequestContent sessionRequestContent,
                                               Map<String, Boolean> usersDataValidations);
}
