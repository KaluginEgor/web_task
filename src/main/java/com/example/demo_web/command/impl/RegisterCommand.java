package com.example.demo_web.command.impl;

import com.example.demo_web.command.*;
import com.example.demo_web.entity.User;
import com.example.demo_web.exception.DaoException;
import com.example.demo_web.exception.ServiceException;
import com.example.demo_web.manager.ConfigurationManager;
import com.example.demo_web.service.UserService;
import com.example.demo_web.service.impl.UserServiceImpl;

import java.util.Optional;

public class RegisterCommand implements ActionCommand {
    private static final String DATA_NOT_VALID = "Entered data is not valid.";

    @Override
    public CommandResult execute(SessionRequestContent sessionRequestContent) {
        String page = null;
        String email = sessionRequestContent.getRequestParameter(RequestParameter.EMAIL);
        String login = sessionRequestContent.getRequestParameter(RequestParameter.LOGIN);
        String firstName = sessionRequestContent.getRequestParameter(RequestParameter.FIRST_NAME);
        String secondName = sessionRequestContent.getRequestParameter(RequestParameter.SECOND_NAME);
        String password = sessionRequestContent.getRequestParameter(RequestParameter.PASSWORD);
        String passwordRepeat = sessionRequestContent.getRequestParameter(RequestParameter.PASSWORD_REPEAT);
        UserService userService = new UserServiceImpl();
        Optional<User> registeredUser = Optional.empty();
        CommandResult commandResult = new CommandResult();
        commandResult.setTransitionType(TransitionType.FORWARD);

        if (password.equals(passwordRepeat)) {
            try {
                registeredUser = userService.register(login, email, firstName, secondName, password);
                sessionRequestContent.setSessionAttribute(Attribute.ACTIVATION_USERS_ID, registeredUser.get().getId());
                String locale = (String) sessionRequestContent.getSessionAttribute(Attribute.LANG);
                userService.constructAndSendConfirmEmail(locale, registeredUser.get());
                page = ConfigurationManager.getProperty("path.page.login");
                sessionRequestContent.setRequestAttribute("error", "confirmation message sent to your email");
            } catch (ServiceException e) {
                if (e.getCause() instanceof DaoException) {
                    page = ConfigurationManager.getProperty("path.page.error");
                } else if (e.getMessage().equals(DATA_NOT_VALID)) {
                    sessionRequestContent.setRequestAttribute("error", "not valid data provided");
                    page = ConfigurationManager.getProperty("path.page.registration");
                } else {
                    sessionRequestContent.setRequestAttribute("error", "registeredUser with such login already exists");
                    page = ConfigurationManager.getProperty("path.page.registration");
                }
            }
        } else {
            sessionRequestContent.setRequestAttribute("error", "enter correct repeat password");
            page = ConfigurationManager.getProperty("path.page.registration");
        }

        commandResult.setPage(page);
        return commandResult;
    }
}
