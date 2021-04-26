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
    private MovieReviewService movieReviewService = MovieReviewServiceImpl.getInstance();
    private MovieService movieService = MovieServiceImpl.getInstance();
    private UserService userService = UserServiceImpl.getInstance();

    @Override
    public CommandResult execute(SessionRequestContent sessionRequestContent) {
        CommandResult commandResult = new CommandResult();
        commandResult.setTransitionType(TransitionType.REDIRECT);
        try {
            String page = (String)sessionRequestContent.getSessionAttribute(Attribute.PAGE);
            int reviewId = Integer.valueOf(sessionRequestContent.getRequestParameter(RequestParameter.MOVIE_REVIEW_ID));
            MovieReview reviewToUpdate = movieReviewService.findById(reviewId);
            sessionRequestContent.setSessionAttribute(Attribute.REVIEW_TO_UPDATE, reviewToUpdate);
            if (sessionRequestContent.getRequestParameter(RequestParameter.MOVIE_ID) != null) {
                Movie movie = movieService.findById(sessionRequestContent.getRequestParameter(RequestParameter.MOVIE_ID)).getKey().get();
                sessionRequestContent.setSessionAttribute(Attribute.MOVIE, movie);
            }
            if (sessionRequestContent.getRequestParameter(RequestParameter.USER_ID) != null) {
                User someUser = userService.findById(sessionRequestContent.getRequestParameter(RequestParameter.USER_ID)).getKey().get();
                sessionRequestContent.setSessionAttribute(Attribute.SOME_USER, someUser);
            }
            commandResult.setPage(page);
        } catch (ServiceException e) {
            commandResult.setPage(PagePath.ERROR_404);
        }
        return commandResult;
    }
}
