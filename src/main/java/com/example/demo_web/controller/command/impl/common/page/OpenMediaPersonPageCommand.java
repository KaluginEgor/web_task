package com.example.demo_web.controller.command.impl.common.page;


import com.example.demo_web.controller.command.*;
import com.example.demo_web.exception.CommandException;
import com.example.demo_web.model.entity.MediaPerson;
import com.example.demo_web.exception.ServiceException;
import com.example.demo_web.model.service.MediaPersonService;
import com.example.demo_web.model.service.impl.MediaPersonServiceImpl;
import com.example.demo_web.model.util.message.ErrorMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.Optional;

public class OpenMediaPersonPageCommand implements ActionCommand {
    private static final Logger logger = LogManager.getLogger();
    private MediaPersonService mediaPersonService = MediaPersonServiceImpl.getInstance();

    @Override
    public CommandResult execute(SessionRequestContent sessionRequestContent) throws CommandException {
        CommandResult commandResult = new CommandResult(PagePath.MEDIA_PERSON, TransitionType.REDIRECT);
        String stringMediaPersonId = sessionRequestContent.getRequestParameter(RequestParameter.MEDIA_PERSON_ID);
        if (stringMediaPersonId == null) {
            sessionRequestContent.setSessionAttribute(Attribute.ERROR_MESSAGE, ErrorMessage.EMPTY_OPEN_MEDIA_PERSON_PARAMETERS);
            commandResult.setPage(PagePath.MAIN);
        } else {
            Optional<String> errorMessage;
            Optional<MediaPerson> mediaPerson;
            Map.Entry<Optional<MediaPerson>, Optional<String>> findResult;
            try {
                findResult = mediaPersonService.findById(stringMediaPersonId);
                mediaPerson = findResult.getKey();
                errorMessage = findResult.getValue();
                if (errorMessage.isPresent()) {
                    sessionRequestContent.setSessionAttribute(Attribute.ERROR_MESSAGE, errorMessage.get());
                    commandResult.setPage(PagePath.MAIN);
                } else {
                    if (mediaPerson.isPresent()) {
                        sessionRequestContent.setSessionAttribute(Attribute.MEDIA_PERSON, mediaPerson.get());
                    }
                }
            } catch (ServiceException e) {
                logger.error(e);
                throw new CommandException(e);
            }
        }
        return commandResult;
    }
}
