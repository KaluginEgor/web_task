package com.example.demo_web.controller.command.impl.admin;

import com.example.demo_web.controller.command.*;
import com.example.demo_web.exception.ServiceException;
import com.example.demo_web.model.entity.MediaPerson;
import com.example.demo_web.model.entity.Movie;
import com.example.demo_web.model.entity.OccupationType;
import com.example.demo_web.model.service.MovieService;
import com.example.demo_web.model.service.impl.MovieServiceImpl;

import java.time.LocalDate;

public class CreateMovieCommand implements ActionCommand {
    private MovieService movieService = new MovieServiceImpl();

    @Override
    public CommandResult execute(SessionRequestContent sessionRequestContent) {
        CommandResult commandResult = new CommandResult();
        commandResult.setTransitionType(TransitionType.FORWARD);
        String title = sessionRequestContent.getRequestParameter(RequestParameter.MOVIE_TITLE);
        String description = sessionRequestContent.getRequestParameter(RequestParameter.MOVIE_DESCRIPTION);
        String stringReleaseDate = sessionRequestContent.getRequestParameter(RequestParameter.MOVIE_RELEASE_DATE);
        LocalDate releaseDate = (stringReleaseDate != null && !stringReleaseDate.isEmpty()) ? LocalDate.parse(stringReleaseDate) : null;
        String picture = sessionRequestContent.getRequestParameter(RequestParameter.PICTURE);
        String[] stringGenresId = sessionRequestContent.getRequestParameters(RequestParameter.MOVIE_GENRE);
        String[] stringMediaPeopleId = sessionRequestContent.getRequestParameters(RequestParameter.MOVIE_CREW);

        try {
            Movie movie = movieService.create(title, description, releaseDate, picture, stringGenresId, stringMediaPeopleId);
            sessionRequestContent.setSessionAttribute(SessionAttribute.MOVIE, movie);
            commandResult.setPage(PagePath.MOVIE);
        } catch (ServiceException e) {
            commandResult.setPage(PagePath.ERROR);
        }
        return commandResult;
    }
}
