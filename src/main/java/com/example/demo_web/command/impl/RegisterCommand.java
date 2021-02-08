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

public class RegisterCommand implements ActionCommand {
    private static final String DATA_NOT_VALID = "Entered data is not valid.";

    @Override
    public String execute(SessionRequestContent sessionRequestContent) {
        String page = null;
        String email = sessionRequestContent.getRequestParameter(RequestParameter.EMAIL);
        String login = sessionRequestContent.getRequestParameter(RequestParameter.LOGIN);
        String password = sessionRequestContent.getRequestParameter(RequestParameter.PASSWORD);
        String passwordRepeat = sessionRequestContent.getRequestParameter(RequestParameter.PASSWORD_REPEAT);
        UserService userService = new UserServiceImpl();
        Optional<User> user = Optional.empty();

        if (password.equals(passwordRepeat)) {
            try {
                user = userService.register(login, email, password);
                page = ConfigurationManager.getProperty("path.page.login");
            } catch (ServiceException e) {
                if (e.getCause() instanceof DaoException) {
                    page = ConfigurationManager.getProperty("path.page.error");
                } else if (e.getMessage().equals(DATA_NOT_VALID)) {
                    sessionRequestContent.setRequestAttribute("error", "not valid data provided");
                    page = ConfigurationManager.getProperty("path.page.registration");
                } else {
                    sessionRequestContent.setRequestAttribute("error", "user with such login already exists");
                    page = ConfigurationManager.getProperty("path.page.registration");
                }
            }
        } else {
            sessionRequestContent.setRequestAttribute("error", "enter correct repeat password");
            page = ConfigurationManager.getProperty("path.page.registration");
        }

        return page;
    }
}
