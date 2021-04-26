package com.example.demo_web.controller.command.impl.common.page;


import com.example.demo_web.controller.command.*;
import com.example.demo_web.exception.CommandException;
import com.example.demo_web.model.entity.MediaPerson;
import com.example.demo_web.exception.ServiceException;
import com.example.demo_web.model.service.MediaPersonService;
import com.example.demo_web.model.service.impl.MediaPersonServiceImpl;
import com.example.demo_web.model.util.message.FriendlyMessage;
import com.example.demo_web.tag.ViewAllMediaPersonsTag;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

public class OpenAllMediaPersonsPageCommand implements ActionCommand {
    private static final Logger logger = LogManager.getLogger();
    private MediaPersonService mediaPersonService = MediaPersonServiceImpl.getInstance();

    @Override
    public CommandResult execute(SessionRequestContent sessionRequestContent) throws CommandException {
        CommandResult commandResult = new CommandResult(PagePath.ALL_MEDIA_PERSONS, TransitionType.REDIRECT);

        Integer currentTablePage = (Integer) sessionRequestContent.getSessionAttribute(Attribute.ALL_MEDIA_PERSONS_CURRENT_PAGE);
        Optional<String> currentPage = Optional.ofNullable(sessionRequestContent.getRequestParameter(RequestParameter.NEW_PAGE));
        if (currentPage.isEmpty() && currentTablePage == null) {
            currentTablePage = 1;
        } else if (currentPage.isPresent()) {
            currentTablePage = Integer.parseInt(currentPage.get());
        }
        sessionRequestContent.setSessionAttribute(Attribute.ALL_MEDIA_PERSONS_CURRENT_PAGE, currentTablePage);
        int mediaPersonsNumber = ViewAllMediaPersonsTag.MEDIA_PERSONS_PER_PAGE_NUMBER;
        int start = (currentTablePage - 1) * mediaPersonsNumber;
        int end = mediaPersonsNumber + start;
        try {
            List<MediaPerson> allCurrentMediaPeople = mediaPersonService.findAllBetween(start, end);
            sessionRequestContent.setSessionAttribute(Attribute.ALL_MEDIA_PERSONS_LIST, allCurrentMediaPeople);
            int mediaPersonsCount = mediaPersonService.countMediaPersons();
            sessionRequestContent.setSessionAttribute(Attribute.MEDIA_PERSONS_COUNT, mediaPersonsCount);
            if (allCurrentMediaPeople.size() == 0) {
                sessionRequestContent.setRequestAttribute(Attribute.CONFIRM_MESSAGE, FriendlyMessage.EMPTY_MEDIA_PERSONS_LIST);
            }
        } catch (ServiceException e) {
            logger.error(e);
            throw new CommandException(e);
        }
        return commandResult;
    }
}
