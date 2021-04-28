package com.example.demo_web.controller.command.impl.user;


import com.example.demo_web.controller.command.*;
import com.example.demo_web.exception.CommandException;
import com.example.demo_web.model.entity.Movie;
import com.example.demo_web.exception.ServiceException;
import com.example.demo_web.model.entity.User;
import com.example.demo_web.model.entity.UserRole;
import com.example.demo_web.model.service.MovieRatingService;
import com.example.demo_web.model.service.MovieService;
import com.example.demo_web.model.service.impl.MovieRatingServiceImpl;
import com.example.demo_web.model.service.impl.MovieServiceImpl;
import com.example.demo_web.model.util.message.ErrorMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

public class CreateMovieRatingCommand implements ActionCommand {
    private static final Logger logger = LogManager.getLogger();
    private MovieRatingService movieRatingService = MovieRatingServiceImpl.getInstance();
    private MovieService movieService = MovieServiceImpl.getInstance();

    @Override
    public CommandResult execute(SessionRequestContent sessionRequestContent) throws CommandException {
        CommandResult commandResult = new CommandResult(PagePath.MOVIE, TransitionType.REDIRECT);
        String stringMovieId = sessionRequestContent.getRequestParameter(RequestParameter.MOVIE_ID);
        String stringUserId = sessionRequestContent.getRequestParameter(RequestParameter.USER_ID);
        String stringValue = sessionRequestContent.getRequestParameter(RequestParameter.MOVIE_RATING_VALUE);
        if (stringMovieId == null || stringUserId == null || stringValue == null) {
            sessionRequestContent.setSessionAttribute(Attribute.ERROR_MESSAGE, ErrorMessage.EMPTY_CREATE_MOVIE_RATING_PARAMETERS);
        } else {
            List<String> errorMessages = movieRatingService.validateData(stringMovieId, stringUserId, stringValue);
            if (!errorMessages.isEmpty()) {
                sessionRequestContent.setSessionAttribute(Attribute.VALIDATION_ERRORS, errorMessages);
            } else {
                User currentUser = (User) sessionRequestContent.getSessionAttribute(Attribute.USER);
                if (UserRole.ADMIN.equals(currentUser.getRole()) || Integer.valueOf(stringUserId).equals(currentUser.getId())) {
                    Optional<String> errorMessage;
                    try {
                        errorMessage = movieRatingService.create(stringMovieId, stringUserId, stringValue);
                        if (errorMessage.isPresent()) {
                            sessionRequestContent.setSessionAttribute(Attribute.ERROR_MESSAGE, errorMessage.get());
                            commandResult.setPage(PagePath.MAIN);
                        } else {
                            Movie movie = movieService.findById(stringMovieId).getKey().get();
                            sessionRequestContent.setSessionAttribute(Attribute.MOVIE, movie);
                        }
                    } catch (ServiceException e) {
                        logger.error(e);
                        throw new CommandException(e);
                    }
                } else {
                    sessionRequestContent.setSessionAttribute(Attribute.ERROR_MESSAGE, ErrorMessage.ERROR_ACCESS);
                    commandResult.setPage(PagePath.MAIN);
                }
            }
        }
        return commandResult;
    }
}
