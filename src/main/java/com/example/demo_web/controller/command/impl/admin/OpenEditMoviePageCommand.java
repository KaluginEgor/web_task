package com.example.demo_web.controller.command.impl.admin;


import com.example.demo_web.controller.command.*;
import com.example.demo_web.model.entity.GenreType;
import com.example.demo_web.model.entity.MediaPerson;
import com.example.demo_web.model.entity.Movie;
import com.example.demo_web.exception.ServiceException;
import com.example.demo_web.model.service.MediaPersonService;
import com.example.demo_web.model.service.MovieService;
import com.example.demo_web.model.service.impl.MediaPersonServiceImpl;
import com.example.demo_web.model.service.impl.MovieServiceImpl;

import java.util.List;

public class OpenEditMoviePageCommand implements ActionCommand {
    private MediaPersonService mediaPersonService = new MediaPersonServiceImpl();
    private MovieService movieService = new MovieServiceImpl();

    @Override
    public CommandResult execute(SessionRequestContent sessionRequestContent) {
        CommandResult commandResult = new CommandResult();
        commandResult.setTransitionType(TransitionType.REDIRECT);
        try {
            sessionRequestContent.setSessionAttribute(Attribute.GENRE_TYPES, GenreType.values());
            List<MediaPerson> mediaPeople = mediaPersonService.finaAll();
            sessionRequestContent.setSessionAttribute(Attribute.MEDIA_PEOPLE, mediaPeople);
            sessionRequestContent.removeSessionAttribute(Attribute.MOVIE);
            if (sessionRequestContent.getRequestParameter((RequestParameter.MOVIE_ID)) != null) {
                Movie movie = movieService.findById(Integer.valueOf(sessionRequestContent.getRequestParameter(RequestParameter.MOVIE_ID)));
                sessionRequestContent.setSessionAttribute(Attribute.MOVIE, movie);
            }
            commandResult.setPage(PagePath.EDIT_MOVIE);
        } catch (ServiceException e) {
            commandResult.setPage(PagePath.ERROR);
        }
        return commandResult;
    }
}
