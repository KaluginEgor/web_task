package com.example.demo_web.service;

import com.example.demo_web.entity.User;
import com.example.demo_web.exception.ServiceException;

import java.util.Optional;

public interface UserService {
    Optional<User> login(String login, String pass) throws ServiceException;

    Optional<User> register(String login, String email, String password) throws ServiceException;

    boolean isValid(String login, String email, String password) throws ServiceException;
}
