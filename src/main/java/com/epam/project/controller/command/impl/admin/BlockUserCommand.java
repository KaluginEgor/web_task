package com.epam.project.controller.command.impl.admin;

import com.epam.project.controller.command.*;
import com.epam.project.exception.CommandException;
import com.epam.project.exception.ServiceException;
import com.epam.project.model.service.UserService;
import com.epam.project.model.service.impl.UserServiceImpl;
import com.epam.project.model.util.message.ErrorMessage;
import com.epam.project.model.util.message.FriendlyMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class BlockUserCommand implements ActionCommand {
    private static final Logger logger = LogManager.getLogger();
    private UserService userService = UserServiceImpl.getInstance();

    @Override
    public CommandResult execute(SessionRequestContent sessionRequestContent) throws CommandException {
        CommandResult commandResult = new CommandResult(PagePath.ALL_USERS, TransitionType.REDIRECT);
        String stringUserId = sessionRequestContent.getRequestParameter(RequestParameter.USER_ID);
        if (stringUserId == null) {
            sessionRequestContent.setSessionAttribute(Attribute.ERROR_MESSAGE, ErrorMessage.EMPTY_BLOCK_PARAMETERS);
        } else {
            Optional<String> errorMessage;
            try {
                errorMessage = userService.block(stringUserId);
                if (errorMessage.isEmpty()) {
                    sessionRequestContent.setSessionAttribute(Attribute.CONFIRM_MESSAGE, FriendlyMessage.BLOCK_USER_CORRECT);
                } else {
                    sessionRequestContent.setSessionAttribute(Attribute.ERROR_MESSAGE, errorMessage.get());
                }
            } catch (ServiceException e) {
                logger.error(e);
                throw new CommandException(e);
            }
        }
        return commandResult;
    }
}
