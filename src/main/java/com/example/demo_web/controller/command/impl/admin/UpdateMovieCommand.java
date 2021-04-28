package com.example.demo_web.controller.command.impl.admin;

import com.example.demo_web.controller.command.*;
import com.example.demo_web.exception.CommandException;
import com.example.demo_web.exception.ServiceException;
import com.example.demo_web.model.entity.Movie;
import com.example.demo_web.model.service.MovieService;
import com.example.demo_web.model.service.impl.MovieServiceImpl;
import com.example.demo_web.model.util.message.ErrorMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class UpdateMovieCommand implements ActionCommand {
    private static final Logger logger = LogManager.getLogger();
    private MovieService movieService = MovieServiceImpl.getInstance();
    private static final String DEFAULT_MOVIE_PICTURE = "C:/Epam/pictures/movie.jpg";

    @Override
    public CommandResult execute(SessionRequestContent sessionRequestContent) throws CommandException {
        CommandResult commandResult = new CommandResult(PagePath.MOVIE, TransitionType.REDIRECT);
        String stringId = sessionRequestContent.getRequestParameter(RequestParameter.MOVIE_ID);
        String title = sessionRequestContent.getRequestParameter(RequestParameter.MOVIE_TITLE);
        String description = sessionRequestContent.getRequestParameter(RequestParameter.MOVIE_DESCRIPTION);
        String stringReleaseDate = sessionRequestContent.getRequestParameter(RequestParameter.MOVIE_RELEASE_DATE);
        String picture = sessionRequestContent.getRequestParameter(RequestParameter.PICTURE);
        if (picture == null) {
            picture = DEFAULT_MOVIE_PICTURE;
        }
        String[] stringGenresId = sessionRequestContent.getRequestParameters(RequestParameter.MOVIE_GENRE);
        String[] stringMediaPersonsId = sessionRequestContent.getRequestParameters(RequestParameter.MOVIE_CREW);
        //============================
        if (title == null || description == null || stringReleaseDate == null) {
            sessionRequestContent.setSessionAttribute(Attribute.ERROR_MESSAGE, ErrorMessage.EMPTY_UPDATE_MOVIE_PARAMETERS);
            commandResult.setPage(PagePath.EDIT_MOVIE);
        } else {
            Map.Entry<List<String>,List<String>> validationResult = movieService.validateData(
                    title, description, stringReleaseDate, stringGenresId, stringMediaPersonsId);
            List<String> validParameters = validationResult.getKey();
            List<String> errorMessages = validationResult.getValue();
            if (!errorMessages.isEmpty()) {
                sessionRequestContent.setSessionAttribute(Attribute.VALIDATION_ERRORS, errorMessages);
                if (validParameters.contains(Attribute.MOVIE_TITLE)) sessionRequestContent.setSessionAttribute(
                        Attribute.MOVIE_TITLE, title);
                if (validParameters.contains(Attribute.MOVIE_DESCRIPTION)) sessionRequestContent.setSessionAttribute(
                        Attribute.MOVIE_DESCRIPTION, description);
                if (validParameters.contains(Attribute.MOVIE_RELEASE_DATE)) sessionRequestContent.setSessionAttribute(
                        Attribute.MOVIE_RELEASE_DATE, LocalDate.parse(stringReleaseDate));
                if (validParameters.contains(Attribute.PICTURE)) sessionRequestContent.setSessionAttribute(
                        Attribute.NEW_PICTURE, picture);
                List<Integer> genresId;
                if (stringGenresId != null) {
                    genresId = Arrays.asList(stringGenresId).stream().map(Integer::parseInt).collect(Collectors.toList());
                } else {
                    genresId = new ArrayList<>();
                }
                if (validParameters.contains(Attribute.MOVIE_GENRE)) sessionRequestContent.setSessionAttribute(
                        Attribute.MOVIE_GENRE, genresId);
                List<Integer> mediaPersonsId;
                if (stringMediaPersonsId != null) {
                    mediaPersonsId = Arrays.asList(stringMediaPersonsId).stream().map(Integer::parseInt).collect(Collectors.toList());
                } else {
                    mediaPersonsId = new ArrayList<>();
                }
                if (validParameters.contains(Attribute.MOVIE_CREW)) sessionRequestContent.setSessionAttribute(
                        Attribute.MOVIE_CREW, mediaPersonsId);
                commandResult.setPage(PagePath.EDIT_MOVIE);
            } else {
                Optional<String> errorMessage;
                Optional<Movie> movie;
                Map.Entry<Optional<Movie>, Optional<String>> findResult;
                try {
                    findResult = movieService.update(stringId, title, description, stringReleaseDate, picture, stringGenresId, stringMediaPersonsId);
                    movie = findResult.getKey();
                    errorMessage = findResult.getValue();
                    if (errorMessage.isPresent()) {
                        sessionRequestContent.setSessionAttribute(Attribute.ERROR_MESSAGE, errorMessage.get());
                        commandResult.setPage(PagePath.EDIT_MOVIE);
                    } else {
                        if (movie.isPresent()) {
                            sessionRequestContent.setSessionAttribute(Attribute.MOVIE, movie.get());
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
