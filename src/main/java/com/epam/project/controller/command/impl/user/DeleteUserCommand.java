package com.epam.project.controller.command.impl.user;

import com.epam.project.controller.command.*;
import com.epam.project.exception.CommandException;
import com.epam.project.exception.ServiceException;
import com.epam.project.model.entity.User;
import com.epam.project.model.entity.UserRole;
import com.epam.project.model.service.UserService;
import com.epam.project.model.service.impl.UserServiceImpl;
import com.epam.project.model.util.message.ErrorMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class DeleteUserCommand implements ActionCommand {
    private static final Logger logger = LogManager.getLogger();
    UserService userService = UserServiceImpl.getInstance();

    @Override
    public CommandResult execute(SessionRequestContent sessionRequestContent) throws CommandException {
        CommandResult commandResult = new CommandResult();
        commandResult.setTransitionType(TransitionType.REDIRECT);
        String stringUserId = sessionRequestContent.getRequestParameter(RequestParameter.USER_ID);
        if (stringUserId == null) {
            sessionRequestContent.setSessionAttribute(Attribute.ERROR_MESSAGE, ErrorMessage.EMPTY_UPDATE_USER_PARAMETERS);
            commandResult.setPage(PagePath.MAIN);
        } else {
            User currentUser = (User) sessionRequestContent.getSessionAttribute(Attribute.USER);
            if (UserRole.ADMIN.equals(currentUser.getRole()) || Integer.valueOf(stringUserId).equals(currentUser.getId())) {
                Optional<String> errorMessage;
                try {
                    errorMessage = userService.delete(stringUserId);
                    if (errorMessage.isPresent()) {
                        sessionRequestContent.setSessionAttribute(Attribute.ERROR_MESSAGE, errorMessage.get());
                        commandResult.setPage(PagePath.MAIN);
                    } else {
                        if (UserRole.USER.equals(currentUser.getRole())) {
                            sessionRequestContent.invalidate();
                            commandResult.setPage(PagePath.INDEX);
                        } else {
                            sessionRequestContent.removeSessionAttribute(Attribute.SOME_USER);
                            commandResult.setPage(PagePath.INDEX);
                        }
                    }
                } catch (ServiceException e) {
                    logger.error(e);
                    throw new CommandException(e);
                }
            } else {
                sessionRequestContent.setSessionAttribute(Attribute.ERROR_MESSAGE, ErrorMessage.ERROR_ACCESS);
                commandResult.setPage(PagePath.MAIN);
            }
        }
        return commandResult;
    }
}
