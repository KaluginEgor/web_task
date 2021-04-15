package com.example.demo_web.controller.command.impl.user;

import com.example.demo_web.controller.command.*;
import com.example.demo_web.exception.ServiceException;
import com.example.demo_web.model.entity.Movie;
import com.example.demo_web.model.entity.MovieReview;
import com.example.demo_web.model.entity.User;
import com.example.demo_web.model.service.MovieReviewService;
import com.example.demo_web.model.service.MovieService;
import com.example.demo_web.model.service.UserService;
import com.example.demo_web.model.service.impl.MovieReviewServiceImpl;
import com.example.demo_web.model.service.impl.MovieServiceImpl;
import com.example.demo_web.model.service.impl.UserServiceImpl;

public class PrepareMovieReviewUpdateCommand implements ActionCommand {
    private MovieReviewService movieReviewService = new MovieReviewServiceImpl();
    private MovieService movieService = new MovieServiceImpl();
    private UserService userService = new UserServiceImpl();

    @Override
    public CommandResult execute(SessionRequestContent sessionRequestContent) {
        CommandResult commandResult = new CommandResult();
        commandResult.setTransitionType(TransitionType.FORWARD);
        try {
            String page = (String)sessionRequestContent.getSessionAttribute(SessionAttribute.PAGE);
            int reviewId = Integer.valueOf(sessionRequestContent.getRequestParameter(RequestParameter.MOVIE_REVIEW_ID));
            MovieReview reviewToUpdate = movieReviewService.findById(reviewId);
            sessionRequestContent.setSessionAttribute(SessionAttribute.REVIEW_TO_UPDATE, reviewToUpdate);
            if (sessionRequestContent.getRequestParameter(RequestParameter.MOVIE_ID) != null) {
                int movieId = Integer.valueOf(sessionRequestContent.getRequestParameter(RequestParameter.MOVIE_ID));
                Movie movie = movieService.findById(movieId);
                sessionRequestContent.setSessionAttribute(SessionAttribute.MOVIE, movie);
            }
            if (sessionRequestContent.getRequestParameter(RequestParameter.USER_ID) != null) {
                int userId = Integer.valueOf(sessionRequestContent.getRequestParameter(RequestParameter.USER_ID));
                User someUser = userService.findById(userId).get();
                sessionRequestContent.setSessionAttribute(SessionAttribute.SOME_USER, someUser);
            }
            commandResult.setPage(page);
        } catch (ServiceException e) {
            commandResult.setPage(PagePath.ERROR);
        }
        return commandResult;
    }
}