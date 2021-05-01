package com.epam.project.controller.command.impl.admin;


import com.epam.project.controller.command.*;
import com.epam.project.exception.CommandException;
import com.epam.project.exception.ServiceException;
import com.epam.project.model.entity.MediaPerson;
import com.epam.project.model.entity.OccupationType;
import com.epam.project.model.service.MediaPersonService;
import com.epam.project.model.service.MovieService;
import com.epam.project.model.service.impl.MediaPersonServiceImpl;
import com.epam.project.model.service.impl.MovieServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.Optional;

public class OpenEditMediaPersonPageCommand implements ActionCommand {
    private static final Logger logger = LogManager.getLogger();
    private MediaPersonService mediaPersonService = MediaPersonServiceImpl.getInstance();
    private MovieService movieService = MovieServiceImpl.getInstance();

    @Override
    public CommandResult execute(SessionRequestContent sessionRequestContent) throws CommandException {
        CommandResult commandResult = new CommandResult(PagePath.EDIT_MEDIA_PERSON, TransitionType.REDIRECT);
        String stringMediaPersonId = sessionRequestContent.getRequestParameter(RequestParameter.MEDIA_PERSON_ID);
        if (stringMediaPersonId != null) {
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
        } else {
            sessionRequestContent.removeSessionAttribute(Attribute.MEDIA_PERSON);
        }
        sessionRequestContent.setSessionAttribute(Attribute.OCCUPATION_TYPES, OccupationType.values());
        try {
            Map<Integer, String> foundMovies = movieService.findAllTitles();
            sessionRequestContent.setSessionAttribute(Attribute.MOVIE_TITLES, foundMovies);
        } catch (ServiceException e) {
            logger.error(e);
            throw new CommandException(e);
        }
        return commandResult;
    }
}
