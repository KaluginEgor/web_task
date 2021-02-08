package com.example.demo_web.command.impl;

import com.example.demo_web.command.ActionCommand;
import com.example.demo_web.command.RequestParameter;
import com.example.demo_web.command.SessionRequestContent;
import com.example.demo_web.entity.User;
import com.example.demo_web.exception.DaoException;
import com.example.demo_web.exception.ServiceException;
import com.example.demo_web.manager.ConfigurationManager;
import com.example.demo_web.service.UserService;
import com.example.demo_web.service.impl.UserServiceImpl;

import java.util.Optional;

public class LoginCommand implements ActionCommand {
    private final UserService userService = new UserServiceImpl();


    @Override
    public String execute(SessionRequestContent sessionRequestContent) {
        String page = null;
        String login = sessionRequestContent.getRequestParameter(RequestParameter.LOGIN);
        String password = sessionRequestContent.getRequestParameter(RequestParameter.PASSWORD);
        UserService userService = new UserServiceImpl();
        Optional<User> user = Optional.empty();

        try {
            user = userService.login(login, password);
            if (user.isPresent()) {
                page = ConfigurationManager.getProperty("path.page.main");
            } else {
                sessionRequestContent.setRequestAttribute("error", "wrong login or password");
                page = ConfigurationManager.getProperty("path.page.login");
            }
        } catch (ServiceException e) {
            if (e.getCause() instanceof DaoException) {
                page = ConfigurationManager.getProperty("path.page.error");
            } else {
                sessionRequestContent.setRequestAttribute("error", "not valid data provided");
                page = ConfigurationManager.getProperty("path.page.login");
            }
        }
        return page;
    }
}