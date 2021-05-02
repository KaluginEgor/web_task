package com.epam.project.controller.command.impl.guest;


import com.epam.project.controller.command.*;
import com.epam.project.exception.CommandException;
import com.epam.project.exception.ServiceException;
import com.epam.project.model.entity.User;
import com.epam.project.model.service.UserService;
import com.epam.project.model.service.impl.UserServiceImpl;
import com.epam.project.model.util.message.ErrorMessage;
import com.epam.project.model.util.message.FriendlyMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * The type Register command.
 */
public class RegisterCommand implements ActionCommand {
    private static final Logger logger = LogManager.getLogger();
    private UserService userService = UserServiceImpl.getInstance();

    @Override
    public CommandResult execute(SessionRequestContent sessionRequestContent) throws CommandException {
        CommandResult commandResult = new CommandResult(PagePath.LOGIN, TransitionType.REDIRECT);
        String email = sessionRequestContent.getRequestParameter(RequestParameter.EMAIL);
        String login = sessionRequestContent.getRequestParameter(RequestParameter.LOGIN);
        String firstName = sessionRequestContent.getRequestParameter(RequestParameter.FIRST_NAME);
        String secondName = sessionRequestContent.getRequestParameter(RequestParameter.SECOND_NAME);
        String password = sessionRequestContent.getRequestParameter(RequestParameter.PASSWORD);
        String passwordRepeat = sessionRequestContent.getRequestParameter(RequestParameter.PASSWORD_REPEAT);

        if (email == null || login == null || firstName == null || secondName == null || password == null || password == null) {
            sessionRequestContent.setSessionAttribute(Attribute.ERROR_MESSAGE, ErrorMessage.EMPTY_REGISTER_USER_PARAMETERS);
            commandResult.setPage(PagePath.REGISTRATION);
        } else {
            if (password.equals(passwordRepeat)) {
                Map.Entry<List<String>, List<String>> validationResult = userService.validateRegistrationData(
                        login, email, firstName, secondName, password);
                List<String> validParameters = validationResult.getKey();
                List<String> errorMessages = validationResult.getValue();
                if (!errorMessages.isEmpty()) {
                    sessionRequestContent.setSessionAttribute(Attribute.VALIDATION_ERRORS, errorMessages);
                    if (validParameters.contains(Attribute.EMAIL)) sessionRequestContent.setSessionAttribute(
                            Attribute.EMAIL, email);
                    if (validParameters.contains(Attribute.LOGIN)) sessionRequestContent.setSessionAttribute(
                            Attribute.LOGIN, login);
                    if (validParameters.contains(Attribute.FIRST_NAME)) sessionRequestContent.setSessionAttribute(
                            Attribute.FIRST_NAME, firstName);
                    if (validParameters.contains(Attribute.SECOND_NAME)) sessionRequestContent.setSessionAttribute(
                            Attribute.SOME_USER, secondName);
                    commandResult.setPage(PagePath.REGISTRATION);
                } else {
                    User currentUser = (User) sessionRequestContent.getSessionAttribute(Attribute.USER);
                    if (currentUser.getId() != 0) {
                        sessionRequestContent.setSessionAttribute(Attribute.ERROR_MESSAGE, ErrorMessage.ALREADY_REGISTERED);
                        commandResult.setPage(PagePath.MAIN);
                    } else {
                        Optional<String> errorMessage;
                        Optional<User> user;
                        Map.Entry<Optional<User>, Optional<String>> findResult;
                        try {
                            findResult = userService.register(login, email, firstName, secondName, password);
                            user = findResult.getKey();
                            errorMessage = findResult.getValue();
                            if (errorMessage.isPresent()) {
                                sessionRequestContent.setSessionAttribute(Attribute.ERROR_MESSAGE, errorMessage.get());
                                sessionRequestContent.setSessionAttribute(Attribute.EMAIL, email);
                                sessionRequestContent.setSessionAttribute(Attribute.FIRST_NAME, firstName);
                                sessionRequestContent.setSessionAttribute(Attribute.SECOND_NAME, secondName);
                                commandResult.setPage(PagePath.REGISTRATION);
                            } else {
                                if (user.isPresent()) {
                                    sessionRequestContent.setSessionAttribute(Attribute.ACTIVATION_USER_ID, Integer.toString(user.get().getId()));
                                    String lang = sessionRequestContent.extractLocale();
                                    userService.constructAndSendConfirmEmail(lang, user.get());
                                    sessionRequestContent.setRequestAttribute(Attribute.CONFIRM_MESSAGE, FriendlyMessage.CONFIRM_MESSAGE_SENT);
                                }
                            }
                        } catch (ServiceException e) {
                            logger.error(e);
                            throw new CommandException(e);
                        }
                    }
                }
            } else {
                sessionRequestContent.setRequestAttribute(Attribute.ERROR_MESSAGE, ErrorMessage.REPEAT_PASSWORD_INCORRECT);
                commandResult.setPage(PagePath.REGISTRATION);
            }
        }

        return commandResult;
    }
}
