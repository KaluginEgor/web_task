package com.example.demo_web.controller.command.impl.user;

import com.example.demo_web.controller.command.*;
import com.example.demo_web.exception.CommandException;
import com.example.demo_web.exception.ServiceException;
import com.example.demo_web.model.entity.Movie;
import com.example.demo_web.model.entity.User;
import com.example.demo_web.model.entity.UserRole;
import com.example.demo_web.model.service.MovieRatingService;
import com.example.demo_web.model.service.MovieService;
import com.example.demo_web.model.service.UserService;
import com.example.demo_web.model.service.impl.MovieRatingServiceImpl;
import com.example.demo_web.model.service.impl.MovieServiceImpl;
import com.example.demo_web.model.service.impl.UserServiceImpl;
import com.example.demo_web.model.util.message.ErrorMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

public class DeleteMovieRatingCommand implements ActionCommand {
    private static final Logger logger = LogManager.getLogger();
    private MovieRatingService movieRatingService = MovieRatingServiceImpl.getInstance();
    private MovieService movieService = MovieServiceImpl.getInstance();
    private UserService userService = UserServiceImpl.getInstance();
    private static final String RATING_VALUE = "5";

    @Override
    public CommandResult execute(SessionRequestContent sessionRequestContent) throws CommandException {
        CommandResult commandResult = new CommandResult();
        commandResult.setTransitionType(TransitionType.REDIRECT);
        String stringRatingId = sessionRequestContent.getRequestParameter(RequestParameter.MOVIE_RATING_ID);
        String stringMovieId = sessionRequestContent.getRequestParameter(RequestParameter.MOVIE_ID);
        String stringUserId = sessionRequestContent.getRequestParameter(RequestParameter.USER_ID);
        String page = (String) sessionRequestContent.getSessionAttribute(Attribute.PAGE);
        try {
            if (stringRatingId == null || stringMovieId == null || stringUserId == null) {
                sessionRequestContent.setSessionAttribute(Attribute.ERROR_MESSAGE, ErrorMessage.EMPTY_DELETE_MOVIE_RATING_PARAMETERS);
                commandResult.setPage(PagePath.MOVIE);
            } else {
                List<String> errorMessages = movieRatingService.validateData(stringMovieId, stringUserId, RATING_VALUE);
                if (!errorMessages.isEmpty()) {
                    sessionRequestContent.setSessionAttribute(Attribute.VALIDATION_ERRORS, errorMessages);
                    commandResult.setPage(PagePath.MAIN);
                } else {
                    User currentUser = (User) sessionRequestContent.getSessionAttribute(Attribute.USER);
                    if (UserRole.ADMIN.equals(currentUser.getRole()) || Integer.valueOf(stringUserId).equals(currentUser.getId())) {
                        Optional<String> errorMessage = movieRatingService.delete(stringRatingId, stringMovieId, stringUserId);
                        if (errorMessage.isPresent()) {
                            sessionRequestContent.setSessionAttribute(Attribute.ERROR_MESSAGE, errorMessage.get());
                            commandResult.setPage(PagePath.MAIN);
                        } else {
                            if (PagePath.MOVIE.equals(page)) {
                                Movie movie = movieService.findById(stringMovieId).getKey().get();
                                sessionRequestContent.setSessionAttribute(Attribute.MOVIE, movie);
                                commandResult.setPage(PagePath.MOVIE);
                            } else if (PagePath.USER.equals(page)) {
                                User someUser = userService.findById(stringUserId).getKey().get();
                                sessionRequestContent.setSessionAttribute(Attribute.SOME_USER, someUser);
                                commandResult.setPage(PagePath.USER);
                            } else {
                                commandResult.setPage(PagePath.MAIN);
                            }
                        }
                    } else {
                        sessionRequestContent.setSessionAttribute(Attribute.ERROR_MESSAGE, ErrorMessage.ERROR_ACCESS);
                        commandResult.setPage(PagePath.MAIN);
                    }
                }
            }
        } catch (ServiceException e) {
            logger.error(e);
            throw new CommandException(e);
        }
        return commandResult;
    }
}
