package com.epam.project.controller.command.impl.admin;


import com.epam.project.controller.command.*;
import com.epam.project.exception.CommandException;
import com.epam.project.exception.ServiceException;
import com.epam.project.model.entity.GenreType;
import com.epam.project.model.entity.Movie;
import com.epam.project.model.service.MediaPersonService;
import com.epam.project.model.service.MovieService;
import com.epam.project.model.service.impl.MediaPersonServiceImpl;
import com.epam.project.model.service.impl.MovieServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.Optional;

/**
 * The type Open edit movie page command.
 */
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
            Map<Integer, String> mediaPersons = mediaPersonService.finaAllNames();
            sessionRequestContent.setSessionAttribute(Attribute.MEDIA_PERSONS, mediaPersons);
        } catch (ServiceException e) {
            logger.error(e);
            throw new CommandException(e);
        }
        return commandResult;
    }
}
