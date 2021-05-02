package com.epam.project.controller.command.impl.common;

import com.epam.project.controller.command.*;
import com.epam.project.exception.CommandException;
import com.epam.project.exception.ServiceException;
import com.epam.project.model.entity.Movie;
import com.epam.project.model.service.MovieService;
import com.epam.project.model.service.impl.MovieServiceImpl;
import com.epam.project.model.util.message.ErrorMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class FindMoviesByTitleCommand implements ActionCommand {
    private static final Logger logger = LogManager.getLogger();
    private MovieService movieService = MovieServiceImpl.getInstance();

    @Override
    public CommandResult execute(SessionRequestContent sessionRequestContent) throws CommandException {
        CommandResult commandResult = new CommandResult(PagePath.MAIN, TransitionType.REDIRECT);

        String movieTitle = sessionRequestContent.getRequestParameter(RequestParameter.MOVIE_TO_FIND);
        if (movieTitle == null) {
            sessionRequestContent.setSessionAttribute(Attribute.ERROR_MESSAGE, ErrorMessage.EMPTY_OPEN_MOVIE_PARAMETERS);
        } else {
            Optional<String> errorMessage;
            List<Movie> movies;
            Map.Entry<List<Movie>, Optional<String>> findResult;
            try {
                findResult = movieService.findByNamePart(movieTitle);
                movies = findResult.getKey();
                errorMessage = findResult.getValue();
                if (errorMessage.isPresent()) {
                    sessionRequestContent.setSessionAttribute(Attribute.ERROR_MESSAGE, errorMessage.get());
                } else {
                    sessionRequestContent.setSessionAttribute(Attribute.QUERY_NAME, movieTitle);
                    sessionRequestContent.setSessionAttribute(Attribute.FOUND_MOVIES, movies);
                }
            } catch (ServiceException e) {
                logger.error(e);
                throw new CommandException(e);
            }
        }
        return commandResult;
    }
}
