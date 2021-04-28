package com.example.demo_web.controller.command.impl.user;

import com.example.demo_web.controller.command.*;
import com.example.demo_web.exception.CommandException;
import com.example.demo_web.exception.ServiceException;
import com.example.demo_web.model.entity.Movie;
import com.example.demo_web.model.entity.User;
import com.example.demo_web.model.entity.UserRole;
import com.example.demo_web.model.service.MovieReviewService;
import com.example.demo_web.model.service.MovieService;
import com.example.demo_web.model.service.impl.MovieReviewServiceImpl;
import com.example.demo_web.model.service.impl.MovieServiceImpl;
import com.example.demo_web.model.util.message.ErrorMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CreateMovieReviewCommand implements ActionCommand {
    private static final Logger logger = LogManager.getLogger();
    MovieReviewService movieReviewService = MovieReviewServiceImpl.getInstance();
    MovieService movieService = MovieServiceImpl.getInstance();

    @Override
    public CommandResult execute(SessionRequestContent sessionRequestContent) throws CommandException {
        CommandResult commandResult = new CommandResult(PagePath.MOVIE, TransitionType.REDIRECT);
        String reviewTitle = sessionRequestContent.getRequestParameter(RequestParameter.MOVIE_REVIEW_TITLE);
        String reviewBody = sessionRequestContent.getRequestParameter(RequestParameter.MOVIE_REVIEW_BODY);
        String stringMovieId = sessionRequestContent.getRequestParameter(RequestParameter.MOVIE_ID);
        String stringUserId = sessionRequestContent.getRequestParameter(RequestParameter.USER_ID);
        if (reviewTitle == null || reviewBody == null || stringMovieId == null || stringUserId == null) {
            sessionRequestContent.setSessionAttribute(Attribute.ERROR_MESSAGE, ErrorMessage.EMPTY_CREATE_MOVIE_REVIEW_PARAMETERS);
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
                } else {
                    commandResult.setPage(PagePath.MAIN);
                }
            } else {
                User currentUser = (User) sessionRequestContent.getSessionAttribute(Attribute.USER);
                if (UserRole.ADMIN.equals(currentUser.getRole()) || Integer.valueOf(stringUserId).equals(currentUser.getId())) {
                    Optional<String> errorMessage;
                    try {
                        errorMessage = movieReviewService.create(reviewTitle, reviewBody, stringMovieId, stringUserId);
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
