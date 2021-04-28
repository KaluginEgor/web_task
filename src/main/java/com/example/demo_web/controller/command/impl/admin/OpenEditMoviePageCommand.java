package com.example.demo_web.controller.command.impl.admin;


import com.example.demo_web.controller.command.*;
import com.example.demo_web.exception.CommandException;
import com.example.demo_web.model.entity.GenreType;
import com.example.demo_web.model.entity.MediaPerson;
import com.example.demo_web.model.entity.Movie;
import com.example.demo_web.exception.ServiceException;
import com.example.demo_web.model.service.MediaPersonService;
import com.example.demo_web.model.service.MovieService;
import com.example.demo_web.model.service.impl.MediaPersonServiceImpl;
import com.example.demo_web.model.service.impl.MovieServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class OpenEditMoviePageCommand implements ActionCommand {
    private static final Logger logger = LogManager.getLogger();
    private MediaPersonService mediaPersonService = MediaPersonServiceImpl.getInstance();
    private MovieService movieService = MovieServiceImpl.getInstance();

    @Override
    public CommandResult execute(SessionRequestContent sessionRequestContent) throws CommandException {
        CommandResult commandResult = new CommandResult(PagePath.EDIT_MOVIE, TransitionType.REDIRECT);
        String stringMovieId = sessionRequestContent.getRequestParameter(RequestParameter.MOVIE_ID);
        if (stringMovieId != null) {
            Optional<String> errorMessage;
            Optional<Movie> movie;
            Map.Entry<Optional<Movie>, Optional<String>> findResult;
            try {
                findResult = movieService.findById(stringMovieId);
                movie = findResult.getKey();
                errorMessage = findResult.getValue();
                if (errorMessage.isPresent()) {
                    sessionRequestContent.setSessionAttribute(Attribute.ERROR_MESSAGE, errorMessage.get());
                    commandResult.setPage(PagePath.MAIN);
                } else {
                    if (movie.isPresent()) {
                        sessionRequestContent.setSessionAttribute(Attribute.MOVIE, movie.get());
                    }
                }
            } catch (ServiceException e) {
                logger.error(e);
                throw new CommandException(e);
            }
        } else {
            sessionRequestContent.removeSessionAttribute(Attribute.MOVIE);
        }
        sessionRequestContent.setSessionAttribute(Attribute.GENRE_TYPES, GenreType.values());
        try {
            List<MediaPerson> mediaPersons = mediaPersonService.finaAll();
            sessionRequestContent.setSessionAttribute(Attribute.MEDIA_PERSONS, mediaPersons);
        } catch (ServiceException e) {
            logger.error(e);
            throw new CommandException(e);
        }
        return commandResult;
    }
}
