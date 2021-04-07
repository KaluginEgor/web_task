package com.example.demo_web.controller.command.impl;


import com.example.demo_web.controller.command.*;
import com.example.demo_web.model.entity.MediaPerson;
import com.example.demo_web.exception.ServiceException;
import com.example.demo_web.model.service.MediaPersonService;
import com.example.demo_web.model.service.impl.MediaPersonServiceImpl;

public class OpenMediaPersonPageCommand implements ActionCommand {
    MediaPersonService mediaPersonService = new MediaPersonServiceImpl();

    @Override
    public CommandResult execute(SessionRequestContent sessionRequestContent) {
        CommandResult commandResult = new CommandResult();
        commandResult.setTransitionType(TransitionType.FORWARD);
        try {
            Integer mediaPersonId = Integer.valueOf(sessionRequestContent.getRequestParameter((RequestParameter.MEDIA_PERSON_ID)));
            MediaPerson mediaPerson = mediaPersonService.findById(mediaPersonId);
            sessionRequestContent.setSessionAttribute(SessionAttribute.MEDIA_PERSON, mediaPerson);
            commandResult.setPage(PagePath.MEDIA_PERSON);
        } catch (ServiceException e) {
            commandResult.setPage(PagePath.ERROR);
        }
        return commandResult;
    }
}
