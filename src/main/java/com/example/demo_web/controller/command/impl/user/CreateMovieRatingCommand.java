package com.example.demo_web.controller.command.impl.user;


import com.example.demo_web.controller.command.*;
import com.example.demo_web.model.entity.Movie;
import com.example.demo_web.exception.ServiceException;
import com.example.demo_web.model.service.MovieRatingService;
import com.example.demo_web.model.service.MovieService;
import com.example.demo_web.model.service.impl.MovieRatingServiceImpl;
import com.example.demo_web.model.service.impl.MovieServiceImpl;

public class CreateMovieRatingCommand implements ActionCommand {

    private MovieRatingService movieRatingService = MovieRatingServiceImpl.getInstance();
    private MovieService movieService = MovieServiceImpl.getInstance();

    @Override
    public CommandResult execute(SessionRequestContent sessionRequestContent) {
        CommandResult commandResult = new CommandResult();
        commandResult.setTransitionType(TransitionType.REDIRECT);

        try {
            int movieId = Integer.valueOf(sessionRequestContent.getRequestParameter(RequestParameter.MOVIE_ID));
            int userId = Integer.valueOf(sessionRequestContent.getRequestParameter(RequestParameter.USER_ID));
            float value = Float.valueOf(sessionRequestContent.getRequestParameter(RequestParameter.MOVIE_RATING_VALUE));
            movieRatingService.create(movieId, userId, value);
            Movie movie = movieService.findById(sessionRequestContent.getRequestParameter(RequestParameter.MOVIE_ID)).getKey().get();
            sessionRequestContent.setSessionAttribute(Attribute.MOVIE, movie);
            commandResult.setPage(PagePath.MOVIE);
        } catch (ServiceException e) {
            commandResult.setPage(PagePath.ERROR_404);
        }

        return commandResult;
    }
}
