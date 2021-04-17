package com.example.demo_web.controller.command.impl.user;

import com.example.demo_web.controller.command.*;
import com.example.demo_web.exception.ServiceException;
import com.example.demo_web.model.entity.User;
import com.example.demo_web.model.service.UserService;
import com.example.demo_web.model.service.impl.UserServiceImpl;

public class UpdateUserCommand implements ActionCommand {
    private UserService userService = new UserServiceImpl();

    @Override
    public CommandResult execute(SessionRequestContent sessionRequestContent) {
        CommandResult commandResult = new CommandResult();
        commandResult.setTransitionType(TransitionType.REDIRECT);
        int userId = Integer.valueOf(sessionRequestContent.getRequestParameter(RequestParameter.USER_ID));
        String firstName = sessionRequestContent.getRequestParameter(RequestParameter.FIRST_NAME);
        String secondName = sessionRequestContent.getRequestParameter(RequestParameter.SECOND_NAME);
        String email = sessionRequestContent.getRequestParameter(RequestParameter.EMAIL);
        String login = sessionRequestContent.getRequestParameter(RequestParameter.LOGIN);
        String role = sessionRequestContent.getRequestParameter(RequestParameter.USER_ROLE);
        String state = sessionRequestContent.getRequestParameter(RequestParameter.USER_STATE);
        String rating = sessionRequestContent.getRequestParameter(RequestParameter.USER_RATING);
        String picture = sessionRequestContent.getRequestParameter(RequestParameter.PICTURE);
        try {
            User someUser = userService.update(userId, login, email, firstName, secondName, picture, role, state, rating).get();
            sessionRequestContent.setSessionAttribute(Attribute.SOME_USER, someUser);
            commandResult.setPage(PagePath.USER);
        } catch (ServiceException e) {
            commandResult.setPage(PagePath.ERROR);
        }
        return commandResult;
    }
}
