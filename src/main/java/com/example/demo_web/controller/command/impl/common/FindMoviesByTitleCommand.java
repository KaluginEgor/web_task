package com.example.demo_web.controller.command.impl.common;

import com.example.demo_web.controller.command.*;
import com.example.demo_web.exception.ServiceException;
import com.example.demo_web.model.entity.Movie;
import com.example.demo_web.model.entity.User;
import com.example.demo_web.model.service.MovieService;
import com.example.demo_web.model.service.impl.MovieServiceImpl;

import java.util.List;

public class FindMoviesByTitleCommand implements ActionCommand {
    private MovieService movieService = new MovieServiceImpl();

    @Override
    public CommandResult execute(SessionRequestContent sessionRequestContent) {
        CommandResult commandResult = new CommandResult();
        commandResult.setTransitionType(TransitionType.REDIRECT);

        try {
            String movieTitle = sessionRequestContent.getRequestParameter(RequestParameter.MOVIE_TO_FIND);
            List<Movie> movieList = movieService.findByNamePart(movieTitle);
            sessionRequestContent.setSessionAttribute(Attribute.QUERY_NAME, movieTitle);
            sessionRequestContent.setSessionAttribute(Attribute.MOVIES, movieList);
            commandResult.setPage(PagePath.MAIN);
        } catch (ServiceException e) {
            commandResult.setPage(PagePath.ERROR);
        }

        return commandResult;
    }
}
