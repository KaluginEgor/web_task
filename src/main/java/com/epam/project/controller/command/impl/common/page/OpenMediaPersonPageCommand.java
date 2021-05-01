package com.epam.project.controller.command.impl.common.page;


import com.epam.project.controller.command.*;
import com.epam.project.exception.CommandException;
import com.epam.project.exception.ServiceException;
import com.epam.project.model.entity.MediaPerson;
import com.epam.project.model.service.MediaPersonService;
import com.epam.project.model.service.impl.MediaPersonServiceImpl;
import com.epam.project.model.util.message.ErrorMessage;
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
