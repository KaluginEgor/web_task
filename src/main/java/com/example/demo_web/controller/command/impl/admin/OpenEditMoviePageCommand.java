package com.example.demo_web.controller.command.impl.admin;


import com.example.demo_web.controller.command.*;
import com.example.demo_web.model.entity.GenreType;
import com.example.demo_web.model.entity.Movie;
import com.example.demo_web.exception.ServiceException;
import com.example.demo_web.model.service.MediaPersonService;
import com.example.demo_web.model.service.MovieService;
import com.example.demo_web.model.service.impl.MediaPersonServiceImpl;
import com.example.demo_web.model.service.impl.MovieServiceImpl;

public class OpenEditMoviePageCommand implements ActionCommand {
    private MediaPersonService mediaPersonService = new MediaPersonServiceImpl();
    private MovieService movieService = new MovieServiceImpl();

    @Override
    public CommandResult execute(SessionRequestContent sessionRequestContent) {
        CommandResult commandResult = new CommandResult();
        commandResult.setTransitionType(TransitionType.FORWARD);
        try {
            sessionRequestContent.setSessionAttribute(SessionAttribute.GENRE_TYPES, GenreType.values());
            if (movieExists(sessionRequestContent)) {
                Movie movie = movieService.findById(Integer.valueOf(sessionRequestContent.getRequestParameter(RequestParameter.MOVIE_ID)));
                sessionRequestContent.setSessionAttribute(SessionAttribute.MOVIE, movie);
            }
            commandResult.setPage(PagePath.EDIT_MOVIE);
        } catch (ServiceException e) {
            commandResult.setPage(PagePath.ERROR);
        }
        return commandResult;
    }

    private boolean movieExists(SessionRequestContent sessionRequestContent) {
        if (sessionRequestContent.getRequestParameter(RequestParameter.MOVIE_ID) != null) {
            return true;
        } else {
            return false;
        }
    }
}
