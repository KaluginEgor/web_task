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

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class UpdateUserCommand implements ActionCommand {
    private static final Logger logger = LogManager.getLogger();
    private UserService userService = UserServiceImpl.getInstance();
    private static final String DEFAULT_USER_PICTURE = "C:/Epam/pictures/user.jpg";

    @Override
    public CommandResult execute(SessionRequestContent sessionRequestContent) throws CommandException {
        CommandResult commandResult = new CommandResult(PagePath.USER, TransitionType.REDIRECT);
        User userToUpdate = (User) sessionRequestContent.getSessionAttribute(Attribute.SOME_USER);
        String stringUserId = sessionRequestContent.getRequestParameter(RequestParameter.USER_ID);
        String firstName = sessionRequestContent.getRequestParameter(RequestParameter.FIRST_NAME);
        String secondName = sessionRequestContent.getRequestParameter(RequestParameter.SECOND_NAME);
        String email = sessionRequestContent.getRequestParameter(RequestParameter.EMAIL);
        String picture = sessionRequestContent.getRequestParameter(RequestParameter.PICTURE);
        if (picture == null || picture.isEmpty()) {
            picture = DEFAULT_USER_PICTURE;
        }
        if (userToUpdate == null || stringUserId == null || firstName == null || secondName == null || email == null) {
            sessionRequestContent.setSessionAttribute(Attribute.ERROR_MESSAGE, ErrorMessage.EMPTY_UPDATE_USER_PARAMETERS);
            commandResult.setPage(PagePath.MAIN);
        } else {
            Map.Entry<List<String>,List<String>> validationResult = userService.validateUpdateData(email, firstName, secondName, picture);
            List<String> validParameters = validationResult.getKey();
            List<String> errorMessages = validationResult.getValue();
            if (!errorMessages.isEmpty()) {
                sessionRequestContent.setSessionAttribute(Attribute.VALIDATION_ERRORS, errorMessages);
                if (validParameters.contains(Attribute.FIRST_NAME)) sessionRequestContent.setSessionAttribute(
                        Attribute.FIRST_NAME, firstName);
                if (validParameters.contains(Attribute.SECOND_NAME)) sessionRequestContent.setSessionAttribute(
                        Attribute.SECOND_NAME, secondName);
                if (validParameters.contains(Attribute.EMAIL)) sessionRequestContent.setSessionAttribute(
                        Attribute.EMAIL, email);
                if (validParameters.contains(RequestParameter.PICTURE)) sessionRequestContent.setSessionAttribute(
                        Attribute.NEW_PICTURE, picture);
                commandResult.setPage(PagePath.EDIT_USER);
            } else {
                User currentUser = (User) sessionRequestContent.getSessionAttribute(Attribute.USER);
                if (Integer.valueOf(stringUserId).equals(currentUser.getId()) ||
                        (UserRole.ADMIN.equals(currentUser.getRole()) && !UserRole.ADMIN.equals(userToUpdate.getRole()))) {
                    Optional<String> errorMessage;
                    Optional<User> user;
                    Map.Entry<Optional<User>, Optional<String>> findResult;
                    try {
                        findResult = userService.update(stringUserId, email, firstName, secondName, picture);
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
                } else {
                    sessionRequestContent.setSessionAttribute(Attribute.ERROR_MESSAGE, ErrorMessage.ERROR_ACCESS);
                    commandResult.setPage(PagePath.MAIN);
                }
            }
        }
        return commandResult;
    }
}
