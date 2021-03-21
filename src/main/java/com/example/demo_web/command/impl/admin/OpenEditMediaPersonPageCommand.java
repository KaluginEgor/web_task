package com.example.demo_web.command.impl.admin;

import com.example.demo_web.command.*;
import com.example.demo_web.entity.MediaPerson;
import com.example.demo_web.entity.OccupationType;
import com.example.demo_web.exception.ServiceException;
import com.example.demo_web.service.MediaPersonService;
import com.example.demo_web.service.impl.MediaPersonServiceImpl;

public class OpenEditMediaPersonPageCommand implements ActionCommand {
    private MediaPersonService mediaPersonService = new MediaPersonServiceImpl();

    @Override
    public CommandResult execute(SessionRequestContent sessionRequestContent) {
        CommandResult commandResult = new CommandResult();
        commandResult.setTransitionType(TransitionType.FORWARD);
        try {
            sessionRequestContent.setSessionAttribute(SessionAttribute.OCCUPATION_TYPES, OccupationType.values());

            MediaPerson mediaPerson = mediaPersonService.findById(Integer.valueOf(sessionRequestContent.getRequestParameter((RequestParameter.MEDIA_PERSON_ID))));
            sessionRequestContent.setSessionAttribute(SessionAttribute.MEDIA_PERSON, mediaPerson);

            commandResult.setPage(PagePath.EDIT_MEDIA_PERSON);
        } catch (ServiceException e) {
            commandResult.setPage(PagePath.ERROR);
        }
        return commandResult;
    }
}
