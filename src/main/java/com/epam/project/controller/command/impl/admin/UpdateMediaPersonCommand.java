package com.epam.project.controller.command.impl.admin;


import com.epam.project.controller.command.*;
import com.epam.project.exception.CommandException;
import com.epam.project.exception.ServiceException;
import com.epam.project.model.entity.MediaPerson;
import com.epam.project.model.entity.OccupationType;
import com.epam.project.model.service.MediaPersonService;
import com.epam.project.model.service.impl.MediaPersonServiceImpl;
import com.epam.project.model.util.message.ErrorMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * The type Update media person command.
 */
public class UpdateMediaPersonCommand implements ActionCommand {
    private static final Logger logger = LogManager.getLogger();
    private MediaPersonService mediaPersonService = MediaPersonServiceImpl.getInstance();
    private static final String DEFAULT_MEDIA_PERSON_PICTURE = "C:/Epam/pictures/media_person.jpg";

    @Override
    public CommandResult execute(SessionRequestContent sessionRequestContent) throws CommandException {
        CommandResult commandResult = new CommandResult(PagePath.MEDIA_PERSON, TransitionType.REDIRECT);
        String stringId = sessionRequestContent.getRequestParameter(RequestParameter.MEDIA_PERSON_ID);
        String firstName = sessionRequestContent.getRequestParameter(RequestParameter.FIRST_NAME);
        String secondName = sessionRequestContent.getRequestParameter(RequestParameter.SECOND_NAME);
        String bio = sessionRequestContent.getRequestParameter(RequestParameter.BIO);
        String stringOccupationTypeId = sessionRequestContent.getRequestParameter(RequestParameter.MEDIA_PERSON_OCCUPATION_TYPE);
        String stringBirthday = sessionRequestContent.getRequestParameter(RequestParameter.MEDIA_PERSON_BIRTHDAY);
        String picture = sessionRequestContent.getRequestParameter(RequestParameter.PICTURE);
        if (picture == null) {
            picture = DEFAULT_MEDIA_PERSON_PICTURE;
        }
        String[] stringMoviesId = sessionRequestContent.getRequestParameters(RequestParameter.MEDIA_PERSON_MOVIES);
        if (firstName == null || secondName == null || bio == null || stringOccupationTypeId == null || stringBirthday == null) {
            sessionRequestContent.setSessionAttribute(Attribute.ERROR_MESSAGE, ErrorMessage.EMPTY_UPDATE_MEDIA_PERSON_PARAMETERS);
            commandResult.setPage(PagePath.MAIN);
        } else {
            Map.Entry<List<String>, List<String>> validationResult = mediaPersonService.validateData(
                    firstName, secondName, bio, stringOccupationTypeId, stringBirthday, picture, stringMoviesId);
            List<String> validParameters = validationResult.getKey();
            List<String> errorMessages = validationResult.getValue();
            if (!errorMessages.isEmpty()) {
                sessionRequestContent.setSessionAttribute(Attribute.VALIDATION_ERRORS, errorMessages);
                if (validParameters.contains(RequestParameter.FIRST_NAME)) sessionRequestContent.setSessionAttribute(
                        Attribute.FIRST_NAME, firstName);
                if (validParameters.contains(RequestParameter.SECOND_NAME)) sessionRequestContent.setSessionAttribute(
                        Attribute.SECOND_NAME, secondName);
                if (validParameters.contains(RequestParameter.BIO)) sessionRequestContent.setSessionAttribute(
                        Attribute.BIO, bio);
                if (validParameters.contains(RequestParameter.MEDIA_PERSON_OCCUPATION_TYPE)) {
                    int occupationId = Integer.parseInt(stringOccupationTypeId);
                    sessionRequestContent.setSessionAttribute(Attribute.MEDIA_PERSON_OCCUPATION_TYPE, OccupationType.values()[occupationId]);
                }
                if (validParameters.contains(RequestParameter.MEDIA_PERSON_BIRTHDAY))
                    sessionRequestContent.setSessionAttribute(
                            Attribute.MEDIA_PERSON_BIRTHDAY, LocalDate.parse(stringBirthday));
                if (validParameters.contains(RequestParameter.PICTURE)) sessionRequestContent.setSessionAttribute(
                        Attribute.NEW_PICTURE, picture);

                List<Integer> mediaPersonMoviesId;
                if (stringMoviesId != null) {
                    mediaPersonMoviesId = Arrays.asList(stringMoviesId).stream().map(Integer::parseInt).collect(Collectors.toList());
                } else {
                    mediaPersonMoviesId = new ArrayList<>();
                }
                if (validParameters.contains(RequestParameter.MEDIA_PERSON_MOVIES)) sessionRequestContent.setSessionAttribute(
                        Attribute.MEDIA_PERSON_MOVIES, mediaPersonMoviesId);
                commandResult.setPage(PagePath.EDIT_MEDIA_PERSON);
            } else {
                Optional<String> errorMessage;
                Optional<MediaPerson> mediaPerson;
                Map.Entry<Optional<MediaPerson>, Optional<String>> findResult;
                try {
                    findResult = mediaPersonService.update(stringId, firstName, secondName, bio, stringOccupationTypeId, stringBirthday, picture, stringMoviesId);
                    mediaPerson = findResult.getKey();
                    errorMessage = findResult.getValue();
                    if (errorMessage.isPresent()) {
                        sessionRequestContent.setSessionAttribute(Attribute.ERROR_MESSAGE, errorMessage.get());
                        commandResult.setPage(PagePath.MAIN);
                    } else {
                        if (mediaPerson.isPresent()) {
                            sessionRequestContent.setSessionAttribute(Attribute.MEDIA_PERSON, mediaPerson.get());
                        }
                    }
                } catch (ServiceException e) {
                    logger.error(e);
                    throw new CommandException(e);
                }
            }
        }
        return commandResult;
    }
}
