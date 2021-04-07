package com.example.demo_web.controller.command.impl.user;

import com.example.demo_web.controller.command.*;
import com.example.demo_web.model.entity.Movie;
import com.example.demo_web.exception.ServiceException;
import com.example.demo_web.model.service.MovieRatingService;
import com.example.demo_web.model.service.MovieService;
import com.example.demo_web.model.service.impl.MovieRatingServiceImpl;
import com.example.demo_web.model.service.impl.MovieServiceImpl;

public class UpdateMovieRatingCommand implements ActionCommand {

    private MovieRatingService movieRatingService = new MovieRatingServiceImpl();
    private MovieService movieService = new MovieServiceImpl();

    @Override
    public CommandResult execute(SessionRequestContent sessionRequestContent) {
        CommandResult commandResult = new CommandResult();
        commandResult.setTransitionType(TransitionType.FORWARD);

        try {
            int movieRatingId = Integer.valueOf(sessionRequestContent.getRequestParameter(RequestParameter.MOVIE_RATING_ID));
            int movieId = Integer.valueOf(sessionRequestContent.getRequestParameter(RequestParameter.MOVIE_ID));
            int userId = Integer.valueOf(sessionRequestContent.getRequestParameter(RequestParameter.USER_ID));
            float value = Float.valueOf(sessionRequestContent.getRequestParameter(RequestParameter.MOVIE_RATING_VALUE));
            movieRatingService.update(movieRatingId, movieId, userId, value);
            Movie movie = movieService.findById(Integer.valueOf(sessionRequestContent.getRequestParameter(RequestParameter.MOVIE_ID)));
            sessionRequestContent.setSessionAttribute(SessionAttribute.MOVIE, movie);
            commandResult.setPage(PagePath.MOVIE);
        } catch (ServiceException e) {
            commandResult.setPage(PagePath.ERROR);
        }

        return commandResult;
    }
}
