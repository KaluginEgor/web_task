package com.example.demo_web.controller.command.impl.admin;


import com.example.demo_web.controller.command.*;
import com.example.demo_web.model.entity.MediaPerson;
import com.example.demo_web.model.entity.Movie;
import com.example.demo_web.model.entity.OccupationType;
import com.example.demo_web.exception.ServiceException;
import com.example.demo_web.model.service.MediaPersonService;
import com.example.demo_web.model.service.MovieService;
import com.example.demo_web.model.service.impl.MediaPersonServiceImpl;
import com.example.demo_web.model.service.impl.MovieServiceImpl;

import java.util.List;

public class OpenEditMediaPersonPageCommand implements ActionCommand {
    private MediaPersonService mediaPersonService = MediaPersonServiceImpl.getInstance();
    private MovieService movieService = MovieServiceImpl.getInstance();

    @Override
    public CommandResult execute(SessionRequestContent sessionRequestContent) {
        CommandResult commandResult = new CommandResult();
        commandResult.setTransitionType(TransitionType.REDIRECT);
        try {
            sessionRequestContent.setSessionAttribute(Attribute.OCCUPATION_TYPES, OccupationType.values());
            List<Movie> foundMovies = movieService.findAll();
            sessionRequestContent.setSessionAttribute(Attribute.MOVIES, foundMovies);
            sessionRequestContent.removeSessionAttribute(Attribute.MEDIA_PERSON);
            if (sessionRequestContent.getRequestParameter((RequestParameter.MEDIA_PERSON_ID)) != null) {
                MediaPerson mediaPerson = mediaPersonService.findById(sessionRequestContent.getRequestParameter((RequestParameter.MEDIA_PERSON_ID))).getKey().get();
                sessionRequestContent.setSessionAttribute(Attribute.MEDIA_PERSON, mediaPerson);
            }
            commandResult.setPage(PagePath.EDIT_MEDIA_PERSON);
        } catch (ServiceException e) {
            commandResult.setPage(PagePath.ERROR_404);
        }
        return commandResult;
    }
}
