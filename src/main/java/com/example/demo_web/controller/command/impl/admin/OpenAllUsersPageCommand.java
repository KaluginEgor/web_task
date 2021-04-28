package com.example.demo_web.controller.command.impl.admin;

import com.example.demo_web.controller.command.*;
import com.example.demo_web.exception.CommandException;
import com.example.demo_web.exception.ServiceException;
import com.example.demo_web.model.entity.User;
import com.example.demo_web.model.service.UserService;
import com.example.demo_web.model.service.impl.UserServiceImpl;
import com.example.demo_web.model.util.message.FriendlyMessage;
import com.example.demo_web.tag.ViewAllUsersTag;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

public class OpenAllUsersPageCommand implements ActionCommand {
    private static final Logger logger = LogManager.getLogger();
    private UserService userService = UserServiceImpl.getInstance();

    @Override
    public CommandResult execute(SessionRequestContent sessionRequestContent) throws CommandException {
        CommandResult commandResult = new CommandResult(PagePath.ALL_USERS, TransitionType.REDIRECT);

        Integer currentTablePage = (Integer) sessionRequestContent.getSessionAttribute(Attribute.ALL_USERS_CURRENT_PAGE);
        Optional<String> currentPage = Optional.ofNullable(sessionRequestContent.getRequestParameter(RequestParameter.NEW_PAGE));
        if (currentPage.isEmpty() && currentTablePage == null) {
            currentTablePage = 1;
        } else if (currentPage.isPresent()) {
            currentTablePage = Integer.parseInt(currentPage.get());
        }
        sessionRequestContent.setSessionAttribute(Attribute.ALL_USERS_CURRENT_PAGE, currentTablePage);
        int usersNumber = ViewAllUsersTag.USERS_PER_PAGE_NUMBER;
        int start = (currentTablePage - 1) * usersNumber;
        int end = usersNumber + start;
        try {
            List<User> allCurrentUsers = userService.findAllBetween(start, end);
            sessionRequestContent.setSessionAttribute(Attribute.ALL_USERS_LIST, allCurrentUsers);
            int usersCount = userService.countUsers();
            sessionRequestContent.setSessionAttribute(Attribute.USERS_COUNT, usersCount);
            if (allCurrentUsers.size() == 0) {
                sessionRequestContent.setRequestAttribute(Attribute.CONFIRM_MESSAGE, FriendlyMessage.EMPTY_USER_LIST);
            }
        } catch (ServiceException e) {
            logger.error(e);
            throw new CommandException(e);
        }
        return commandResult;
    }
}
