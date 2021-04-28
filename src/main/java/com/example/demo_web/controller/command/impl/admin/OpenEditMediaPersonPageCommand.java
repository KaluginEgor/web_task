package com.example.demo_web.controller.command.impl.admin;


import com.example.demo_web.controller.command.*;
import com.example.demo_web.exception.CommandException;
import com.example.demo_web.model.entity.MediaPerson;
import com.example.demo_web.model.entity.Movie;
import com.example.demo_web.model.entity.OccupationType;
import com.example.demo_web.exception.ServiceException;
import com.example.demo_web.model.service.MediaPersonService;
import com.example.demo_web.model.service.MovieService;
import com.example.demo_web.model.service.impl.MediaPersonServiceImpl;
import com.example.demo_web.model.service.impl.MovieServiceImpl;
import com.example.demo_web.model.util.message.ErrorMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
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
            List<Movie> foundMovies = movieService.findAll();
            sessionRequestContent.setSessionAttribute(Attribute.MOVIES, foundMovies);
        } catch (ServiceException e) {
            logger.error(e);
            throw new CommandException(e);
        }
        return commandResult;
    }
}
