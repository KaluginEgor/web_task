package com.epam.project.controller.command.impl.user;


import com.epam.project.controller.command.*;
import com.epam.project.exception.CommandException;
import com.epam.project.exception.ServiceException;
import com.epam.project.model.service.UserService;
import com.epam.project.model.service.impl.UserServiceImpl;
import com.epam.project.model.util.message.ErrorMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class ConfirmRegistrationCommand implements ActionCommand {
    private static final Logger logger = LogManager.getLogger();
    private final UserService userService = UserServiceImpl.getInstance();

    @Override
    public CommandResult execute(SessionRequestContent sessionRequestContent) throws CommandException {
        CommandResult commandResult = new CommandResult(PagePath.LOGIN, TransitionType.REDIRECT);
        String stringUserId = sessionRequestContent.getRequestParameter(RequestParameter.ID);
        String stringActivationUserId = (String) sessionRequestContent.getSessionAttribute(Attribute.ACTIVATION_USER_ID);

        if (stringUserId == null || stringActivationUserId == null) {
            sessionRequestContent.setSessionAttribute(Attribute.ERROR_MESSAGE, ErrorMessage.EMPTY_ACTIVATION_PARAMETERS);
            commandResult.setPage(PagePath.MAIN);
        } else {
            try {
                Optional<String> errorMessage;
                if (stringUserId.equals(stringActivationUserId)) {
                    errorMessage = userService.activate(stringActivationUserId);
                    if (errorMessage.isPresent()) {
                        sessionRequestContent.setSessionAttribute(Attribute.ERROR_MESSAGE, errorMessage.get());
                        commandResult.setPage(PagePath.MAIN);
                    } else {
                        sessionRequestContent.removeSessionAttribute(Attribute.ACTIVATION_USER_ID);
                    }
                } else {
                    commandResult.setPage(PagePath.ERROR_404);
                }
            } catch (ServiceException e) {
                logger.error(e);
                throw new CommandException(e);
            }
        }
        return commandResult;
    }
}
