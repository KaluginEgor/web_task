package com.example.demo_web.command.impl;

import com.example.demo_web.command.*;
import com.example.demo_web.entity.Movie;
import com.example.demo_web.exception.ServiceException;
import com.example.demo_web.service.MovieService;
import com.example.demo_web.service.impl.MovieServiceImpl;

public class ShowMoviePageCommand implements ActionCommand {

    private MovieService movieService = new MovieServiceImpl();

    @Override
    public CommandResult execute(SessionRequestContent sessionRequestContent) {
        CommandResult commandResult = new CommandResult();
        commandResult.setTransitionType(TransitionType.FORWARD);
        try {
            Integer movieId = Integer.valueOf(sessionRequestContent.getRequestParameter(RequestParameter.ID));
            Movie movie = movieService.findById(movieId);
            sessionRequestContent.setSessionAttribute(SessionAttribute.MOVIE, movie);
            commandResult.setPage(PagePath.MOVIE);
        } catch (ServiceException e) {
            commandResult.setPage(PagePath.ERROR);
        }
        return commandResult;
    }
}
