package com.example.demo_web.controller.command.impl.admin;


import com.example.demo_web.controller.command.*;
import com.example.demo_web.model.entity.MediaPerson;
import com.example.demo_web.model.entity.OccupationType;
import com.example.demo_web.exception.ServiceException;
import com.example.demo_web.model.service.MediaPersonService;
import com.example.demo_web.model.service.impl.MediaPersonServiceImpl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CreateMediaPersonCommand implements ActionCommand {
    private MediaPersonService mediaPersonService = new MediaPersonServiceImpl();

    @Override
    public CommandResult execute(SessionRequestContent sessionRequestContent) {
        CommandResult commandResult = new CommandResult();
        commandResult.setTransitionType(TransitionType.FORWARD);
        try {
            List<Integer> moviesId = new ArrayList<>();
            for (String movieId : sessionRequestContent.getRequestParameters(RequestParameter.MOVIE_TITLE)) {
                moviesId.add(Integer.valueOf(movieId));
            }
            MediaPerson mediaPerson = mediaPersonService.create(convertToMediaPerson(sessionRequestContent), moviesId);
            sessionRequestContent.setSessionAttribute(SessionAttribute.MEDIA_PERSON, mediaPerson);
            commandResult.setPage(PagePath.MEDIA_PERSON);
        } catch (ServiceException e) {
            commandResult.setPage(PagePath.ERROR);
        }
        return commandResult;
    }

    private MediaPerson convertToMediaPerson(SessionRequestContent sessionRequestContent) {
        MediaPerson mediaPerson = new MediaPerson();
        mediaPerson.setFirstName(sessionRequestContent.getRequestParameter(RequestParameter.FIRST_NAME));
        mediaPerson.setSecondName(sessionRequestContent.getRequestParameter(RequestParameter.SECOND_NAME));
        mediaPerson.setBio(sessionRequestContent.getRequestParameter(RequestParameter.BIO));
        OccupationType occupationType = OccupationType.valueOf(sessionRequestContent.getRequestParameter(RequestParameter.OCCUPATION_TYPE));
        mediaPerson.setOccupationType(occupationType);

        String stringBirthday = sessionRequestContent.getRequestParameter(RequestParameter.BIRTHDAY);
        LocalDate birthday = (stringBirthday != null && !stringBirthday.isEmpty()) ? LocalDate.parse(stringBirthday) : null;
        mediaPerson.setBirthday(birthday);
        mediaPerson.setPicture(sessionRequestContent.getRequestParameter(RequestParameter.PICTURE));

        return mediaPerson;
    }
}
