package com.example.demo_web.controller.command.impl.common.page;


import com.example.demo_web.controller.command.*;
import com.example.demo_web.exception.CommandException;
import com.example.demo_web.model.entity.Movie;
import com.example.demo_web.exception.ServiceException;
import com.example.demo_web.model.service.MovieService;
import com.example.demo_web.model.service.impl.MovieServiceImpl;
import com.example.demo_web.model.util.message.ErrorMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.Optional;

public class OpenMoviePageCommand implements ActionCommand {
    private static final Logger logger = LogManager.getLogger();
    private MovieService movieService = MovieServiceImpl.getInstance();

    @Override
    public CommandResult execute(SessionRequestContent sessionRequestContent) throws CommandException {
        CommandResult commandResult = new CommandResult(PagePath.MOVIE, TransitionType.REDIRECT);
        String stringMovieId = sessionRequestContent.getRequestParameter(RequestParameter.MOVIE_ID);
        if (stringMovieId == null) {
            sessionRequestContent.setSessionAttribute(Attribute.ERROR_MESSAGE, ErrorMessage.EMPTY_OPEN_MOVIE_PARAMETERS);
            commandResult.setPage(PagePath.MAIN);
        } else {
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
        }
        return commandResult;
    }
}
