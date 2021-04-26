package com.example.demo_web.controller.command.impl.user;

import com.example.demo_web.controller.command.*;
import com.example.demo_web.exception.ServiceException;
import com.example.demo_web.model.entity.User;
import com.example.demo_web.model.service.UserService;
import com.example.demo_web.model.service.impl.UserServiceImpl;

public class OpenEditUserPageCommand implements ActionCommand {
    private UserService userService = UserServiceImpl.getInstance();

    @Override
    public CommandResult execute(SessionRequestContent sessionRequestContent) {
        CommandResult commandResult = new CommandResult();
        commandResult.setTransitionType(TransitionType.REDIRECT);
        try {
            if (sessionRequestContent.getRequestParameter(RequestParameter.USER_ID) != null) {
                User someUser = userService.findById(sessionRequestContent.getRequestParameter(RequestParameter.USER_ID)).getKey().get();
                sessionRequestContent.setSessionAttribute(Attribute.SOME_USER, someUser);
            }
            commandResult.setPage(PagePath.EDIT_USER);
        } catch (ServiceException e) {
            commandResult.setPage(PagePath.ERROR_404);
        }
        return commandResult;
    }
}
