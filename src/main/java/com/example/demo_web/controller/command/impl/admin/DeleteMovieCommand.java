package com.example.demo_web.controller.command.impl.admin;

import com.example.demo_web.controller.command.*;
import com.example.demo_web.exception.ServiceException;
import com.example.demo_web.model.service.MovieService;
import com.example.demo_web.model.service.impl.MovieServiceImpl;

public class DeleteMovieCommand implements ActionCommand {
    private MovieService movieService = MovieServiceImpl.getInstance();

    @Override
    public CommandResult execute(SessionRequestContent sessionRequestContent) {
        CommandResult commandResult = new CommandResult();
        commandResult.setTransitionType(TransitionType.REDIRECT);
        try {
            int movieId = Integer.valueOf(sessionRequestContent.getRequestParameter(RequestParameter.MOVIE_ID));
            movieService.delete(movieId);
            commandResult.setPage(PagePath.INDEX);
        } catch (ServiceException e) {
            commandResult.setPage(PagePath.ERROR_404);
        }
        return commandResult;
    }
}
