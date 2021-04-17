package com.example.demo_web.controller.command.impl;


import com.example.demo_web.controller.command.*;
import com.example.demo_web.model.entity.User;
import com.example.demo_web.exception.ServiceException;
import com.example.demo_web.model.service.UserService;
import com.example.demo_web.model.service.impl.UserServiceImpl;

import java.util.Map;
import java.util.Optional;

public class LoginCommand implements ActionCommand {
    private final UserService userService = new UserServiceImpl();


    @Override
    public CommandResult execute(SessionRequestContent sessionRequestContent) {
        String login = sessionRequestContent.getRequestParameter(RequestParameter.LOGIN);
        String password = sessionRequestContent.getRequestParameter(RequestParameter.PASSWORD);
        Optional<User> user = Optional.empty();
        CommandResult commandResult = new CommandResult();
        commandResult.setTransitionType(TransitionType.REDIRECT);

        sessionRequestContent.setRequestAttribute(RequestParameter.LOGIN, login);

        try {
            Map<String,Boolean> usersDataValidations = userService.defineIncorrectLoginData(login, password);
            if (!usersDataValidations.containsValue(Boolean.FALSE)) {
                user = userService.login(login, password);
                if (user.isPresent()) {
                    commandResult.setPage(PagePath.MAIN);
                    sessionRequestContent.setSessionAttribute(Attribute.USER, user.get());
                }
            } else {
                userService.defineErrorMessageFromDataValidations(sessionRequestContent, usersDataValidations);
                commandResult.setPage(PagePath.LOGIN);
            }
        } catch (ServiceException e) {
            commandResult.setPage(PagePath.ERROR);
        }
        return commandResult;
    }
}