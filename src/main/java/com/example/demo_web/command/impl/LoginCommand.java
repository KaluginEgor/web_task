package com.example.demo_web.command.impl;

import com.example.demo_web.command.*;
import com.example.demo_web.entity.User;
import com.example.demo_web.exception.DaoException;
import com.example.demo_web.exception.ServiceException;
import com.example.demo_web.service.UserService;
import com.example.demo_web.service.impl.UserServiceImpl;

import java.util.Optional;

public class LoginCommand implements ActionCommand {
    private final UserService userService = new UserServiceImpl();


    @Override
    public CommandResult execute(SessionRequestContent sessionRequestContent) {
        String login = sessionRequestContent.getRequestParameter(RequestParameter.LOGIN);
        String password = sessionRequestContent.getRequestParameter(RequestParameter.PASSWORD);
        Optional<User> user = Optional.empty();
        CommandResult commandResult = new CommandResult();
        commandResult.setTransitionType(TransitionType.FORWARD);

        try {
            user = userService.login(login, password);
            if (user.isPresent()) {
                commandResult.setPage(PagePath.MAIN);
            } else {
                sessionRequestContent.setRequestAttribute(RequestParameter.ERROR_MESSAGE, ErrorMessage.LOGIN_OR_PASSWORD_INCORRECT_ERROR_MESSAGE);
                commandResult.setPage(PagePath.LOGIN);
            }
        } catch (ServiceException e) {
            if (e.getCause() instanceof DaoException) {
                commandResult.setPage(PagePath.ERROR);
            } else {
                sessionRequestContent.setRequestAttribute(RequestParameter.ERROR_MESSAGE, "not valid data provided");
                commandResult.setPage(PagePath.LOGIN);
            }
        }
        return commandResult;
    }
}