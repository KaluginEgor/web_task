package com.example.demo_web.controller.command.impl.user;

import com.example.demo_web.controller.command.*;
import com.example.demo_web.exception.ServiceException;
import com.example.demo_web.model.entity.Movie;
import com.example.demo_web.model.entity.User;
import com.example.demo_web.model.service.UserService;
import com.example.demo_web.model.service.impl.UserServiceImpl;

public class DeleteUserCommand implements ActionCommand {
    UserService userService = new UserServiceImpl();
    private static final String USER = "USER";

    @Override
    public CommandResult execute(SessionRequestContent sessionRequestContent) {
        CommandResult commandResult = new CommandResult();
        commandResult.setTransitionType(TransitionType.REDIRECT);
        try {
            String userId = sessionRequestContent.getRequestParameter(RequestParameter.USER_ID);
            String userRole = sessionRequestContent.getRequestParameter(RequestParameter.USER_ROLE);
            userService.delete(userId);
            if (userRole.equals(USER)) {
                sessionRequestContent.invalidate();
                commandResult.setPage(PagePath.ALL_MOVIES);
            } else {
                commandResult.setPage(PagePath.LOGIN);
            }
        } catch (ServiceException e) {
            commandResult.setPage(PagePath.ERROR);
        }
        return commandResult;
    }
}
