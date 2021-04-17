package com.example.demo_web.controller.command.impl.user;

import com.example.demo_web.controller.command.*;
import com.example.demo_web.exception.ServiceException;
import com.example.demo_web.model.entity.Movie;
import com.example.demo_web.model.entity.User;
import com.example.demo_web.model.service.MovieRatingService;
import com.example.demo_web.model.service.MovieService;
import com.example.demo_web.model.service.UserService;
import com.example.demo_web.model.service.impl.MovieRatingServiceImpl;
import com.example.demo_web.model.service.impl.MovieServiceImpl;
import com.example.demo_web.model.service.impl.UserServiceImpl;

public class DeleteMovieRatingCommand implements ActionCommand {
    private MovieRatingService movieRatingService = new MovieRatingServiceImpl();
    private MovieService movieService = new MovieServiceImpl();
    private UserService userService = new UserServiceImpl();

    @Override
    public CommandResult execute(SessionRequestContent sessionRequestContent) {
        CommandResult commandResult = new CommandResult();
        commandResult.setTransitionType(TransitionType.REDIRECT);
        try {
            int ratingId = Integer.valueOf(sessionRequestContent.getRequestParameter(RequestParameter.MOVIE_RATING_ID));
            String page = (String)sessionRequestContent.getSessionAttribute(Attribute.PAGE);
            movieRatingService.delete(ratingId);
            if (sessionRequestContent.getRequestParameter(RequestParameter.MOVIE_ID) != null) {
                int movieId = Integer.valueOf(sessionRequestContent.getRequestParameter(RequestParameter.MOVIE_ID));
                Movie movie = movieService.findById(movieId);
                sessionRequestContent.setSessionAttribute(Attribute.MOVIE, movie);
            }
            if (sessionRequestContent.getRequestParameter(RequestParameter.USER_ID) != null) {
                int userId = Integer.valueOf(sessionRequestContent.getRequestParameter(RequestParameter.USER_ID));
                User someUser = userService.findById(userId).get();
                sessionRequestContent.setSessionAttribute(Attribute.SOME_USER, someUser);
            }
            commandResult.setPage(page);
        } catch (ServiceException e) {
            commandResult.setPage(PagePath.ERROR);
        }
        return commandResult;
    }
}
