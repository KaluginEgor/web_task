package com.example.demo_web.controller.command.impl.user;

import com.example.demo_web.controller.command.*;
import com.example.demo_web.exception.ServiceException;
import com.example.demo_web.model.entity.Movie;
import com.example.demo_web.model.service.MovieReviewService;
import com.example.demo_web.model.service.MovieService;
import com.example.demo_web.model.service.impl.MovieReviewServiceImpl;
import com.example.demo_web.model.service.impl.MovieServiceImpl;

public class CreateMovieReviewCommand implements ActionCommand {
    MovieReviewService movieReviewService = MovieReviewServiceImpl.getInstance();
    MovieService movieService = MovieServiceImpl.getInstance();

    @Override
    public CommandResult execute(SessionRequestContent sessionRequestContent) {
        CommandResult commandResult = new CommandResult();
        commandResult.setTransitionType(TransitionType.REDIRECT);

        try {
            int movieId = Integer.valueOf(sessionRequestContent.getRequestParameter(RequestParameter.MOVIE_ID));
            int userId = Integer.valueOf(sessionRequestContent.getRequestParameter(RequestParameter.USER_ID));
            String reviewTitle = sessionRequestContent.getRequestParameter(RequestParameter.MOVIE_REVIEW_TITLE);
            String reviewBody = sessionRequestContent.getRequestParameter(RequestParameter.MOVIE_REVIEW_BODY);
            movieReviewService.create(reviewTitle, reviewBody, movieId, userId);
            Movie movie = movieService.findById(sessionRequestContent.getRequestParameter(RequestParameter.MOVIE_ID)).getKey().get();
            sessionRequestContent.setSessionAttribute(Attribute.MOVIE, movie);
            commandResult.setPage(PagePath.MOVIE);
        } catch (ServiceException e) {
            commandResult.setPage(PagePath.ERROR_404);
        }

        return commandResult;
    }
}
