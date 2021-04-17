package com.example.demo_web.controller.command.impl.user;

import com.example.demo_web.controller.command.*;
import com.example.demo_web.exception.ServiceException;
import com.example.demo_web.model.entity.*;
import com.example.demo_web.model.service.MediaPersonService;
import com.example.demo_web.model.service.MovieService;
import com.example.demo_web.model.service.UserService;
import com.example.demo_web.model.service.impl.MediaPersonServiceImpl;
import com.example.demo_web.model.service.impl.MovieServiceImpl;
import com.example.demo_web.model.service.impl.UserServiceImpl;

import javax.servlet.http.Part;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class UploadPictureCommand implements ActionCommand {
    private MovieService movieService = new MovieServiceImpl();
    private UserService userService = new UserServiceImpl();
    private MediaPersonService mediaPersonService = new MediaPersonServiceImpl();
    private static final String UPLOAD_PICTURE_DIRECTORY = "C:/Epam/pictures";
    private static final char FILE_FORMAT_SEPARATOR = '.';

    @Override
    public CommandResult execute(SessionRequestContent sessionRequestContent) {
        CommandResult commandResult = new CommandResult();
        commandResult.setTransitionType(TransitionType.REDIRECT);
        String page = (String)sessionRequestContent.getSessionAttribute(Attribute.PAGE);
        List<Part> fileParts = sessionRequestContent.getFileParts();
        String fileName = null;
        try {
            if (sessionRequestContent.getRequestParameter(RequestParameter.MOVIE_ID) != null) {
                sessionRequestContent.setSessionAttribute(Attribute.GENRE_TYPES, GenreType.values());
                List<MediaPerson> mediaPeople = mediaPersonService.finaAll();
                sessionRequestContent.setSessionAttribute(Attribute.MEDIA_PEOPLE, mediaPeople);
                int movieId = Integer.valueOf(sessionRequestContent.getRequestParameter(RequestParameter.MOVIE_ID));
                if (movieId != 0) {
                    Movie movie = movieService.findById(movieId);
                    sessionRequestContent.setSessionAttribute(Attribute.MOVIE, movie);
                }
            }
            if (sessionRequestContent.getRequestParameter(RequestParameter.USER_ID) != null) {
                int userId = Integer.valueOf(sessionRequestContent.getRequestParameter(RequestParameter.USER_ID));
                User someUser = userService.findById(userId).get();
                sessionRequestContent.setSessionAttribute(Attribute.SOME_USER, someUser);
            }
            if (sessionRequestContent.getRequestParameter(RequestParameter.MEDIA_PERSON_ID) != null) {
                sessionRequestContent.setSessionAttribute(Attribute.OCCUPATION_TYPES, OccupationType.values());
                List<Movie> foundMovies = movieService.findAll();
                sessionRequestContent.setSessionAttribute(Attribute.MOVIES, foundMovies);
                int mediaPersonId = Integer.valueOf(sessionRequestContent.getRequestParameter(RequestParameter.MEDIA_PERSON_ID));
                if (mediaPersonId != 0) {
                    MediaPerson mediaPerson = mediaPersonService.findById(mediaPersonId);
                    sessionRequestContent.setSessionAttribute(Attribute.MEDIA_PERSON, mediaPerson);
                }
            }
        } catch (ServiceException e) {

        }

        try {
            for (Part part : fileParts) {
                fileName = part.getSubmittedFileName();
                if (fileName != null && !fileName.isEmpty()) {
                    fileName = buildNewFileName(fileName);
                    part.write(fileName);
                }
            }
        } catch (IOException e) {
            //logger.error(e);
        }
        if (fileName == null) {
            commandResult.setPage(page);
            //requestContext.setRequestAttribute(RequestAttribute.ERROR_MESSAGE, EMPTY_UPLOAD_FILE_PARAMETERS);
        } else {
            if (!fileName.isEmpty()) {
                sessionRequestContent.setSessionAttribute(Attribute.NEW_PICTURE, fileName);
                commandResult.setPage(page);
                //logger.info("User -> {}, change avatar", user);
            } else {
                //requestContext.setRequestAttribute(RequestAttribute.ERROR_MESSAGE, ErrorMessage.ERROR_WITH_UPLOAD);
                commandResult.setPage(page);
            }
        }
        return commandResult;
    }

    private String buildNewFileName(String oldFileName) {
        String fileFormat = oldFileName.substring(oldFileName.indexOf(FILE_FORMAT_SEPARATOR));
        return UPLOAD_PICTURE_DIRECTORY + "/" + UUID.randomUUID().toString() + fileFormat;
    }
}
