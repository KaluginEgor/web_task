package com.epam.project.controller.command.impl.guest;


import com.epam.project.controller.command.*;
import com.epam.project.exception.CommandException;
import com.epam.project.exception.ServiceException;
import com.epam.project.model.entity.User;
import com.epam.project.model.service.UserService;
import com.epam.project.model.service.impl.UserServiceImpl;
import com.epam.project.model.util.message.ErrorMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * The type Login command.
 */
public class LoginCommand implements ActionCommand {
    private static final Logger logger = LogManager.getLogger();
    private final UserService userService = UserServiceImpl.getInstance();

    @Override
    public CommandResult execute(SessionRequestContent sessionRequestContent) throws CommandException {
        CommandResult commandResult = new CommandResult(PagePath.USER, TransitionType.REDIRECT);
        String login = sessionRequestContent.getRequestParameter(RequestParameter.LOGIN);
        String password = sessionRequestContent.getRequestParameter(RequestParameter.PASSWORD);
        if (login == null || password == null) {
            sessionRequestContent.setSessionAttribute(Attribute.ERROR_MESSAGE, ErrorMessage.EMPTY_LOGIN_USER_PARAMETERS);
            commandResult.setPage(PagePath.LOGIN);
        } else {
            Map.Entry<List<String>, List<String>> validationResult = userService.validateLoginData(login, password);
            List<String> validParameters = validationResult.getKey();
            List<String> errorMessages = validationResult.getValue();
            if (!errorMessages.isEmpty()) {
                sessionRequestContent.setSessionAttribute(Attribute.VALIDATION_ERRORS, errorMessages);
                if (validParameters.contains(Attribute.LOGIN)) sessionRequestContent.setSessionAttribute(
                        Attribute.LOGIN, login);
                commandResult.setPage(PagePath.LOGIN);
            } else {
                User currentUser = (User) sessionRequestContent.getSessionAttribute(Attribute.USER);
                if (currentUser.getId() != 0) {
                    sessionRequestContent.setSessionAttribute(Attribute.ERROR_MESSAGE, ErrorMessage.ALREADY_LOGGED_IN);
                    commandResult.setPage(PagePath.MAIN);
                } else {
                    Optional<String> errorMessage;
                    Optional<User> user;
                    Map.Entry<Optional<User>, Optional<String>> findResult;
                    try {
                        findResult = userService.login(login, password);
                        user = findResult.getKey();
                        errorMessage = findResult.getValue();
                        if (errorMessage.isPresent()) {
                            sessionRequestContent.setSessionAttribute(Attribute.ERROR_MESSAGE, errorMessage.get());
                            sessionRequestContent.setSessionAttribute(Attribute.LOGIN, login);
                            commandResult.setPage(PagePath.LOGIN);
                        } else {
                            if (user.isPresent()) {
                                sessionRequestContent.setSessionAttribute(Attribute.USER, user.get());
                                sessionRequestContent.setSessionAttribute(Attribute.SOME_USER, user.get());
                                commandResult.setPage(PagePath.USER);
                            }
                        }
                    } catch (ServiceException e) {
                        logger.error(e);
                        throw new CommandException(e);
                    }
                }
            }
        }
        return commandResult;
    }
}