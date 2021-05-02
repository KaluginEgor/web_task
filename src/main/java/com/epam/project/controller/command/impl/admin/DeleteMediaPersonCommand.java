package com.epam.project.controller.command.impl.admin;

import com.epam.project.controller.command.*;
import com.epam.project.exception.CommandException;
import com.epam.project.exception.ServiceException;
import com.epam.project.model.service.MediaPersonService;
import com.epam.project.model.service.impl.MediaPersonServiceImpl;
import com.epam.project.model.util.message.ErrorMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class DeleteMediaPersonCommand implements ActionCommand {
    private static final Logger logger = LogManager.getLogger();
    private MediaPersonService mediaPersonService = MediaPersonServiceImpl.getInstance();

    @Override
    public CommandResult execute(SessionRequestContent sessionRequestContent) throws CommandException {
        CommandResult commandResult = new CommandResult(PagePath.MAIN, TransitionType.REDIRECT);
        String stringMediaPersonId = sessionRequestContent.getRequestParameter(RequestParameter.MEDIA_PERSON_ID);
        if (stringMediaPersonId == null) {
            sessionRequestContent.setSessionAttribute(Attribute.ERROR_MESSAGE, ErrorMessage.EMPTY_DELETE_MEDIA_PERSON_PARAMETERS);
        } else {
            Optional<String> errorMessage;
            try {
                errorMessage = mediaPersonService.delete(stringMediaPersonId);
                if (errorMessage.isPresent()) {
                    sessionRequestContent.setSessionAttribute(Attribute.ERROR_MESSAGE, errorMessage.get());
                }
            } catch (ServiceException e) {
                logger.error(e);
                throw new CommandException(e);
            }
        }
        return commandResult;
    }
}
