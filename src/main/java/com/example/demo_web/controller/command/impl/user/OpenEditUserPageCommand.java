package com.example.demo_web.controller.command.impl.user;

import com.example.demo_web.controller.command.*;
import com.example.demo_web.exception.CommandException;
import com.example.demo_web.exception.ServiceException;
import com.example.demo_web.model.entity.User;
import com.example.demo_web.model.service.UserService;
import com.example.demo_web.model.service.impl.UserServiceImpl;
import com.example.demo_web.model.util.message.ErrorMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.Optional;

public class OpenEditUserPageCommand implements ActionCommand {
    private static final Logger logger = LogManager.getLogger();
    private UserService userService = UserServiceImpl.getInstance();

    @Override
    public CommandResult execute(SessionRequestContent sessionRequestContent) throws CommandException {
        CommandResult commandResult = new CommandResult(PagePath.EDIT_USER, TransitionType.REDIRECT);
        String stringUserId = sessionRequestContent.getRequestParameter(RequestParameter.USER_ID);
        if (stringUserId == null) {
            sessionRequestContent.setSessionAttribute(Attribute.ERROR_MESSAGE, ErrorMessage.EMPTY_EDIT_USER_PARAMETERS);
            commandResult.setPage(PagePath.MAIN);
        } else {
            Optional<String> errorMessage;
            Optional<User> user;
            Map.Entry<Optional<User>, Optional<String>> findResult;
            try {
                findResult = userService.findById(stringUserId);
                user = findResult.getKey();
                errorMessage = findResult.getValue();
                if (errorMessage.isPresent()) {
                    sessionRequestContent.setSessionAttribute(Attribute.ERROR_MESSAGE, errorMessage.get());
                    commandResult.setPage(PagePath.MAIN);
                } else {
                    if (user.isPresent()) {
                        sessionRequestContent.setSessionAttribute(Attribute.SOME_USER, user.get());
                    }
                }
            } catch (ServiceException e) {
                logger.error(e);
                throw new CommandException(e);
            }
        }
        return commandResult;
    }
}
