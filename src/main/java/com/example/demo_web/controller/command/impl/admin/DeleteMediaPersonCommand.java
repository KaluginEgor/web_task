package com.example.demo_web.controller.command.impl.admin;

import com.example.demo_web.controller.command.*;
import com.example.demo_web.exception.ServiceException;
import com.example.demo_web.model.service.MediaPersonService;
import com.example.demo_web.model.service.impl.MediaPersonServiceImpl;

public class DeleteMediaPersonCommand implements ActionCommand {
    private MediaPersonService mediaPersonService = MediaPersonServiceImpl.getInstance();

    @Override
    public CommandResult execute(SessionRequestContent sessionRequestContent) {
        CommandResult commandResult = new CommandResult();
        commandResult.setTransitionType(TransitionType.REDIRECT);
        try {
            int mediaPersonId = Integer.valueOf(sessionRequestContent.getRequestParameter(RequestParameter.MEDIA_PERSON_ID));
            mediaPersonService.delete(mediaPersonId);
            commandResult.setPage(PagePath.INDEX);
        } catch (ServiceException e) {
            commandResult.setPage(PagePath.ERROR_404);
        }
        return commandResult;
    }
}
