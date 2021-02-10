package com.example.demo_web.command.impl;

import com.example.demo_web.command.*;
import com.example.demo_web.exception.ServiceException;
import com.example.demo_web.manager.ConfigurationManager;
import com.example.demo_web.service.UserService;
import com.example.demo_web.service.impl.UserServiceImpl;

public class ConfirmRegistrationCommand implements ActionCommand {

    private final UserService userService = new UserServiceImpl();

    @Override
    public CommandResult execute(SessionRequestContent sessionRequestContent) {
        CommandResult commandResult = new CommandResult();
        commandResult.setTransitionType(TransitionType.FORWARD);
        String page = null;
        int activationUsersId = Integer.parseInt(sessionRequestContent.getRequestParameter(RequestParameter.PARAM_ID));
        int usersId = (Integer) sessionRequestContent.getSessionAttribute(Attribute.ACTIVATION_USERS_ID);
        try {
            if (usersId == activationUsersId) {
                userService.activateUser(activationUsersId);
                page = ConfigurationManager.getProperty("path.page.main");
            } else {
                page = ConfigurationManager.getProperty("path.page.error");
            }
        } catch (ServiceException e) {
            page = ConfigurationManager.getProperty("path.page.error");
        }
        commandResult.setPage(page);
        return commandResult;
    }
}
