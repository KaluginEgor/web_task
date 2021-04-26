package com.example.demo_web.controller.command.impl.user;


import com.example.demo_web.controller.command.*;
import com.example.demo_web.exception.ServiceException;
import com.example.demo_web.model.service.UserService;
import com.example.demo_web.model.service.impl.UserServiceImpl;

public class ConfirmRegistrationCommand implements ActionCommand {

    private final UserService userService = UserServiceImpl.getInstance();

    @Override
    public CommandResult execute(SessionRequestContent sessionRequestContent) {
        CommandResult commandResult = new CommandResult();
        commandResult.setTransitionType(TransitionType.REDIRECT);
        String stringUserId = (String) sessionRequestContent.getRequestParameter(RequestParameter.ID);
        String stringActivationUserId = (String) sessionRequestContent.getSessionAttribute(Attribute.ACTIVATION_USER_ID);
        try {
            if (stringUserId.equals(stringActivationUserId)) {
                userService.activate(stringActivationUserId);
                sessionRequestContent.setSessionAttribute(Attribute.ACTIVATION_USER_ID, "");
                commandResult.setPage(PagePath.ALL_MOVIES);
            } else {
                commandResult.setPage(PagePath.ERROR_404);
            }
        } catch (ServiceException e) {
            commandResult.setPage(PagePath.ERROR_404);
        }
        return commandResult;
    }
}
