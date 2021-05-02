package com.epam.project.controller.command.impl.user;

import com.epam.project.controller.command.*;
import com.epam.project.exception.CommandException;
import com.epam.project.exception.ServiceException;
import com.epam.project.model.entity.Movie;
import com.epam.project.model.entity.User;
import com.epam.project.model.entity.UserRole;
import com.epam.project.model.service.MovieReviewService;
import com.epam.project.model.service.MovieService;
import com.epam.project.model.service.UserService;
import com.epam.project.model.service.impl.MovieReviewServiceImpl;
import com.epam.project.model.service.impl.MovieServiceImpl;
import com.epam.project.model.service.impl.UserServiceImpl;
import com.epam.project.model.util.message.ErrorMessage;
import com.epam.project.model.validator.MovieValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * The type Update movie review command.
 */
public class UpdateMovieReviewCommand implements ActionCommand {
    private static final Logger logger = LogManager.getLogger();
    /**
     * The Movie review service.
     */
    MovieReviewService movieReviewService = MovieReviewServiceImpl.getInstance();
    private UserService userService = UserServiceImpl.getInstance();
    /**
     * The Movie service.
     */
    MovieService movieService = MovieServiceImpl.getInstance();

    @Override
    public CommandResult execute(SessionRequestContent sessionRequestContent) throws CommandException {
        CommandResult commandResult = new CommandResult();
        commandResult.setTransitionType(TransitionType.REDIRECT);
        String stringMovieReviewId = sessionRequestContent.getRequestParameter(RequestParameter.MOVIE_REVIEW_ID);
        String stringMovieId = sessionRequestContent.getRequestParameter(RequestParameter.MOVIE_ID);
        String stringUserId = sessionRequestContent.getRequestParameter(RequestParameter.USER_ID);
        String reviewTitle = sessionRequestContent.getRequestParameter(RequestParameter.MOVIE_REVIEW_TITLE);
        String reviewBody = sessionRequestContent.getRequestParameter(RequestParameter.MOVIE_REVIEW_BODY);
        String page;
        if (reviewTitle == null || reviewBody == null || stringMovieReviewId == null ||stringMovieId == null || stringUserId == null) {
            sessionRequestContent.setSessionAttribute(Attribute.ERROR_MESSAGE, ErrorMessage.EMPTY_UPDATE_MOVIE_REVIEW_PARAMETERS);
            commandResult.setPage(PagePath.MAIN);
        } else {
            Map.Entry<List<String>,List<String>> validationResult = movieReviewService.validateData(
                    reviewTitle, reviewBody, stringMovieId, stringUserId);
            List<String> validParameters = validationResult.getKey();
            List<String> errorMessages = validationResult.getValue();
            if (!errorMessages.isEmpty()) {
                sessionRequestContent.setSessionAttribute(Attribute.VALIDATION_ERRORS, errorMessages);
                if (validParameters.contains(Attribute.MOVIE_ID) && validParameters.contains(Attribute.USER_ID)) {
                    if (validParameters.contains(Attribute.MOVIE_REVIEW_TITLE)) sessionRequestContent.setSessionAttribute(
                            Attribute.MOVIE_REVIEW_TITLE, reviewTitle);
                    if (validParameters.contains(Attribute.MOVIE_REVIEW_BODY)) sessionRequestContent.setSessionAttribute(
                            Attribute.MOVIE_REVIEW_BODY, reviewBody);
                    sessionRequestContent.setSessionAttribute(Attribute.MOVIE_ID, stringMovieId);
                    sessionRequestContent.setSessionAttribute(Attribute.USER_ID, stringUserId);
                    page = definePage(sessionRequestContent);
                    if (MovieValidator.isValidId(stringMovieId)) {
                        sessionRequestContent.setSessionAttribute(Attribute.MOVIE_REVIEW_ID, Integer.parseInt(stringMovieReviewId));
                    }
                    commandResult.setPage(page);
                } else {
                    commandResult.setPage(PagePath.MAIN);
                }
            } else {
                User currentUser = (User) sessionRequestContent.getSessionAttribute(Attribute.USER);
                if (UserRole.ADMIN.equals(currentUser.getRole()) || Integer.valueOf(stringUserId).equals(currentUser.getId())) {
                    Optional<String> errorMessage;
                    try {
                        errorMessage = movieReviewService.update(stringMovieReviewId, reviewTitle, reviewBody, stringMovieId, stringUserId);
                        if (errorMessage.isPresent()) {
                            sessionRequestContent.setSessionAttribute(Attribute.ERROR_MESSAGE, errorMessage.get());
                            commandResult.setPage(PagePath.MAIN);
                        } else {
                            page = definePage(sessionRequestContent);
                            commandResult.setPage(page);
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
