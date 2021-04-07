package com.example.demo_web.controller.command.impl.admin;


import com.example.demo_web.controller.command.*;
import com.example.demo_web.model.entity.MediaPerson;
import com.example.demo_web.model.entity.OccupationType;
import com.example.demo_web.exception.ServiceException;
import com.example.demo_web.model.service.MediaPersonService;
import com.example.demo_web.model.service.MovieService;
import com.example.demo_web.model.service.impl.MediaPersonServiceImpl;
import com.example.demo_web.model.service.impl.MovieServiceImpl;

import java.util.Map;

public class OpenEditMediaPersonPageCommand implements ActionCommand {
    private MediaPersonService mediaPersonService = new MediaPersonServiceImpl();
    private MovieService movieService = new MovieServiceImpl();

    @Override
    public CommandResult execute(SessionRequestContent sessionRequestContent) {
        CommandResult commandResult = new CommandResult();
        commandResult.setTransitionType(TransitionType.FORWARD);
        try {
            sessionRequestContent.setSessionAttribute(SessionAttribute.OCCUPATION_TYPES, OccupationType.values());

            if (sessionRequestContent.getRequestParameter((RequestParameter.MEDIA_PERSON_ID)) != null) {
                MediaPerson mediaPerson = mediaPersonService.findById(Integer.valueOf(sessionRequestContent.getRequestParameter((RequestParameter.MEDIA_PERSON_ID))));
                sessionRequestContent.setRequestAttribute(SessionAttribute.MEDIA_PERSON, mediaPerson);
            }
            Map<Integer, String> foundTitles = movieService.findAllTitles();
            sessionRequestContent.setSessionAttribute(SessionAttribute.MOVIES_TITLES, foundTitles);

            commandResult.setPage(PagePath.EDIT_MEDIA_PERSON);
        } catch (ServiceException e) {
            commandResult.setPage(PagePath.ERROR);
        }
        return commandResult;
    }
}
