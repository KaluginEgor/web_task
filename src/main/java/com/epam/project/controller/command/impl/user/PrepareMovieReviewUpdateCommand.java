package com.epam.project.controller.command.impl.user;

import com.epam.project.controller.command.*;
import com.epam.project.exception.CommandException;
import com.epam.project.exception.ServiceException;
import com.epam.project.model.entity.Movie;
import com.epam.project.model.entity.MovieReview;
import com.epam.project.model.entity.User;
import com.epam.project.model.entity.UserRole;
import com.epam.project.model.service.MovieReviewService;
import com.epam.project.model.service.MovieService;
import com.epam.project.model.service.UserService;
import com.epam.project.model.service.impl.MovieReviewServiceImpl;
import com.epam.project.model.service.impl.MovieServiceImpl;
import com.epam.project.model.service.impl.UserServiceImpl;
import com.epam.project.model.util.message.ErrorMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class PrepareMovieReviewUpdateCommand implements ActionCommand {
    private static final Logger logger = LogManager.getLogger();
    private MovieReviewService movieReviewService = MovieReviewServiceImpl.getInstance();
    private MovieService movieService = MovieServiceImpl.getInstance();
    private UserService userService = UserServiceImpl.getInstance();
    private static final String TITLE = "title";
    private static final String BODY = "body";

    @Override
    public CommandResult execute(SessionRequestContent sessionRequestContent) throws CommandException {
        CommandResult commandResult = new CommandResult();
        commandResult.setTransitionType(TransitionType.REDIRECT);
        String stringMovieReviewId = sessionRequestContent.getRequestParameter(RequestParameter.MOVIE_REVIEW_ID);
        String stringMovieId = sessionRequestContent.getRequestParameter(RequestParameter.MOVIE_ID);
        String stringUserId = sessionRequestContent.getRequestParameter(RequestParameter.USER_ID);
        if (stringMovieReviewId == null || stringMovieId == null || stringUserId == null) {
            sessionRequestContent.setSessionAttribute(Attribute.ERROR_MESSAGE, ErrorMessage.EMPTY_PREPARE_MOVIE_REVIEW_PARAMETERS);
            commandResult.setPage(PagePath.MOVIE);
        } else {
            Map.Entry<List<String>,List<String>> validationResult = movieReviewService.validateData(TITLE, BODY, stringMovieId, stringUserId);
            List<String> errorMessages = validationResult.getValue();
            if (!errorMessages.isEmpty()) {
                sessionRequestContent.setSessionAttribute(Attribute.VALIDATION_ERRORS, errorMessages);
                commandResult.setPage(PagePath.MAIN);
            } else {
                User currentUser = (User) sessionRequestContent.getSessionAttribute(Attribute.USER);
                if (UserRole.ADMIN.equals(currentUser.getRole()) || Integer.valueOf(stringUserId).equals(currentUser.getId())) {
                    try {
                        Map.Entry<Optional<MovieReview>, Optional<String>> findResult = movieReviewService.findById(
                                stringMovieReviewId, stringMovieId, stringUserId);
                        Optional<MovieReview> reviewToUpdate = findResult.getKey();
                        Optional<String> errorMessage = findResult.getValue();
                        if (errorMessage.isPresent()) {
                            sessionRequestContent.setSessionAttribute(Attribute.ERROR_MESSAGE, errorMessage.get());
                            commandResult.setPage(PagePath.MAIN);
                        } else {
                            if (reviewToUpdate.isPresent()) {
                                sessionRequestContent.setSessionAttribute(Attribute.REVIEW_TO_UPDATE, reviewToUpdate.get());
                                String page = definePage(sessionRequestContent);
                                commandResult.setPage(page);
                            }
                        }
                    } catch (ServiceException e) {
                        commandResult.setPage(PagePath.ERROR_404);
                    }
                } else {
                    sessionRequestContent.setSessionAttribute(Attribute.ERROR_MESSAGE, ErrorMessage.ERROR_ACCESS);
                    commandResult.setPage(PagePath.MAIN);
                }
            }
        }
        return commandResult;
    }

    private String definePage(SessionRequestContent sessionRequestContent) throws CommandException {
        String stringMovieId = sessionRequestContent.getRequestParameter(RequestParameter.MOVIE_ID);
        String stringUserId = sessionRequestContent.getRequestParameter(RequestParameter.USER_ID);
        String page = (String) sessionRequestContent.getSessionAttribute(Attribute.PAGE);
        String result;
        try {
            if (PagePath.MOVIE.equals(page)) {
                Movie movie = movieService.findById(stringMovieId).getKey().get();
                sessionRequestContent.setSessionAttribute(Attribute.MOVIE, movie);
                result = PagePath.MOVIE;
            } else if (PagePath.USER.equals(page)) {
                User someUser = userService.findById(stringUserId).getKey().get();
                sessionRequestContent.setSessionAttribute(Attribute.SOME_USER, someUser);
                result = PagePath.USER;
            } else {
                result = PagePath.MAIN;
            }
        } catch (ServiceException e) {
            logger.error(e);
            throw new CommandException(e);
        }
        return result;
    }
}
