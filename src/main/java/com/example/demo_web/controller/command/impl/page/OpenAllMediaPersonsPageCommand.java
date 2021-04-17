package com.example.demo_web.controller.command.impl.page;


import com.example.demo_web.controller.command.*;
import com.example.demo_web.model.entity.MediaPerson;
import com.example.demo_web.exception.ServiceException;
import com.example.demo_web.model.service.MediaPersonService;
import com.example.demo_web.model.service.impl.MediaPersonServiceImpl;
import com.example.demo_web.tag.ViewAllMediaPersonsTag;

import java.util.List;
import java.util.Optional;

public class OpenAllMediaPersonsPageCommand implements ActionCommand {
    @Override
    public CommandResult execute(SessionRequestContent sessionRequestContent) {
        CommandResult commandResult = new CommandResult();
        commandResult.setTransitionType(TransitionType.FORWARD);

        Integer currentTablePage = (Integer) sessionRequestContent.getSessionAttribute(Attribute.ALL_ACTORS_CURRENT_PAGE);
        Optional<String> currentPage = Optional.ofNullable(sessionRequestContent.getRequestParameter(RequestParameter.NEW_PAGE));
        if (currentPage.isEmpty() && currentTablePage == null) {
            currentTablePage = 1;
        } else if (currentPage.isPresent()) {
            currentTablePage = Integer.parseInt(currentPage.get());
        }
        sessionRequestContent.setSessionAttribute(Attribute.ALL_ACTORS_CURRENT_PAGE, currentTablePage);
        int actorsNumber = ViewAllMediaPersonsTag.ACTORS_PER_PAGE_NUMBER;
        int start = (currentTablePage - 1) * actorsNumber;
        int end = actorsNumber + start;
        MediaPersonService mediaPersonService = new MediaPersonServiceImpl();
        try {
            List<MediaPerson> allCurrentMediaPeople = mediaPersonService.findAllBetween(start, end);
            sessionRequestContent.setSessionAttribute(Attribute.ALL_ACTORS_LIST, allCurrentMediaPeople);
            int actorsCount = mediaPersonService.countMediaPersons();
            sessionRequestContent.setSessionAttribute(Attribute.ACTORS_COUNT, actorsCount);
            if (allCurrentMediaPeople.size() == 0) {
                //sessionRequestContent.setRequestAttribute(RequestAttribute.CONFIRM_MESSAGE, FriendlyMessage.EMPTY_USER_LIST);
            }
            commandResult.setPage(PagePath.ALL_MEDIA_PERSONS);
        } catch (ServiceException e) {
            commandResult.setPage(PagePath.ERROR);
        }
        return commandResult;
    }
}
