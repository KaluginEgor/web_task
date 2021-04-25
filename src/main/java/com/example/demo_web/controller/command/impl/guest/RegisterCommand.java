package com.example.demo_web.controller.command.impl.guest;


import com.example.demo_web.controller.command.*;
import com.example.demo_web.model.entity.User;
import com.example.demo_web.exception.ServiceException;
import com.example.demo_web.model.service.UserService;
import com.example.demo_web.model.service.impl.UserServiceImpl;

import java.util.Map;
import java.util.Optional;

public class RegisterCommand implements ActionCommand {
    private UserService userService = new UserServiceImpl();

    @Override
    public CommandResult execute(SessionRequestContent sessionRequestContent) {
        String email = sessionRequestContent.getRequestParameter(RequestParameter.EMAIL);
        String login = sessionRequestContent.getRequestParameter(RequestParameter.LOGIN);
        String firstName = sessionRequestContent.getRequestParameter(RequestParameter.FIRST_NAME);
        String secondName = sessionRequestContent.getRequestParameter(RequestParameter.SECOND_NAME);
        String password = sessionRequestContent.getRequestParameter(RequestParameter.PASSWORD);
        String passwordRepeat = sessionRequestContent.getRequestParameter(RequestParameter.PASSWORD_REPEAT);
        Optional<User> registeredUser = Optional.empty();
        CommandResult commandResult = new CommandResult();
        commandResult.setTransitionType(TransitionType.REDIRECT);

        if (password.equals(passwordRepeat)) {
            try {
                Map<String,Boolean> usersDataValidations = userService.defineIncorrectRegistrationData(login, email, firstName, secondName, password);
                if (usersDataValidations.containsValue(Boolean.FALSE)) {
                    userService.defineErrorMessageFromDataValidations(sessionRequestContent, usersDataValidations);
                    commandResult.setPage(PagePath.REGISTRATION);
                } else {
                    registeredUser = userService.register(login, email, firstName, secondName, password);
                    sessionRequestContent.setSessionAttribute(Attribute.ACTIVATION_USER_ID, registeredUser.get().getId());
                    String locale = (String) sessionRequestContent.getSessionAttribute(Attribute.LANG);
                    userService.constructAndSendConfirmEmail(locale, registeredUser.get());
                    commandResult.setPage(PagePath.LOGIN);
                    sessionRequestContent.setRequestAttribute(RequestParameter.ERROR_MESSAGE, ErrorMessage.CONFIRM_MESSAGE_SENT);
                }
            } catch (ServiceException e) {
                commandResult.setPage(PagePath.ERROR);
            }
        } else {
            sessionRequestContent.setRequestAttribute(RequestParameter.ERROR_MESSAGE, ErrorMessage.REPEAT_PASSWORD_INCORRECT_MESSAGE);
            commandResult.setPage(PagePath.REGISTRATION);
        }

        return commandResult;
    }
}
